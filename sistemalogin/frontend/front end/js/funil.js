// js/funil.js
document.addEventListener('DOMContentLoaded', () => {
    // --- 1. GUARDA DE AUTENTICAÇÃO ---
    const token = localStorage.getItem('jwt_token');
    if (!token) {
        window.location.href = 'index.html';
        return;
    }
    const headers = { 'Authorization': `Bearer ${token}`, 'Content-Type': 'application/json' };

    // --- 2. PEGAR O ID DO FUNIL DA URL E SELETORES ---
    const urlParams = new URLSearchParams(window.location.search);
    const funilId = urlParams.get('id'); // Ex: funil.html?id=1
    const isEditMode = funilId !== null; // Flag para sabermos o modo

    const funnelNameInput = document.getElementById('funnel-name');
    const stagesContainer = document.getElementById('stages-container');
    const saveButton = document.querySelector('.btn-save');
    const addStageColumn = document.querySelector('.add-stage-column'); // Pega a coluna "Adicionar"

    // --- 3. FUNÇÕES DE RENDERIZAÇÃO ---

    /**
     * Renderiza uma coluna de etapa no DOM
     * @param {object} etapa - O objeto da etapa (ex: {id: 1, nome: 'Contato'})
     */
    const renderStageColumn = (etapa) => {
        const newColumn = document.createElement('div');
        newColumn.classList.add('stage-column');
        newColumn.dataset.etapaId = etapa.id; // Armazena o ID da etapa para salvar depois

        const newTitle = etapa.nome;

        newColumn.innerHTML = `
            <h3 class="stage-title">${newTitle}</h3>
            <div class="stage-card">
                <div class="form-group">
                    <label for="stage-name-${etapa.id}">Nome</label>
                    <input type="text" id="stage-name-${etapa.id}" value="${newTitle}">
                </div>
                <button class="delete-stage-btn"><i class="fa-solid fa-trash-can"></i> Excluir etapa</button>
            </div>
        `;
        // Insere antes da coluna "Adicionar nova etapa"
        stagesContainer.insertBefore(newColumn, addStageColumn);

        // Atualiza o input dentro do card para mudar o h3 ao digitar
        const input = newColumn.querySelector(`#stage-name-${etapa.id}`);
        const title = newColumn.querySelector('.stage-title');
        input.addEventListener('input', () => {
            title.textContent = input.value || 'Nova Etapa'; // Atualiza o título em tempo real
        });
    };

    /**
     * Limpa as etapas de exemplo que vêm no HTML
     */
    const clearExampleStages = () => {
        const exampleColumns = stagesContainer.querySelectorAll('.stage-column:not(.add-stage-column)');
        exampleColumns.forEach(col => col.remove());
    };

    // --- 4. BUSCAR DADOS (MODO EDIÇÃO) ---
    const fetchFunilData = async () => {
        if (!isEditMode) {
            // MODO CRIAÇÃO: Apenas limpa etapas de exemplo do HTML
            clearExampleStages();
            return;
        }

        // MODO EDIÇÃO: Busca dados do funil e etapas
        try {
            const funilResponse = await fetch(`http://localhost:8080/api/funis/${funilId}`, { headers });
            if (!funilResponse.ok) throw new Error('Funil não encontrado');
            const funil = await funilResponse.json();
            funnelNameInput.value = funil.nome;

            const etapasResponse = await fetch(`http://localhost:8080/api/etapas?funilId=${funilId}`, { headers });
            if (!etapasResponse.ok) throw new Error('Erro ao buscar etapas');
            const etapas = await etapasResponse.json(); // Assumindo que vêm ordenadas

            clearExampleStages(); // Limpa exemplos
            etapas.forEach(etapa => renderStageColumn(etapa)); // Renderiza as etapas buscadas

        } catch (error) {
            console.error('Erro ao buscar dados do funil:', error);
            alert(error.message);
            window.location.href = 'dashboard.html'; // Volta se der erro
        }
    };

    // --- 5. LÓGICA PARA SALVAR (CRIAR OU ATUALIZAR) ---
    saveButton.addEventListener('click', async () => {
        
        // 1. Coletar dados do Funil
        const funilData = { nome: funnelNameInput.value };
        if (!funilData.nome.trim()) {
            alert('Por favor, dê um nome ao funil.');
            return;
        }
        
        // 2. Coletar dados das Etapas (lendo do DOM)
        const stageColumns = stagesContainer.querySelectorAll('.stage-column:not(.add-stage-column)');
        const etapasData = [];
        stageColumns.forEach((column, index) => {
            const input = column.querySelector('input[type="text"]');
            const etapaId = column.dataset.etapaId; // Pega o ID (undefined se for nova etapa em modo edição)

            etapasData.push({
                id: etapaId ? parseInt(etapaId) : null, // ID da etapa (null se for nova)
                nome: input.value,
                posicao: index // A ordem no DOM define a posição
            });
        });

        if (etapasData.length === 0) {
            alert('Você precisa adicionar pelo menos uma etapa ao funil.');
            return;
        }
        
        // --- Inicia o processo de salvar ---
        try {
            let savedFunilId = funilId; // ID do funil para salvar as etapas

            if (isEditMode) {
                // --- MODO EDIÇÃO (PUT Funil) ---
                console.log("Modo Edição: Atualizando Funil...");
                await fetch(`http://localhost:8080/api/funis/${funilId}`, {
                    method: 'PUT',
                    headers: headers,
                    body: JSON.stringify(funilData)
                });
            } else {
                // --- MODO CRIAÇÃO (POST Funil) ---
                console.log("Modo Criação: Criando Funil...");
                const response = await fetch(`http://localhost:8080/api/funis`, {
                    method: 'POST',
                    headers: headers,
                    body: JSON.stringify(funilData)
                });
                if (!response.ok) throw new Error('Erro ao criar o funil.');
                const novoFunil = await response.json();
                savedFunilId = novoFunil.id; // Pega o ID do funil recém-criado
            }

            // --- SALVAR ETAPAS (A LÓGICA COMPLEXA) ---
            // Agora, salvamos as etapas.
            // O ideal é ter um endpoint no back-end que receba a lista de etapas de uma vez (batch update).
            // Se não tiver, precisamos fazer um loop e salvar uma por uma.

            console.log("Salvando etapas...", etapasData);
            
            // Vamos assumir que sua API tem endpoints /api/etapas (POST) e /api/etapas/{id} (PUT)
            for (const etapa of etapasData) {
                
                const payload = {
                    nome: etapa.nome,
                    posicao: etapa.posicao,
                    funilId: savedFunilId // Associa ao funil
                };
                
                if (etapa.id) {
                    // Atualiza etapa existente (PUT)
                    await fetch(`http://localhost:8080/api/etapas/${etapa.id}`, {
                       method: 'PUT',
                       headers: headers,
                       body: JSON.stringify(payload)
                    });
                } else {
                    // Cria nova etapa (POST)
                     await fetch(`http://localhost:8080/api/etapas`, {
                       method: 'POST',
                       headers: headers,
                       body: JSON.stringify(payload)
                    });
                }
            }

            // (Opcional: Lógica para DELETAR etapas que foram removidas da tela,
            // o que exigiria guardar os IDs originais e comparar com os atuais)

            alert('Funil salvo com sucesso!');
            window.location.href = 'dashboard.html';

        } catch (error) {
            console.error('Erro ao salvar funil:', error);
            alert('Erro ao salvar funil: ' + error.message);
        }
    });

    // --- 6. INICIALIZAÇÃO ---
    fetchFunilData(); // Busca os dados do funil (se for modo de edição) ou limpa (modo criação)
});
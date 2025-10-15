document.addEventListener('DOMContentLoaded', () => {
    // --- 1. GUARDA DE AUTENTICAÇÃO E LÓGICA DE LOGOUT (Já implementado) ---
    const token = localStorage.getItem('jwt_token');
    if (!token) {
        window.location.href = 'index.html';
        return;
    }
    document.querySelectorAll('a[href="index.html"]').forEach(link => {
        link.addEventListener('click', (event) => {
            event.preventDefault();
            localStorage.removeItem('jwt_token');
            window.location.href = 'index.html';
        });
    });

    const headers = {
        'Authorization': `Bearer ${token}`,
        'Content-Type': 'application/json'
    };

    // --- 2. LÓGICA DO KANBAN ---
    const kanbanBoard = document.querySelector('.kanban-board');
    const funnelSelector = document.querySelector('.custom-select-wrapper');

    // Função para buscar etapas e negócios e renderizar o quadro
    const renderKanban = async (funilId) => {
        if (!funilId) return;

        try {
            // Buscar as etapas do funil selecionado
            const etapasResponse = await fetch(`http://localhost:8080/api/etapas?funilId=${funilId}`, { headers });
            const etapas = await etapasResponse.json();

            // Limpa o quadro atual
            kanbanBoard.innerHTML = '';

            if (etapas.length > 0) {
                // Para cada etapa, cria uma coluna
                etapas.forEach(etapa => {
                    const column = document.createElement('div');
                    column.classList.add('kanban-column');
                    column.setAttribute('data-etapa-id', etapa.id);
                    column.innerHTML = `
                        <h2 class="column-title">${etapa.nome}</h2>
                        <div class="cards-container"></div>
                    `;
                    kanbanBoard.appendChild(column);
                });

                // Buscar os negócios e distribuí-los nas colunas
                const negociosResponse = await fetch(`http://localhost:8080/api/negocios`, { headers });
                const negocios = await negociosResponse.json();

                negocios.forEach(negocio => {
                    // Encontra a coluna correta para o negócio
                    const column = kanbanBoard.querySelector(`.kanban-column[data-etapa-id='${negocio.etapaId}']`);
                    if (column) {
                        const card = document.createElement('div');
                        card.classList.add('kanban-card');
                        card.innerHTML = `
                            <h4>${negocio.titulo}</h4>
                            <p>${negocio.organizacaoNome}</p>
                            <span>${new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(negocio.valor)}</span>
                        `;
                        column.querySelector('.cards-container').appendChild(card);
                    }
                });
            } else {
                kanbanBoard.innerHTML = '<p>Este funil ainda não possui etapas. Edite-o para adicioná-las.</p>';
            }

        } catch (error) {
            console.error('Erro ao renderizar o Kanban:', error);
        }
    };

    // --- 3. LÓGICA DO SELETOR DE FUNIL (Já implementado, agora com chamada de renderização) ---
    // (O código para popular o seletor de funis continua aqui)
    // Dentro do evento de clique de uma opção do funil, adicione:
    // const funilId = item.getAttribute('data-value');
    // renderKanban(funilId);


    // --- 4. LÓGICA DO MODAL "ADICIONAR NEGÓCIO" ---
    const modalForm = document.querySelector('.modal-form');
    modalForm.addEventListener('submit', async (event) => {
        event.preventDefault();

        // Simulação de pegar dados do formulário
        const negocioData = {
            titulo: document.getElementById('title').value,
            valor: parseFloat(document.getElementById('value').value),
            // Aqui você precisaria buscar os IDs de pessoa, funil e etapa
            pessoaId: 1, // Exemplo: você precisaria de um seletor de pessoas
            funilId: 1,  // Exemplo: pegar o ID do funil selecionado
            etapaId: 1,  // Exemplo: pegar o ID da primeira etapa do funil
            organizacao: document.getElementById('organization').value
        };

        try {
            const response = await fetch('http://localhost:8080/api/negocios', {
                method: 'POST',
                headers: headers,
                body: JSON.stringify(negocioData)
            });

            if (response.ok) {
                alert('Negócio criado com sucesso!');
                closeModal(); // Função do seu script.js
                renderKanban(negocioData.funilId); // Atualiza o quadro
            } else {
                alert('Falha ao criar o negócio.');
            }
        } catch (error) {
            console.error('Erro ao criar negócio:', error);
        }
    });

    // Iniciar a renderização com o primeiro funil
    // (Esta lógica precisa ser integrada com a busca de funis que já fizemos)
});
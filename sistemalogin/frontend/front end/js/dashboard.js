document.addEventListener('DOMContentLoaded', async () => {
    // --- 1. GUARDA DE AUTENTICAÇÃO E CONFIGURAÇÕES GLOBAIS ---
    const token = localStorage.getItem('jwt_token');
    if (!token) {
        window.location.href = 'index.html';
        return;
    }
    const headers = {
        'Authorization': `Bearer ${token}`,
        'Content-Type': 'application/json'
    };
    let funis = []; // Armazena a lista de funis do usuário
    let currentEtapaIdForModal = null; // Armazena o ID da etapa ao abrir o modal

    // --- 2. SELETORES DO DOM ---
    const kanbanBoard = document.getElementById('kanban-board');
    const funnelTriggerText = document.getElementById('funnel-trigger-text');
    const funnelOptionsList = document.getElementById('funnel-options-list');
    const modalOverlay = document.querySelector('.modal-overlay'); // Seleciona o overlay
    const modal = document.querySelector('.modal'); // Seleciona o modal
    const modalForm = document.getElementById('modal-form');
    const userProfileIcon = document.getElementById('user-profile-icon');
    const closeModalBtns = document.querySelectorAll('.modal-close-btn, [data-close-modal]'); // Seleciona botões de fechar

    // --- 3. FUNÇÕES DE RENDERIZAÇÃO E API ---

    // Busca dados do usuário atual
    const fetchCurrentUser = async () => {
        try {
            const response = await fetch('http://localhost:8080/api/users/me', { headers });
            if (response.ok) {
                const user = await response.json();
                if (user.nome) {
                    userProfileIcon.textContent = user.nome.charAt(0).toUpperCase();
                }
            } else { // Trata token inválido/expirado
                localStorage.removeItem('jwt_token');
                window.location.href = 'index.html';
            }
        } catch (error) {
            console.error('Erro ao buscar usuário atual:', error);
        }
    };

    // Renderiza o quadro Kanban
    const renderKanban = async (funilId) => {
        if (!funilId) {
            kanbanBoard.innerHTML = '<h2>Selecione um funil para começar.</h2>';
            return;
        }
        kanbanBoard.innerHTML = '<h2>Carregando Kanban...</h2>'; // Feedback visual

        try {
            const [etapasResponse, negociosResponse] = await Promise.all([
                fetch(`http://localhost:8080/api/etapas?funilId=${funilId}`, { headers }),
                fetch('http://localhost:8080/api/negocios', { headers })
            ]);

            if (!etapasResponse.ok || !negociosResponse.ok) {
                 throw new Error(`Falha ao buscar dados: Etapas ${etapasResponse.status}, Negócios ${negociosResponse.status}`);
            }

            let etapas = await etapasResponse.json();
            const negocios = await negociosResponse.json();
            etapas.sort((a, b) => a.posicao - b.posicao); // Ordena etapas pela posição

            kanbanBoard.innerHTML = '';

            // Renderiza Colunas de Etapas
            etapas.forEach(etapa => {
                const column = document.createElement('div');
                column.className = 'kanban-column';
                column.dataset.etapaId = etapa.id;
                column.innerHTML = `
                    <div class="kanban-column-header">
                        <h2 class="column-title">${etapa.nome}</h2>
                        <button class="btn-new-deal-in-column" data-etapa-id="${etapa.id}" title="Adicionar Negócio nesta Etapa">
                            <i class="fa-solid fa-plus"></i>
                        </button>
                    </div>
                    <div class="cards-container"></div>`;
                kanbanBoard.appendChild(column);

                const cardsContainer = column.querySelector('.cards-container');
                const negociosNestaEtapa = negocios.filter(neg => neg.etapaId === etapa.id && neg.funilId == funilId);

                // Renderiza os Cards de Negócio
                negociosNestaEtapa.forEach(negocio => {
                    const card = document.createElement('div');
                    card.className = 'kanban-card';
                    // Adicionando mais detalhes ao card
                    card.innerHTML = `
                        <h4>${negocio.titulo}</h4>
                        <p><i class="fa-solid fa-building"></i> ${negocio.organizacaoNome || 'Sem organização'}</p>
                        <p><i class="fa-solid fa-user"></i> ${negocio.pessoaNome}</p>
                        <span class="deal-value">${new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(negocio.valor)}</span>
                    `;
                    cardsContainer.appendChild(card);
                });
            });

            // Adiciona a coluna "Adicionar Etapa" (se necessário)
            // (Você pode adicionar a lógica do botão 'add-stage-button' aqui se desejar)

        } catch (error) {
            console.error("Erro ao renderizar o Kanban:", error);
            kanbanBoard.innerHTML = '<h2>Erro ao carregar o Kanban. Tente novamente.</h2>';
        }
    };

    // Busca e popula o seletor de funis
    const fetchAndPopulateFunnels = async () => {
        try {
            const response = await fetch('http://localhost:8080/api/funis', { headers });
            if (!response.ok) throw new Error(`Status: ${response.status}`);
            funis = await response.json();

            const newFunnelOption = document.createElement('li');
            newFunnelOption.className = 'new-funnel-option';
            newFunnelOption.dataset.value = 'novo';
            newFunnelOption.innerHTML = '<i class="fa-solid fa-plus"></i> Novo Funil';
            newFunnelOption.addEventListener('click', () => { window.location.href = 'funil.html'; }); // Adiciona listener aqui

            funnelOptionsList.innerHTML = '';
            funnelOptionsList.appendChild(newFunnelOption);

            if (funis.length > 0) {
                funis.forEach((funil, index) => {
                    const li = document.createElement('li');
                    li.textContent = funil.nome;
                    li.dataset.value = funil.id;
                    if (index === 0) {
                        li.classList.add('selected');
                        funnelTriggerText.textContent = funil.nome;
                        funnelTriggerText.dataset.selectedFunilId = funil.id;
                        renderKanban(funil.id);
                    }
                    li.addEventListener('click', () => {
                        // Lógica para trocar seleção no dropdown (do script.js, adaptada)
                        const previouslySelected = funnelOptionsList.querySelector('li.selected');
                        if(previouslySelected) previouslySelected.classList.remove('selected');
                        li.classList.add('selected');
                        document.querySelector('.custom-select-options').classList.add('hidden'); // Fecha o dropdown

                        funnelTriggerText.textContent = funil.nome;
                        funnelTriggerText.dataset.selectedFunilId = funil.id;
                        renderKanban(funil.id);
                    });
                    funnelOptionsList.insertBefore(li, newFunnelOption);
                });
            } else {
                funnelTriggerText.textContent = "Crie um funil";
                kanbanBoard.innerHTML = '<h2>Você ainda não tem funis. <a href="funil.html">Crie um</a> para começar.</h2>';
            }
        } catch (error) {
            console.error("Erro ao buscar funis:", error);
            funnelTriggerText.textContent = "Erro ao carregar";
        }
    };

    // --- 4. FUNÇÕES DO MODAL ---
    const openModal = () => {
        const dataAberturaInput = document.getElementById('modal-data-abertura');
        const dataFechamentoInput = document.getElementById('modal-data-fechamento');
        const hoje = new Date().toISOString().split('T')[0];

        dataAberturaInput.value = hoje;
        dataFechamentoInput.min = hoje;
        dataFechamentoInput.value = ''; // Limpa data de fechamento

        modalForm.reset(); // Limpa outros campos
        dataAberturaInput.value = hoje; // Redefine data de abertura após reset

        modalOverlay.classList.remove('hidden');
        modal.classList.remove('hidden');
    };

    const closeModal = () => {
        modalOverlay.classList.add('hidden');
        modal.classList.add('hidden');
        currentEtapaIdForModal = null; // Limpa a etapa selecionada
    };

    // --- 5. EVENT LISTENERS ---

    // Listener de delegação no Kanban para abrir o modal
    kanbanBoard.addEventListener('click', (event) => {
        const newDealButton = event.target.closest('.btn-new-deal-in-column');
        if (newDealButton) {
            const selectedFunilId = funnelTriggerText.dataset.selectedFunilId;
            if (!selectedFunilId || selectedFunilId === 'undefined') {
                alert("Erro: Selecione um funil primeiro.");
                return;
            }
            currentEtapaIdForModal = newDealButton.dataset.etapaId;
            openModal(); // Chama a função para abrir e preparar o modal
        }
    });

    // Listeners para fechar o modal
    closeModalBtns.forEach(btn => btn.addEventListener('click', closeModal));
    modalOverlay.addEventListener('click', (event) => {
        if (event.target === modalOverlay) closeModal();
    });

    // Submissão do formulário do modal
    modalForm.addEventListener('submit', async (event) => {
        event.preventDefault();

        const selectedFunilId = funnelTriggerText.dataset.selectedFunilId;
        if (!currentEtapaIdForModal) {
            alert("Erro crítico: A etapa de destino não foi identificada.");
            return;
        }

        const dataAberturaStr = document.getElementById('modal-data-abertura').value;
        const dataFechamentoStr = document.getElementById('modal-data-fechamento').value;

        // Validação de Data
        if (!dataAberturaStr) {
            alert("A Data de Abertura é obrigatória."); return;
        }
        if (dataFechamentoStr) {
            const dataAbertura = new Date(dataAberturaStr + 'T00:00:00');
            const dataFechamento = new Date(dataFechamentoStr + 'T00:00:00');
            if (dataFechamento < dataAbertura) {
                alert("A Data de Fechamento deve ser igual ou posterior à Data de Abertura."); return;
            }
        }

        const negocioData = {
            titulo: document.getElementById('modal-titulo').value,
            valor: parseFloat(document.getElementById('modal-valor').value),
            organizacao: document.getElementById('modal-organizacao').value,
            pessoaContato: document.getElementById('modal-pessoa-nome').value,
            emailPessoaContato: document.getElementById('modal-pessoa-email').value,
            telefonePessoaContato: document.getElementById('modal-pessoa-telefone').value,
            dataDeAbertura: dataAberturaStr, // Envia YYYY-MM-DD
            data_de_fechamento: dataFechamentoStr ? dataFechamentoStr : null, // Envia YYYY-MM-DD ou null
            funilId: parseInt(selectedFunilId),
            etapaId: parseInt(currentEtapaIdForModal)
        };

        console.log("Enviando dados do negócio:", negocioData);

        try {
            const response = await fetch('http://localhost:8080/api/negocios', {
                method: 'POST',
                headers: headers,
                body: JSON.stringify(negocioData)
            });

            if (response.ok) {
                alert('Negócio criado com sucesso!');
                closeModal(); // Fecha o modal usando a função
                renderKanban(selectedFunilId); // Atualiza o quadro
            } else {
                const errorData = await response.json();
                alert('Erro ao criar negócio: ' + (errorData.message || response.statusText));
            }
        } catch (error) {
            console.error("Erro ao submeter negócio:", error);
            alert("Erro de rede ao criar negócio.");
        }
    });

    // --- 6. INICIALIZAÇÃO DA PÁGINA ---
    fetchCurrentUser(); // Busca o usuário atual para o ícone
    fetchAndPopulateFunnels(); // Busca os funis e renderiza o primeiro Kanban

}); // Fim do DOMContentLoaded
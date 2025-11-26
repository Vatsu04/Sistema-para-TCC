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
    let currentNegocioIdForEdit = null; // null = criando, ID = editando

    // --- 2. SELETORES DO DOM ---
    const kanbanBoard = document.getElementById('kanban-board');
    const funnelTriggerText = document.getElementById('funnel-trigger-text');
    const funnelOptionsList = document.getElementById('funnel-options-list');
    const modalOverlay = document.querySelector('.modal-overlay'); // Seleciona o overlay
    const modal = document.querySelector('.modal'); // Seleciona o modal
    const modalForm = document.getElementById('modal-form');
    const userProfileIcon = document.getElementById('user-profile-icon');
    const closeModalBtns = document.querySelectorAll('.modal-close-btn, [data-close-modal]'); // Seleciona botões de fechar
    const editFunnelBtn = document.getElementById('edit-funnel-btn');
    const deleteFunnelBtn = document.getElementById('delete-funnel-btn');

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
        kanbanBoard.innerHTML = '<h2>Carregando Kanban...</h2>';

        try {
            const [etapasResponse, negociosResponse] = await Promise.all([
                fetch(`http://localhost:8080/api/etapas?funilId=${funilId}`, { headers }),
                fetch('http://localhost:8080/api/negocios', { headers })
            ]);

            if (!etapasResponse.ok || !negociosResponse.ok) {
                throw new Error(`Falha ao buscar dados.`);
            }

            let etapas = await etapasResponse.json();
            const negocios = await negociosResponse.json();
            etapas.sort((a, b) => a.posicao - b.posicao);

            kanbanBoard.innerHTML = '';

        
            etapas.forEach(etapa => {
                const column = document.createElement('div');
                column.className = 'kanban-column';
                column.dataset.etapaId = etapa.id; 
                
                column.innerHTML = `
                    <div class="kanban-column-header">
                        <div class="column-title-wrapper">
                            <h2 class="column-title">${etapa.nome}</h2>
                        </div>
                        <div class="stage-actions">
                            <button class="btn-icon-stage edit" title="Renomear Etapa" onclick="window.editStage(${etapa.id}, '${etapa.nome}')">
                                <i class="fa-solid fa-pen"></i>
                            </button>
                            <button class="btn-icon-stage delete" title="Excluir Etapa e Negócios" onclick="window.deleteStage(${etapa.id})">
                                <i class="fa-solid fa-trash"></i>
                            </button>
                        </div>
                    </div>
                    
                    <div style="margin-bottom: 10px;">
                        <button class="btn-new-deal-in-column" data-etapa-id="${etapa.id}" style="width:100%">
                            <i class="fa-solid fa-plus"></i> Novo Negócio
                        </button>
                    </div>

                    <div class="cards-container"></div>`; 
                
                kanbanBoard.appendChild(column);

                const cardsContainer = column.querySelector('.cards-container');
                
                // Listeners de Drop Zone (Drag & Drop)
                cardsContainer.addEventListener('dragover', handleDragOver);
                cardsContainer.addEventListener('dragenter', handleDragEnter);
                cardsContainer.addEventListener('dragleave', handleDragLeave);
                cardsContainer.addEventListener('drop', handleDrop);

                // Renderiza Cards
                const negociosNestaEtapa = negocios.filter(neg => neg.etapaId === etapa.id && neg.funilId == funilId);
                negociosNestaEtapa.forEach(negocio => {
                    const card = document.createElement('div');
                    card.className = 'kanban-card';
                    card.draggable = true;
                    card.dataset.negocioId = negocio.id; // Importante para a busca funcionar
                    card.dataset.currentEtapaId = etapa.id;

                    card.innerHTML = `
                        <div class="card-header">
                            <h4>${negocio.titulo}</h4>
                            <div class="card-actions">
                                <button class="btn-card-action" onclick="window.editNegocio(${negocio.id})" title="Editar">
                                    <i class="fa-solid fa-pen"></i>
                                </button>
                                <button class="btn-card-action delete" onclick="window.deleteNegocio(${negocio.id})" title="Excluir">
                                    <i class="fa-solid fa-trash"></i>
                                </button>
                            </div>
                        </div>
                        <p><i class="fa-solid fa-building"></i> ${negocio.organizacao || 'Sem organização'}</p>
                        <p><i class="fa-solid fa-user"></i> ${negocio.pessoaNome}</p>
                        <span class="deal-value">${new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(negocio.valor)}</span>
                    `;
                    card.addEventListener('dragstart', handleDragStart);
                    card.addEventListener('dragend', handleDragEnd);
                    cardsContainer.appendChild(card);
                });
            });

            // 2. Renderiza Coluna "Adicionar Nova Etapa" no final
            const addStageDiv = document.createElement('div');
            addStageDiv.className = 'add-new-stage-column';
            addStageDiv.innerHTML = `
                <button class="btn-add-stage-kanban" onclick="window.createNewStage()">
                    <i class="fa-solid fa-plus"></i> Adicionar Etapa
                </button>
            `;
            kanbanBoard.appendChild(addStageDiv);

        } catch (error) {
            console.error("Erro ao renderizar o Kanban:", error);
            kanbanBoard.innerHTML = '<h2>Erro ao carregar o Kanban.</h2>';
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
    
    // Função unificada para abrir modal (Criação ou Edição)
    const openModal = (negocio = null) => {
        modalOverlay.classList.remove('hidden');
        modal.classList.remove('hidden');
        const modalTitle = document.querySelector('.modal-header h2');
        const btnSubmit = modalForm.querySelector('button[type="submit"]');

        if (negocio) {
            // --- MODO EDIÇÃO ---
            currentNegocioIdForEdit = negocio.id;
            currentEtapaIdForModal = negocio.etapaId; 
            
            modalTitle.textContent = "Editar Negócio";
            btnSubmit.textContent = "Salvar Alterações";


            document.getElementById('modal-titulo').value = negocio.titulo;
            document.getElementById('modal-pessoa-nome').value = negocio.pessoaNome;
            document.getElementById('modal-pessoa-email').value = negocio.emailPessoaContato;
            document.getElementById('modal-pessoa-telefone').value = negocio.telefonePessoaContato;
            document.getElementById('modal-organizacao').value = negocio.organizacao;
            document.getElementById('modal-valor').value = negocio.valor;
            document.getElementById('modal-data-abertura').value = negocio.dataDeAbertura; // YYYY-MM-DD
            document.getElementById('modal-data-fechamento').value = negocio.dataDeFechamento || '';

        } else {
            // --- MODO CRIAÇÃO ---
            currentNegocioIdForEdit = null;


            modalTitle.textContent = "Adicionar Negócio";
            btnSubmit.textContent = "Adicionar";

            modalForm.reset();
            
            const hoje = new Date().toISOString().split('T')[0];
            document.getElementById('modal-data-abertura').value = hoje;
            document.getElementById('modal-data-fechamento').min = hoje;
        }
    };

    const closeModal = () => {
        modalOverlay.classList.add('hidden');
        modal.classList.add('hidden');
        currentEtapaIdForModal = null; 
        currentNegocioIdForEdit = null; 
    };

    kanbanBoard.addEventListener('click', (event) => {
        const newDealButton = event.target.closest('.btn-new-deal-in-column');
        if (newDealButton) {
            const selectedFunilId = funnelTriggerText.dataset.selectedFunilId;
            if (!selectedFunilId || selectedFunilId === 'undefined') {
                alert("Erro: Selecione um funil primeiro.");
                return;
            }
            currentEtapaIdForModal = newDealButton.dataset.etapaId;
            openModal();
        }
    });

    closeModalBtns.forEach(btn => btn.addEventListener('click', closeModal));
    modalOverlay.addEventListener('click', (event) => {
        if (event.target === modalOverlay) closeModal();
    });

    modalForm.addEventListener('submit', async (event) => {
        event.preventDefault();

        const selectedFunilId = funnelTriggerText.dataset.selectedFunilId;

        const dataAberturaStr = document.getElementById('modal-data-abertura').value;
        const dataFechamentoStr = document.getElementById('modal-data-fechamento').value;

        if (!dataAberturaStr) {
            alert("A Data de Abertura é obrigatória."); return;
        }
        if (dataFechamentoStr && dataFechamentoStr < dataAberturaStr) {
            alert("Data de fechamento não pode ser anterior à abertura."); return;
        }

        const negocioData = {
            titulo: document.getElementById('modal-titulo').value,
            valor: parseFloat(document.getElementById('modal-valor').value),
            organizacao: document.getElementById('modal-organizacao').value,
            pessoaContato: document.getElementById('modal-pessoa-nome').value,
            emailPessoaContato: document.getElementById('modal-pessoa-email').value,
            telefonePessoaContato: document.getElementById('modal-pessoa-telefone').value,
            dataDeAbertura: dataAberturaStr,
            data_de_fechamento: dataFechamentoStr || null,
            funilId: parseInt(selectedFunilId),
            etapaId: parseInt(currentEtapaIdForModal)
        };

        try {
            let response;
            
            if (currentNegocioIdForEdit) {
                response = await fetch(`http://localhost:8080/api/negocios/${currentNegocioIdForEdit}`, {
                    method: 'PUT',
                    headers: headers,
                    body: JSON.stringify(negocioData)
                });
            } else {
                if (!currentEtapaIdForModal) { alert("Erro de etapa."); return; }
                
                response = await fetch('http://localhost:8080/api/negocios', {
                    method: 'POST',
                    headers: headers,
                    body: JSON.stringify(negocioData)
                });
            }

            if (response.ok) {
                alert(currentNegocioIdForEdit ? 'Negócio atualizado!' : 'Negócio criado!');
                closeModal();
                renderKanban(selectedFunilId);
            } else {
                const errorData = await response.json();
                alert('Erro: ' + (errorData.message || response.statusText));
            }
        } catch (error) {
            console.error("Erro ao salvar negócio:", error);
            alert("Erro de conexão.");
        }
    });

    const handleDragStart = (event) => {
        const card = event.target.closest('.kanban-card');
        if (!card) return;
        
        event.dataTransfer.setData("text/plain", card.dataset.negocioId);
        event.dataTransfer.effectAllowed = "move";
        setTimeout(() => { 
            card.classList.add('dragging');
        }, 0);
    };

    const handleDragEnd = (event) => {
        const card = event.target.closest('.kanban-card');
        if (card) {
            card.classList.remove('dragging');
        }
    };

    const handleDragOver = (event) => {
        event.preventDefault(); 
        event.dataTransfer.dropEffect = "move";
    };

    const handleDragEnter = (event) => {
        const container = event.target.closest('.cards-container');
        if (container) {
            container.classList.add('drag-over');
        }
    };

    const handleDragLeave = (event) => {

        const container = event.target.closest('.cards-container');
        if (container) {
            container.classList.remove('drag-over');
        }
    };

    const handleDrop = async (event) => {
        event.preventDefault();
        const cardsContainer = event.target.closest('.cards-container');
        const column = event.target.closest('.kanban-column');

        if (!column || !cardsContainer) return;

        cardsContainer.classList.remove('drag-over');

        const negocioId = event.dataTransfer.getData("text/plain");
        const newEtapaId = column.dataset.etapaId;
        const draggedCard = document.querySelector(`.kanban-card[data-negocio-id="${negocioId}"]`);
        
        if (!draggedCard) return;

        const originalEtapaId = draggedCard.dataset.currentEtapaId;

        if (newEtapaId === originalEtapaId) {
            return;
        }


        cardsContainer.appendChild(draggedCard);
        draggedCard.dataset.currentEtapaId = newEtapaId; 


        try {
            await updateNegocioEtapa(negocioId, newEtapaId);

        } catch (error) {
            console.error("Falha ao atualizar etapa:", error);
            alert("Erro ao mover o card. A alteração será desfeita.");
            renderKanban(funnelTriggerText.dataset.selectedFunilId); 
        }
    };

    const updateNegocioEtapa = async (negocioId, newEtapaId) => {
        try {
            // 1. Buscar (GET) o estado atual do negócio
            const getResponse = await fetch(`http://localhost:8080/api/negocios/${negocioId}`, {
                method: 'GET',
                headers: headers
            });

            if (!getResponse.ok) {
                throw new Error(`Falha ao buscar negócio: ${getResponse.status}`);
            }

            const negocio = await getResponse.json();
            
            negocio.etapaId = parseInt(newEtapaId);

            const putResponse = await fetch(`http://localhost:8080/api/negocios/${negocioId}`, {
                method: 'PUT', 
                headers: headers,
                body: JSON.stringify(negocio) 
            });

            if (!putResponse.ok) {
                const errorData = await putResponse.json();
                console.error("Erro do PUT:", errorData);
                throw new Error(errorData.message || `Status ${putResponse.status}`);
            }
            

            console.log('Negócio movido com sucesso na API.');

        } catch (error) {
            console.error("Erro detalhado em updateNegocioEtapa:", error);

            throw error; 
        }
    };

    if (editFunnelBtn) {
        editFunnelBtn.addEventListener('click', async () => {
            const funilId = funnelTriggerText.dataset.selectedFunilId;
            const currentName = funnelTriggerText.textContent;

            if (!funilId || funilId === 'undefined' || funilId === 'novo') {
                alert("Selecione um funil válido para editar.");
                return;
            }

            const newName = prompt("Novo nome do funil:", currentName);

            if (!newName || newName.trim() === "" || newName === currentName) {
                return; 
            }

            try {
                const response = await fetch(`http://localhost:8080/api/funis/${funilId}`, {
                    method: 'PUT',
                    headers: headers,
                    body: JSON.stringify({ nome: newName }) 
                });

                if (response.ok) {
                    alert('Funil renomeado com sucesso!');
                    funnelTriggerText.textContent = newName;
                    fetchAndPopulateFunnels(); 
                } else {
                    const err = await response.json();
                    alert('Erro ao atualizar: ' + (err.message || response.statusText));
                }
            } catch (error) {
                console.error("Erro ao renomear funil:", error);
                alert("Erro de conexão ao tentar renomear o funil.");
            }
        });
    }

    if (deleteFunnelBtn) {
        deleteFunnelBtn.addEventListener('click', async () => {
            const funilId = funnelTriggerText.dataset.selectedFunilId;
            const funilNome = funnelTriggerText.textContent;

            if (!funilId || funilId === 'undefined' || funilId === 'novo') {
                alert("Selecione um funil válido para excluir.");
                return;
            }

            const confirmacao = confirm(
                `PERIGO: Você está prestes a excluir o funil "${funilNome}".\n\n` +
                `Isso irá apagar PERMANENTEMENTE:\n` +
                `- Todas as etapas deste funil.\n` +
                `- Todos os negócios/cards dentro deste funil.\n\n` +
                `Tem certeza absoluta que deseja continuar?`
            );

            if (!confirmacao) return;

            try {
                const response = await fetch(`http://localhost:8080/api/funis/${funilId}`, {
                    method: 'DELETE',
                    headers: headers
                });

                if (response.ok || response.status === 204) {
                    alert('Funil excluído com sucesso.');
                    window.location.reload(); 
                } else {
                    const err = await response.json();
                    alert('Erro ao excluir funil: ' + (err.message || response.statusText));
                }
            } catch (error) {
                console.error("Erro ao excluir funil:", error);
                alert("Erro de conexão ao tentar excluir o funil.");
            }
        });
    }

    // Ações de Etapa
    window.createNewStage = async () => {
        const funilId = document.getElementById('funnel-trigger-text').dataset.selectedFunilId;
        if (!funilId) { alert('Erro: Nenhum funil selecionado.'); return; }

        const nome = prompt("Digite o nome da nova etapa:");
        if (!nome || !nome.trim()) return;

        try {
            const response = await fetch('http://localhost:8080/api/etapas', {
                method: 'POST',
                headers: headers,
                body: JSON.stringify({
                    nome: nome,
                    funilId: parseInt(funilId)
                })
            });

            if (response.ok) {
                renderKanban(funilId); 
            } else {
                alert('Erro ao criar etapa.');
            }
        } catch (error) {
            console.error(error);
            alert('Erro de conexão.');
        }
    };

    window.editStage = async (etapaId, currentName) => {
        const funilId = document.getElementById('funnel-trigger-text').dataset.selectedFunilId;
        const newName = prompt("Renomear etapa:", currentName);
        
        if (!newName || newName.trim() === "" || newName === currentName) return;

        try {
            const response = await fetch(`http://localhost:8080/api/etapas/${etapaId}`, {
                method: 'PUT',
                headers: headers,
                body: JSON.stringify({ nome: newName }) 
            });

            if (response.ok) {
                renderKanban(funilId);
            } else {
                alert('Erro ao atualizar etapa.');
            }
        } catch (error) {
            console.error(error);
        }
    };

        window.deleteStage = async (etapaId) => {
        // 1. Busca o elemento da coluna pelo ID
        const columnElement = document.querySelector(`.kanban-column[data-etapa-id="${etapaId}"]`);
        
        // 2. Extrai o texto do título (h2)
        // Se por algum motivo não achar, usa um texto padrão "esta etapa"
        const etapaNome = columnElement ? columnElement.querySelector('.column-title').textContent : 'esta etapa';

        const confirmMsg = `ATENÇÃO: Ao excluir a etapa "${etapaNome}", TODOS OS NEGÓCIOS dentro dela também serão excluídos permanentemente.\n\nDeseja continuar?`;
        
        if (!confirm(confirmMsg)) return;

        try {
            const response = await fetch(`http://localhost:8080/api/etapas/${etapaId}`, {
                method: 'DELETE',
                headers: headers
            });

            if (response.ok || response.status === 204) {
                const funilId = document.getElementById('funnel-trigger-text').dataset.selectedFunilId;
                renderKanban(funilId);
            } else {
                const err = await response.json();
                alert('Erro ao excluir: ' + (err.message || 'Verifique se existem negócios nesta etapa.'));
            }
        } catch (error) {
            console.error(error);
            alert('Erro de conexão.');
        }
    };
    // Ações de Negócio
    window.editNegocio = async (negocioId) => {
        try {
            // Busca os dados atuais do negócio para preencher o modal
            const response = await fetch(`http://localhost:8080/api/negocios/${negocioId}`, { headers });
            if (response.ok) {
                const negocio = await response.json();
                openModal(negocio); // Abre modo EDIÇÃO passando o objeto
            } else {
                alert("Erro ao buscar dados do negócio.");
            }
        } catch (error) {
            console.error(error);
            alert("Erro de conexão.");
        }
    };

        window.deleteNegocio = async (negocioId) => {
        // 1. Busca o elemento do card pelo ID
        const cardElement = document.querySelector(`.kanban-card[data-negocio-id="${negocioId}"]`);
        
        // 2. Extrai o texto do título (h4)
        const negocioTitulo = cardElement ? cardElement.querySelector('h4').textContent : 'este negócio';

        if (!confirm(`Tem certeza que deseja excluir o negócio "${negocioTitulo}"?`)) return;

        try {
            const response = await fetch(`http://localhost:8080/api/negocios/${negocioId}`, {
                method: 'DELETE',
                headers: headers
            });

            if (response.ok || response.status === 204) {
                const funilId = document.getElementById('funnel-trigger-text').dataset.selectedFunilId;
                renderKanban(funilId);
            } else {
                alert("Erro ao excluir negócio.");
            }
        } catch (error) {
            console.error(error);
            alert("Erro de conexão.");
        }
    };
    // --- 8. INICIALIZAÇÃO DA PÁGINA ---
    fetchCurrentUser(); // Busca o usuário atual para o ícone
    fetchAndPopulateFunnels(); // Busca os funis e renderiza o primeiro Kanban

}); // Fim do DOMContentLoaded
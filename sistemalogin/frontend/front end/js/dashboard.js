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
    let funis = []; // Armazenará a lista de funis do usuário
    let pessoas = []; // Armazenará a lista de contatos do usuário

    // --- 2. SELETORES DO DOM (ELEMENTOS DA PÁGINA) ---
    const kanbanBoard = document.getElementById('kanban-board');
    const funnelTriggerText = document.getElementById('funnel-trigger-text');
    const funnelOptionsList = document.getElementById('funnel-options-list');
    const modalForm = document.getElementById('modal-form');
    const modalPessoaSelect = document.getElementById('modal-pessoa');
    const openModalButton = document.querySelector('.btn-new-deal');
    const userProfileIcon = document.getElementById('user-profile-icon');

    // --- 3. FUNÇÕES DE RENDERIZAÇÃO E API ---

    // Função para buscar dados do usuário e atualizar o ícone do perfil
    const fetchCurrentUser = async () => {
        try {
            const response = await fetch('http://localhost:8080/api/users/me', { headers });
            if (response.ok) {
                const user = await response.json();
                if (user.nome) {
                    userProfileIcon.textContent = user.nome.charAt(0).toUpperCase();
                }
            }
        } catch (error) {
            console.error('Erro ao buscar usuário atual:', error);
        }
    };
    
    // Função principal que desenha o quadro Kanban
    const renderKanban = async (funilId) => {
        if (!funilId) {
            kanbanBoard.innerHTML = '<h2>Selecione um funil para começar.</h2>';
            return;
        }

        try {
            const etapasResponse = await fetch(`http://localhost:8080/api/etapas?funilId=${funilId}`, { headers });
            const etapas = await etapasResponse.json();

            const negociosResponse = await fetch('http://localhost:8080/api/negocios', { headers });
            const negocios = await negociosResponse.json();
            
            kanbanBoard.innerHTML = ''; 

            if (etapas.length > 0) {
                etapas.forEach(etapa => {
                    const column = document.createElement('div');
                    column.className = 'kanban-column';
                    column.dataset.etapaId = etapa.id;
                    column.innerHTML = `<h2 class="column-title">${etapa.nome}</h2><div class="cards-container"></div>`;
                    kanbanBoard.appendChild(column);

                    const negociosNestaEtapa = negocios.filter(neg => neg.etapaId === etapa.id && neg.funilId == funilId);

                    negociosNestaEtapa.forEach(negocio => {
                        const card = document.createElement('div');
                        card.className = 'kanban-card';
                        card.innerHTML = `
                            <h4>${negocio.titulo}</h4>
                            <p>${negocio.pessoaNome}</p>
                            <span>${new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(negocio.valor)}</span>
                        `;
                        column.querySelector('.cards-container').appendChild(card);
                    });
                });
            } else {
                kanbanBoard.innerHTML = '<h2>Este funil não possui etapas. <a href="funil.html">Adicione etapas</a> para começar.</h2>';
            }
        } catch (error) {
            console.error("Erro ao renderizar o Kanban:", error);
        }
    };

    // Função para buscar e popular o seletor de funis
    const fetchAndPopulateFunnels = async () => {
        try {
            const response = await fetch('http://localhost:8080/api/funis', { headers });
            funis = await response.json();

            const newFunnelOption = funnelOptionsList.querySelector('.new-funnel-option');
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
                        funnelTriggerText.textContent = funil.nome;
                        funnelTriggerText.dataset.selectedFunilId = funil.id;
                        renderKanban(funil.id);
                    });
                    funnelOptionsList.insertBefore(li, newFunnelOption);
                });
            } else {
                funnelTriggerText.textContent = "Crie um funil";
                kanbanBoard.innerHTML = '<h2>Você ainda não tem funis. Crie um para começar.</h2>';
            }
        } catch (error) {
            console.error("Erro ao buscar funis:", error);
        }
    };

    // Função para buscar e popular o seletor de pessoas no modal
    const fetchAndPopulatePessoas = async () => {
        try {
            const response = await fetch('http://localhost:8080/api/pessoas', { headers });
            pessoas = await response.json();
            modalPessoaSelect.innerHTML = '<option value="">Selecione um contato...</option>'; 
            pessoas.forEach(pessoa => {
                const option = document.createElement('option');
                option.value = pessoa.id;
                option.textContent = pessoa.nome;
                modalPessoaSelect.appendChild(option);
            });
        } catch (error) {
            console.error("Erro ao buscar pessoas:", error);
        }
    };
    
    // --- 4. EVENT LISTENERS ---

    openModalButton.addEventListener('click', () => {
        fetchAndPopulatePessoas(); 
    });

    modalForm.addEventListener('submit', async (event) => {
        event.preventDefault();

        const selectedFunilId = funnelTriggerText.dataset.selectedFunilId;
        
        const etapasResponse = await fetch(`http://localhost:8080/api/etapas?funilId=${selectedFunilId}`, { headers });
        const etapas = await etapasResponse.json();
        if (etapas.length === 0) {
            alert("Este funil não tem etapas. Adicione uma antes de criar um negócio.");
            return;
        }

        const negocioData = {
            titulo: document.getElementById('modal-titulo').value,
            valor: parseFloat(document.getElementById('modal-valor').value),
            organizacao: document.getElementById('modal-organizacao').value,
            pessoaId: parseInt(modalPessoaSelect.value),
            funilId: parseInt(selectedFunilId),
            etapaId: etapas[0].id 
        };

        try {
            const response = await fetch('http://localhost:8080/api/negocios', {
                method: 'POST',
                headers: headers,
                body: JSON.stringify(negocioData)
            });

            if (response.ok) {
                alert('Negócio criado com sucesso!');
                modalForm.reset();
                document.querySelector('.modal-overlay').click(); 
                renderKanban(selectedFunilId); 
            } else {
                alert('Erro ao criar negócio.');
            }
        } catch (error) {
            console.error("Erro ao submeter negócio:", error);
        }
    });

    // --- 5. INICIALIZAÇÃO DA PÁGINA ---
    fetchCurrentUser();
    fetchAndPopulateFunnels();
});
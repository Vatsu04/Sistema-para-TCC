// js/config.js

// js/config.js

document.addEventListener('DOMContentLoaded', () => {
    // ... (todo o código existente: Auth Guard, seletores, modais, fetch users, etc.) ...

    // --- NOVO: SELETOR PARA O FORMULÁRIO DE PREFERÊNCIAS ---
    const userPrefsForm = document.getElementById('user-prefs-form');
    if (!userPrefsForm) {
        console.error("ERRO: Formulário de preferências 'user-prefs-form' não encontrado!");
        return; // Impede erros se o elemento não existir
    }

    // --- NOVO: EVENT LISTENER PARA SALVAR PREFERÊNCIAS ---
    userPrefsForm.addEventListener('submit', async (event) => {
        event.preventDefault(); // Impede o recarregamento da página

        const newName = document.getElementById('name').value.trim();

        if (!newName) {
            alert('O nome não pode ficar vazio.');
            return;
        }

        const updateData = {
            nome: newName
        };

        console.log("Enviando atualização de perfil:", updateData);

        try {
            const response = await fetch('http://localhost:8080/api/users/me', {
                method: 'PUT', // Método HTTP para atualização
                headers: headers, // Cabeçalhos com token e Content-Type
                body: JSON.stringify(updateData) // Envia apenas o nome a ser atualizado
            });

            console.log("Resposta PUT /me:", response.status, response.statusText);

            if (response.ok) {
                const updatedUser = await response.json();
                alert('Alterações salvas com sucesso!');
                // Opcional: Atualiza o nome no campo e no ícone do header, caso tenha mudado
                document.getElementById('name').value = updatedUser.nome;
                 if (updatedUser.nome) {
                    profileIcon.textContent = updatedUser.nome.charAt(0).toUpperCase();
                }
            } else {
                // Tenta mostrar erro da API
                let apiErrorMessage = 'Erro ao salvar alterações.';
                try {
                    const errorData = await response.json();
                    apiErrorMessage = errorData.messages ? errorData.messages.join('\n') : (errorData.message || apiErrorMessage);
                } catch (e) {
                    apiErrorMessage = `Erro ${response.status}: ${response.statusText}`;
                }
                console.error("Erro da API ao atualizar perfil:", apiErrorMessage);
                alert(apiErrorMessage);
            }
        } catch (error) {
            console.error('Erro de rede ao atualizar perfil:', error);
            alert('Ocorreu um erro de rede ao tentar salvar as alterações.');
        }
    });

    // A chamada inicial para fetchCurrentUserData() já existe e preencherá os campos
    // fetchCurrentUserData(); // Garanta que esta linha está no final do script
});

// Flag para garantir que o script só execute uma vez
if (!window.configScriptLoaded) {
    window.configScriptLoaded = true;

    document.addEventListener('DOMContentLoaded', () => {
        console.log("DOM Carregado - Iniciando config.js");

        // --- 1. GUARDA DE AUTENTICAÇÃO E CABEÇALHOS ---
        const token = localStorage.getItem('jwt_token');
        if (!token) {
            console.error("Token não encontrado, redirecionando para login.");
            window.location.href = 'index.html';
            return;
        }
        const headers = {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json'
        };
        console.log("Token encontrado.");

        // --- 2. SELETORES DE ELEMENTOS ---
        const addUserModal = document.getElementById('add-user-modal');
        const addUserBtn = document.getElementById('add-user-btn');
        const addUserForm = document.getElementById('add-user-form');
        const closeModalBtns = document.querySelectorAll('.modal-close-btn, [data-close-modal]');
        const userListElement = document.querySelector('.user-list');
        const manageUsersSection = document.getElementById('gerenciar-usuarios');
        const nameInput = document.getElementById('name');
        const emailInput = document.getElementById('email');
        const profileIcon = document.getElementById('user-profile-icon');


        // Verifica se todos os elementos essenciais foram encontrados
        if (!addUserModal || !addUserBtn || !addUserForm || !userListElement || !manageUsersSection || !nameInput || !emailInput || !profileIcon) {
            console.error("ERRO CRÍTICO: Elementos essenciais do DOM não foram encontrados!");
            return;
        }
        console.log("Seletores do DOM carregados.");

        // --- 3. FUNÇÕES PARA CONTROLAR O MODAL ---
        const openModal = () => {
            console.log("Abrindo modal add-user.");
            addUserModal.classList.remove('hidden');
        }
        const closeModal = () => {
            console.log("Fechando modal add-user.");
            addUserModal.classList.add('hidden');
            addUserForm.reset(); // Limpa o formulário ao fechar
        }

        // --- 4. LÓGICA DE API E RENDERIZAÇÃO ---

        // Função para buscar e renderizar a lista de usuários
        const fetchAndRenderUsers = async () => {
            try {
                console.log("Buscando lista completa de usuários (/all)...");
                const response = await fetch('http://localhost:8080/api/users/all', { headers });
                console.log("Resposta /all:", response.status);

                if (!response.ok) {
                    console.warn('Usuário não tem permissão para ver a lista de usuários ou ocorreu um erro.');
                    manageUsersSection.innerHTML = '<h2>Acesso negado ou erro ao buscar usuários.</h2>';
                    return;
                }
                const users = await response.json();
                console.log("Usuários recebidos:", users);

                userListElement.innerHTML = ''; // Limpa a lista atual
                users.forEach(user => {
                    const li = document.createElement('li');
                    li.className = 'user-item';
                    li.innerHTML = `
                        <span class="user-name">${user.nome}</span>
                        <span class="user-email">${user.email}</span>
                        <span class="user-role">${user.perfil}</span>
                        <button class="delete-user-btn" data-user-id="${user.id}"><i class="fa-solid fa-xmark"></i></button>
                    `;
                    userListElement.appendChild(li);
                });
                console.log("Lista de usuários renderizada.");
            } catch (error) {
                console.error('Erro ao buscar ou renderizar lista de usuários:', error);
                userListElement.innerHTML = '<p>Erro ao carregar a lista de usuários.</p>';
            }
        };

        // Função para buscar os dados do usuário logado
        const fetchCurrentUserData = async () => {
            try {
                console.log("Buscando dados do usuário atual (/me)...");
                const response = await fetch('http://localhost:8080/api/users/me', { headers });
                console.log("Resposta /me:", response.status);

                if (response.ok) {
                    const user = await response.json();
                    console.log("Dados do usuário atual:", user);

                    // Preenche os campos do formulário de preferências
                    nameInput.value = user.nome;
                    emailInput.value = user.email;
                    if (user.nome) {
                        profileIcon.textContent = user.nome.charAt(0).toUpperCase();
                    }

                    // --- LÓGICA DE PERMISSÃO PARA EDIÇÃO ---
                    if (user.perfil === 'ADMINISTRADOR') {
                        console.log("Usuário é ADMINISTRADOR. Habilitando edição de perfil.");
                        // Permite editar o nome
                        nameInput.readOnly = false;
                        // Mostra o botão "Salvar Alterações"
                        document.getElementById('save-prefs-actions').style.display = 'block';
                        // Mostra o botão de editar senha (se houver lógica futura)
                        document.getElementById('edit-password-btn').style.display = 'inline-block'; // Ou 'block'

                        // Busca a lista de usuários (como já fazia)
                        console.log("Buscando lista completa de usuários...");
                        fetchAndRenderUsers();
                    } else { // Se for ASSISTENTE
                        console.log("Usuário NÃO é ADMINISTRADOR. Desabilitando edição de perfil.");
                        // Garante que o nome NÃO seja editável
                        nameInput.readOnly = true;
                        // Garante que o botão "Salvar" NÃO apareça
                        document.getElementById('save-prefs-actions').style.display = 'none';
                        // Esconde o botão de editar senha
                        document.getElementById('edit-password-btn').style.display = 'none';

                        // Esconde a seção de gerenciar usuários (como já fazia)
                        console.log("Escondendo seção de Gerenciar Usuários.");
                        const manageUsersTabLink = document.querySelector('a[data-target="gerenciar-usuarios"]');
                        if(manageUsersTabLink) manageUsersTabLink.parentElement.style.display = 'none';
                        manageUsersSection.innerHTML = '<h2>Acesso negado. Apenas administradores podem gerenciar usuários.</h2>';
                    }
                    // --- FIM DA LÓGICA DE PERMISSÃO ---

                } else { // Falha ao buscar /me
                    console.error('Falha ao buscar dados do usuário atual. Status:', response.status);
                    localStorage.removeItem('jwt_token');
                    window.location.href = 'index.html';
                }
            } catch (error) {
                console.error('Erro de rede ao buscar dados do usuário atual:', error);
                alert('Não foi possível carregar os dados do usuário. Verifique sua conexão e se o servidor está online.');
            }
        };

        // --- 5. EVENT LISTENERS ---

        console.log("Adicionando event listeners...");
        addUserBtn.addEventListener('click', openModal);
        closeModalBtns.forEach(btn => btn.addEventListener('click', closeModal));
        addUserModal.addEventListener('click', (event) => {
            if (event.target === addUserModal) closeModal();
        });

        // Verifica se o listener já foi adicionado para evitar duplicação
        if (!addUserForm.dataset.listenerAttached) {
            addUserForm.addEventListener('submit', async (event) => {
                event.preventDefault(); // Impede o recarregamento da página
                console.log("Evento SUBMIT do formulário 'Adicionar Usuário' acionado!");

                // --- LEITURA E VALIDAÇÃO ---
                const nameInputElem = document.getElementById('new-user-name');
                const emailInputElem = document.getElementById('new-user-email');
                const passwordInputElem = document.getElementById('new-user-password');
                const passwordConfirmInputElem = document.getElementById('new-user-password-confirm');
                const roleSelectElem = document.getElementById('new-user-role');
                const statusSelectElem = document.getElementById('new-user-status');


                const name = nameInputElem.value.trim();
                const email = emailInputElem.value.trim();
                const password = passwordInputElem.value;
                const passwordConfirm = passwordConfirmInputElem.value;
                const role = roleSelectElem.value;
                const isActive = statusSelectElem.value === 'true';
        

                console.log("--- Iniciando Validação Explícita ---");
                console.log("Nome:", name, `(comprimento ${name.length})`);
                console.log("Email:", email, `(comprimento ${email.length})`);
                console.log("Senha:", password ? `(comprimento ${password.length})` : '(vazio)');
                console.log("Perfil:", role);
                console.log("Status Selecionado (string):", isActive ? 'true' : 'false');
                console.log("Ativo (boolean):", isActive);

                let isValid = true;
                const missingFields = [];
                if (name.length === 0) { isValid = false; missingFields.push('Nome'); console.error("FALHA CHECK: Nome"); }
                if (email.length === 0) { isValid = false; missingFields.push('E-mail'); console.error("FALHA CHECK: E-mail"); }
                if (password === '') { isValid = false; missingFields.push('Senha'); console.error("FALHA CHECK: Senha"); }
                if (role === '') { isValid = false; missingFields.push('Perfil'); console.error("FALHA CHECK: Perfil"); }
                if (isActive === '') { isValid = false; missingFields.push('Status'); console.error("FALHA CHECK: Status"); }

                if (!isValid) {
                    alert('Por favor, preencha todos os campos obrigatórios: ' + missingFields.join(', ') + '.');
                    return;
                }

                if (password !== passwordConfirm) {
                    alert('As senhas não coincidem!');
                    return;
                }


                // --- MONTAGEM E FETCH ---
                const newUser = {
                    nome: name,
                    email: email,
                    senha: password,
                    perfil: role,
                    ativo: isActive
                };


                try {

                    const response = await fetch('http://localhost:8080/api/users/admin/create', {
                        method: 'POST',
                        headers: headers,
                        body: JSON.stringify(newUser)
                    });
                    console.log("Resposta recebida:", response.status, response.statusText);

                    if (response.ok) {
                        alert('Usuário adicionado com sucesso!');
                        closeModal();
                        fetchAndRenderUsers(); // Atualiza a lista
                    } else {
                        let apiErrorMessage = 'Erro ao adicionar usuário.';
                        try {
                            const errorData = await response.json();
                            apiErrorMessage = errorData.messages ? errorData.messages.join('\n') : (errorData.message || apiErrorMessage);
                        } catch (e) {
                            apiErrorMessage = `Erro ${response.status}: ${response.statusText}`;
                        }
                        console.error("Erro da API:", apiErrorMessage);
                        alert(apiErrorMessage);
                    }
                } catch (error) {
                    console.error('Erro de rede ou ao processar requisição:', error);
                    alert('Ocorreu um erro de rede. Verifique se o servidor está online.');
                }
            });
            addUserForm.dataset.listenerAttached = 'true'; // Marca que o listener foi adicionado

        } else {

        }

        // --- 6. INICIALIZAÇÃO DA PÁGINA ---

        fetchCurrentUserData();
    });

} else {

}


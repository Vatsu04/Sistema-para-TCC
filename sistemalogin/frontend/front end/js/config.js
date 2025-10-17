// js/config.js

document.addEventListener('DOMContentLoaded', () => {
    // --- 1. GUARDA DE AUTENTICAÇÃO E CABEÇALHOS ---
    const token = localStorage.getItem('jwt_token');
    if (!token) {
        window.location.href = 'index.html';
        return;
    }
    const headers = {
        'Authorization': `Bearer ${token}`,
        'Content-Type': 'application/json'
    };

    // --- 2. SELETORES DE ELEMENTOS DO MODAL E DA LISTA ---
    const addUserModal = document.getElementById('add-user-modal');
    const addUserBtn = document.getElementById('add-user-btn');
    const addUserForm = document.getElementById('add-user-form');
    const closeModalBtns = document.querySelectorAll('.modal-close-btn, [data-close-modal]');
    const userListElement = document.querySelector('.user-list');

    // --- 3. FUNÇÕES PARA CONTROLAR O MODAL ---
    const openModal = () => addUserModal.classList.remove('hidden');
    const closeModal = () => addUserModal.classList.add('hidden');

    // --- 4. LÓGICA DE API E RENDERIZAÇÃO ---

    // Função para buscar e renderizar a lista de usuários
    const fetchAndRenderUsers = async () => {
        try {
            const response = await fetch('http://localhost:8080/api/users/all', { headers });
            if (!response.ok) {
                // Se o usuário não for admin, o back-end retornará 403 Forbidden
                console.warn('Usuário não tem permissão para ver a lista de usuários.');
                document.getElementById('gerenciar-usuarios').innerHTML = '<h2>Acesso negado. Apenas administradores podem gerenciar usuários.</h2>';
                return;
            }
            const users = await response.json();
            
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
        } catch (error) {
            console.error('Erro ao buscar lista de usuários:', error);
        }
    };

    // Função para buscar os dados do usuário logado
    const fetchCurrentUserData = async () => {
        try {
            const response = await fetch('http://localhost:8080/api/users/me', { headers });
            if (response.ok) {
                const user = await response.json();
                document.getElementById('name').value = user.nome;
                document.getElementById('email').value = user.email;
                const profileIcon = document.getElementById('user-profile-icon');
                if (user.nome) {
                    profileIcon.textContent = user.nome.charAt(0).toUpperCase();
                }

                // Se o usuário for um administrador, busca a lista de usuários
                if (user.perfil === 'ADMINISTRADOR') {
                    fetchAndRenderUsers();
                } else {
                    // Esconde a aba de gerenciar usuários se não for admin
                    const manageUsersTab = document.querySelector('a[data-target="gerenciar-usuarios"]').parentElement;
                    manageUsersTab.style.display = 'none';
                }
            } else {
                localStorage.removeItem('jwt_token');
                window.location.href = 'index.html';
            }
        } catch (error) {
            console.error('Erro de rede ou de servidor:', error);
        }
    };

    // --- 5. EVENT LISTENERS ---

    // Abre o modal ao clicar no botão "+ Usuário"
    addUserBtn.addEventListener('click', openModal);

    // Fecha o modal ao clicar nos botões de fechar/cancelar
    closeModalBtns.forEach(btn => btn.addEventListener('click', closeModal));

    // Fecha o modal ao clicar no overlay
    addUserModal.addEventListener('click', (event) => {
        if (event.target === addUserModal) {
            closeModal();
        }
    });

    // Lida com a submissão do formulário de novo usuário
    addUserForm.addEventListener('submit', async (event) => {
        event.preventDefault();

        const name = document.getElementById('new-user-name').value;
        const email = document.getElementById('new-user-email').value;
        const password = document.getElementById('new-user-password').value;
        const passwordConfirm = document.getElementById('new-user-password-confirm').value;

        // Validação simples no front-end
        if (password !== passwordConfirm) {
            alert('As senhas não coincidem!');
            return;
        }

        const newUser = {
            nome: name,
            email: email,
            senha: password
        };

        try {
            // Usa o endpoint público de registro, que já tem a validação de senha forte
            const response = await fetch('http://localhost:8080/api/auth/register', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' }, // Não precisa de token de admin aqui
                body: JSON.stringify(newUser)
            });

            if (response.ok) {
                alert('Usuário adicionado com sucesso!');
                closeModal();
                addUserForm.reset();
                fetchAndRenderUsers(); // Atualiza a lista de usuários na tela
            } else {
                // Tenta extrair a mensagem de erro da API
                const errorData = await response.json();
                const errorMessage = errorData.messages ? errorData.messages.join('\n') : 'Erro ao adicionar usuário.';
                alert(errorMessage);
            }
        } catch (error) {
            console.error('Erro ao criar usuário:', error);
            alert('Ocorreu um erro de rede.');
        }
    });

    // --- 6. INICIALIZAÇÃO DA PÁGINA ---
    fetchCurrentUserData();
});
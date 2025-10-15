// js/config.js
document.addEventListener('DOMContentLoaded', () => {
    // --- 1. GUARDA DE AUTENTICAÇÃO ---
    const token = localStorage.getItem('jwt_token');
    if (!token) {
        window.location.href = 'index.html';
        return;
    }
    const headers = { 'Authorization': `Bearer ${token}` };

    // --- 2. BUSCAR DADOS DO USUÁRIO LOGADO ---
    const fetchCurrentUser = async () => {
        try {
            // NOTA: Você precisará criar este endpoint no back-end: GET /api/users/me
            const response = await fetch('http://localhost:8080/api/users/me', { headers });
            const user = await response.json();

            document.getElementById('name').value = user.nome;
            document.getElementById('email').value = user.email;

            // Se o usuário for admin, busca a lista de usuários
            if (user.perfil === 'ADMINISTRADOR') {
                fetchUserList();
            }

        } catch (error) {
            console.error('Erro ao buscar dados do usuário:', error);
        }
    };

    // --- 3. BUSCAR LISTA DE USUÁRIOS (PARA ADMINS) ---
    const fetchUserList = async () => {
         try {
            // NOTA: Você precisará criar este endpoint no back-end: GET /api/users
            const response = await fetch('http://localhost:8080/api/users', { headers });
            const users = await response.json();

            const userList = document.querySelector('.user-list');
            userList.innerHTML = ''; // Limpa a lista
            users.forEach(user => {
                const li = document.createElement('li');
                li.classList.add('user-item');
                li.innerHTML = `
                    <span class="user-name">${user.nome}</span>
                    <span class="user-email">${user.email}</span>
                    <span class="user-role">${user.perfil}</span>
                    <button class="delete-user-btn" data-user-id="${user.id}"><i class="fa-solid fa-xmark"></i></button>
                `;
                userList.appendChild(li);
            });
         } catch (error) {
            console.error('Erro ao buscar lista de usuários:', error);
         }
    };

    fetchCurrentUser();
});
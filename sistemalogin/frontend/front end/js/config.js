// js/config.js

document.addEventListener('DOMContentLoaded', () => {
    // --- 1. GUARDA DE AUTENTICAÇÃO (Auth Guard) ---
    // Este bloco de código é a primeira linha de defesa de qualquer página protegida.
    const token = localStorage.getItem('jwt_token');

    // Se não houver token, o usuário não está logado. Redireciona imediatamente para a página de login.
    if (!token) {
        window.location.href = 'index.html';
        return; // Para a execução do script para evitar erros
    }

    // --- 2. FUNÇÃO PARA BUSCAR OS DADOS DO USUÁRIO ---
    // Esta função assíncrona fará a chamada à sua API.
    const fetchCurrentUserData = async () => {
        try {
            // Faz a requisição para o endpoint /me, enviando o token no cabeçalho Authorization.
            const response = await fetch('http://localhost:8080/api/users/me', {
                method: 'GET',
                headers: {
                    'Authorization': `Bearer ${token}`
                }
            });

            // Se a resposta for bem-sucedida...
            if (response.ok) {
                const user = await response.json(); // Converte a resposta JSON em um objeto JavaScript

                // --- 3. ATUALIZAR A PÁGINA (O DOM) ---
                // Pega os elementos do formulário e preenche com os dados do usuário.
                document.getElementById('name').value = user.nome;
                document.getElementById('email').value = user.email;
                
                // Atualiza também o ícone do perfil no header
                const profileIcon = document.getElementById('user-profile-icon');
                if (user.nome) {
                    profileIcon.textContent = user.nome.charAt(0).toUpperCase(); // Pega a primeira letra do nome
                }

            } else {
                // Se o token for inválido ou expirado, o back-end retornará um erro.
                console.error('Falha ao buscar dados do usuário:', response.statusText);
                // Como medida de segurança, limpa o token inválido e redireciona para o login.
                localStorage.removeItem('jwt_token');
                window.location.href = 'index.html';
            }
        } catch (error) {
            console.error('Erro de rede ou de servidor:', error);
            alert('Não foi possível carregar os dados do usuário. Verifique sua conexão.');
        }
    };


    fetchCurrentUserData();
});
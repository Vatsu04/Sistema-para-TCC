
document.addEventListener('DOMContentLoaded', () => {
    const loginForm = document.getElementById('login-form');

    loginForm.addEventListener('submit', async (event) => {
        // 1. Impede o comportamento padrão do formulário (que seria recarregar a página)
        event.preventDefault();

        // 2. Pega os valores dos campos de e-mail e senha
        const email = document.getElementById('email').value;
        const password = document.getElementById('password').value;

        // 3. Monta o corpo da requisição em formato JSON
        const loginData = {
            email: email,
            senha: password // Certifique-se que o nome 'senha' corresponde ao seu LoginDTO.java
        };

        try {
            // 4. Envia a requisição POST para a sua API usando fetch
            const response = await fetch('http://localhost:8080/api/auth/login', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(loginData)
            });

            // 5. Verifica se a resposta foi bem-sucedida (status 200-299)
            if (response.ok) {
                const data = await response.json();

                // 6. Armazena o token JWT no navegador (localStorage)
                // Este token será usado para autenticar todas as futuras requisições
                localStorage.setItem('jwt_token', data.token);

                // 7. Redireciona o usuário para o dashboard
                window.location.href = 'dashboard.html';

            } else {
                let errorMessage = 'E-mail ou senha inválidos. Tente novamente.'; // Mensagem padrão

                // A alteração no backend (UserDetailsServiceImpl) faz o 
                // Spring Security retornar HTTP 403 (Forbidden) para usuários inativos.
                if (response.status === 403) {
                    errorMessage = 'Este usuário está inativo. Contate um administrador.';
                }
                
                // Se o status for 401 (Unauthorized) ou outro,
                // a mensagem padrão de "E-mail ou senha inválidos" será usada.

                alert(errorMessage);
            }
        } catch (error) {
            console.error('Erro ao tentar fazer login:', error);
            alert('Não foi possível conectar ao servidor. Verifique sua conexão.');
        }
    });
});
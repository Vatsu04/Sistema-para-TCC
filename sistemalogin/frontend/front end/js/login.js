document.addEventListener('DOMContentLoaded', () => {

    // --- Seletores (Tudo em um só lugar) ---
    const loginContainer = document.querySelector('.login-container');
    const forgotPassContainer = document.querySelector('.forgot-password-container');
    const showForgotPassLink = document.querySelector('.forgot-password');
    const forgotPassForm = document.getElementById('fpassword-form');
    const backToLoginBtn = document.querySelector('.btn-back');
    const loginForm = document.getElementById('login-form');


    const urlParams = new URLSearchParams(window.location.search);
    const oauthError = urlParams.get('error');

    if (oauthError) {
        if (oauthError === 'oauth_user_not_found') {
            alert('Falha no login com Google: Este e-mail não está cadastrado no sistema.');
        } else if (oauthError === 'oauth_user_inactive') {
            alert('Falha no login com Google: Este usuário está inativo.');
        }
        
        // Limpa a URL para que o alerta não apareça de novo se o usuário recarregar
        window.history.replaceState({}, document.title, window.location.pathname);
    }

    // --- Lógica para mostrar o formulário "Esqueci Senha" ---
    if (showForgotPassLink) {
        showForgotPassLink.addEventListener('click', (e) => {
            e.preventDefault(); // Impede o link de navegar
            
            if (loginContainer) loginContainer.style.display = 'none';
            if (forgotPassContainer) forgotPassContainer.style.display = 'flex'; // <-- MUDANÇA AQUI
        });
    }

    // --- Lógica para o botão "Voltar" ---
    if (backToLoginBtn) {
        backToLoginBtn.addEventListener('click', () => {
            if (loginContainer) loginContainer.style.display = 'flex'; // <-- MUDANÇA AQUI
            if (forgotPassContainer) forgotPassContainer.style.display = 'none';
        });
    }

    // --- Lógica de Envio do "Esqueci Senha" ---
    if (forgotPassForm) {
        forgotPassForm.addEventListener('submit', async (e) => {
            e.preventDefault();
            
            const emailInput = document.getElementById('fpass-email');
            const email = emailInput.value;

            if (!email) {
                alert('Por favor, insira seu e-mail.');
                return;
            }

            try {
                const response = await fetch('http://localhost:8080/api/auth/forgot-password', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify({ email: email })
                });

                // Resposta genérica por segurança
                alert('Solicitação recebida! Se este e-mail estiver cadastrado, você receberá um link para redefinir sua senha.');
                
                // Volta para a tela de login
                if (loginContainer) loginContainer.style.display = 'flex'; // <-- MUDANÇA AQUI
                if (forgotPassContainer) forgotPassContainer.style.display = 'none';

            } catch (error) {
                console.error('Erro ao solicitar redefinição de senha:', error);
                alert('Erro ao conectar com o servidor. Tente novamente.');
            }
        });
    }

    // --- Lógica de Envio do Login (vinda do segundo DOMContentLoaded) ---
    if (loginForm) {
        loginForm.addEventListener('submit', async (event) => {
            event.preventDefault();

            const email = document.getElementById('email').value;
            const password = document.getElementById('password').value;

            const loginData = {
                email: email,
                senha: password 
            };

            try {
                const response = await fetch('http://localhost:8080/api/auth/login', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(loginData)
                });

                if (response.ok) {
                    const data = await response.json();
                    localStorage.setItem('jwt_token', data.token);
                    window.location.href = 'dashboard.html';

                } else {
                    let errorMessage = 'E-mail ou senha inválidos. Tente novamente.'; 
                    if (response.status === 403) {
                        errorMessage = 'Este usuário está inativo. Contate um administrador.';
                    }
                    alert(errorMessage);
                }
            } catch (error) {
                console.error('Erro ao tentar fazer login:', error);
                alert('Não foi possível conectar ao servidor. Verifique sua conexão.');
            }
        });
    }
});
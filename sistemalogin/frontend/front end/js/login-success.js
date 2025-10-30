document.addEventListener('DOMContentLoaded', () => {
    const urlParams = new URLSearchParams(window.location.search);
    const token = urlParams.get('token');

    if (token) {
        // Salva o token JWT no localStorage
        localStorage.setItem('jwt_token', token);
        // Redireciona para o dashboard
        window.location.href = 'dashboard.html';
    } else {
        // Se falhou, volta para o login
        alert('Falha na autenticação.');
        window.location.href = 'index.html';
    }
});
document.addEventListener('DOMContentLoaded', () => {

    function getCookie(name) {
        const value = `; ${document.cookie}`;
        const parts = value.split(`; ${name}=`);
        if (parts.length === 2) return parts.pop().split(';').shift();
        return null;
    }

    const token = getCookie('AUTH_TOKEN');

    const urlParams = new URLSearchParams(window.location.search);
    const error = urlParams.get('error');

    if (token) {
        localStorage.setItem('jwt_token', token);

        document.cookie = "AUTH_TOKEN=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;";

        // Redireciona para o dashboard
        window.location.href = 'dashboard.html';

    } else if (error) {
        let mensagem = 'Falha no login.';
        if(error === 'oauth_user_not_found') mensagem = 'Usuário não encontrado no sistema.';
        if(error === 'oauth_user_inactive') mensagem = 'Usuário inativo.';

        alert(mensagem);
        window.location.href = 'index.html';

    } else {
        window.location.href = 'index.html';
    }
});
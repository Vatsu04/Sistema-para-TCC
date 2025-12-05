document.addEventListener('DOMContentLoaded', () => {
    // 1. Tenta pegar parâmetros da URL (Token ou Erro)
    const urlParams = new URLSearchParams(window.location.search);
    const token = urlParams.get('token');
    const error = urlParams.get('error');

    if (token) {
        // SUCESSO: O Java mandou o token na URL
        localStorage.setItem('jwt_token', token);

        // Limpa a URL para não deixar o token exposto no histórico
        window.history.replaceState({}, document.title, window.location.pathname);

        // Redireciona para o dashboard
        window.location.href = 'dashboard.html';

    } else if (error) {
        // ERRO: O Java mandou um código de erro
        let mensagem = 'Falha no login.';

        if(error === 'oauth_user_not_found') {
            mensagem = 'Este e-mail Google não está cadastrado no sistema.';
        } else if(error === 'oauth_user_inactive') {
            mensagem = 'Este usuário está inativo. Contate o administrador.';
        } else if(error === 'server_error') {
            mensagem = 'Erro interno no servidor. Tente novamente mais tarde.';
        }

        alert(mensagem);
        window.location.href = 'index.html';

    } else {
        // NADA: Acesso direto sem token nem erro
        window.location.href = 'index.html';
    }
});
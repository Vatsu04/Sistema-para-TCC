document.addEventListener('DOMContentLoaded', () => {
    
    const changePasswordForm = document.getElementById('change-password-form');
    const newPasswordInput = document.getElementById('new-password');
    const confirmPasswordInput = document.getElementById('confirm-password');

    const urlParams = new URLSearchParams(window.location.search);
    const token = urlParams.get('token');

    if (!token) {
        handleInvalidToken("Link inválido. Token não encontrado.");
        return;
    }

    const validateToken = async () => {
        try {
  
            const response = await fetch('http://localhost:8080/api/auth/validate-token', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ token: token })
            });

            if (!response.ok) {
                const errorMessage = await response.text();
                handleInvalidToken(errorMessage || "Token inválido ou expirado.");
            }
  
        } catch (error) {
            handleInvalidToken("Erro ao conectar com o servidor para validar o token.");
        }
    };

    function handleInvalidToken(message) {
        alert(message);
 
        if (changePasswordForm) changePasswordForm.innerHTML = `<p style="color: red; text-align: center;">${message}</p>`;
    }

    validateToken();

    if (changePasswordForm) {
        changePasswordForm.addEventListener('submit', async (e) => {
            e.preventDefault();

            const newPassword = newPasswordInput.value;
            const confirmPassword = confirmPasswordInput.value;

            if (!newPassword || !confirmPassword) {
                alert("Por favor, preencha os dois campos.");
                return;
            }
            if (newPassword !== confirmPassword) {
                alert("As senhas não coincidem.");
                return;
            }
            if (newPassword.length < 8) {
                alert("A senha deve ter pelo menos 8 caracteres.");
                return;
            }

            if (!/[a-z]/.test(newPassword)) {
                alert("A senha deve conter pelo menos uma letra minúscula.");
                return;
            }
            
            // Checa por letra maiúscula
            if (!/[A-Z]/.test(newPassword)) {
                alert("A senha deve conter pelo menos uma letra maiúscula.");
                return;
            }

            // Checa por número
            if (!/\d/.test(newPassword)) { // \d é um atalho para [0-9]
                alert("A senha deve conter pelo menos um número.");
                return;
            }

            // Checa por caractere especial (ajuste a lista se necessário)
            const specialChars = /[!@#$%^&*()_+\-=\[\]{};':"\\|,.<>\/?]/;
            if (!specialChars.test(newPassword)) {
                alert("A senha deve conter pelo menos um caractere especial (ex: !@#$%).");
                return;
            }

            try {
                const response = await fetch('http://localhost:8080/api/auth/reset-password', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({
                        token: token,
                        newPassword: newPassword,
                        confirmPassword: confirmPassword
                    })
                });

                const message = await response.text();

                if (response.ok) {
                    alert(message); 
                    window.location.href = 'index.html'; 
                } else {
                    alert(message);
                }
            } catch (error) {
                alert("Erro de rede ao tentar alterar a senha.");
            }
        });
    }
});
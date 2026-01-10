// js/funil.js
document.addEventListener('DOMContentLoaded', () => {
    // --- 1. GUARDA DE AUTENTICAÇÃO ---
    const token = localStorage.getItem('jwt_token');
    if (!token) {
        window.location.href = 'index.html';
        return;
    }
    const headers = { 'Authorization': `Bearer ${token}`, 'Content-Type': 'application/json' };

    // --- 2. PEGAR O ID DO FUNIL DA URL ---
    const urlParams = new URLSearchParams(window.location.search);
    const funilId = urlParams.get('id'); // Ex: funil.html?id=1

    const funnelNameInput = document.getElementById('funnel-name');
    const stagesContainer = document.getElementById('stages-container');

    // --- 3. BUSCAR DADOS DO FUNIL E SUAS ETAPAS ---
    const fetchFunilData = async () => {
        if (!funilId) {
            // Se não houver ID, é um funil novo
            return;
        }
        try {
            const funilResponse = await fetch(`http://localhost:8080/api/funis/${funilId}`, { headers });
            const funil = await funilResponse.json();
            funnelNameInput.value = funil.nome;

            const etapasResponse = await fetch(`http://localhost:8080/api/etapas?funilId=${funilId}`, { headers });
            const etapas = await etapasResponse.json();

            // Renderiza as etapas existentes
            // (Aqui você adicionaria a lógica para limpar as etapas de exemplo e adicionar as reais)
        } catch (error) {
            console.error('Erro ao buscar dados do funil:', error);
        }
    };

    // --- 4. LÓGICA PARA SALVAR ALTERAÇÕES ---
    const saveButton = document.querySelector('.btn-save');
    saveButton.addEventListener('click', async () => {
        // Coleta todos os dados da página
        const funilData = { nome: funnelNameInput.value };
        const etapasData = []; // Você precisaria ler cada card de etapa e pegar nome e posição

        try {
            // Salva o nome do funil
            await fetch(`http://localhost:8080/api/funis/${funilId}`, {
                method: 'PUT',
                headers: headers,
                body: JSON.stringify(funilData)
            });

            // Salva as etapas (requer um loop de requisições POST/PUT)
            alert('Funil salvo com sucesso!');
            window.location.href = 'dashboard.html';

        } catch (error) {
            console.error('Erro ao salvar funil:', error);
        }
    });

    fetchFunilData();
});
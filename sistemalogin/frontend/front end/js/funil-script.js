document.addEventListener('DOMContentLoaded', () => {
    const stagesContainer = document.getElementById('stages-container');
    // O botão agora é o card inteiro para uma área de clique maior
    const addCardButton = document.getElementById('add-stage-card-button');
    const addStageColumn = document.querySelector('.add-stage-column');
    let stageCount = 2; // Começamos com 2 etapas de exemplo

    const addNewStage = () => {
        stageCount++;
        const newColumn = document.createElement('div');
        newColumn.classList.add('stage-column');
        
        const newTitle = `Nova Etapa ${stageCount - 1}`;

        newColumn.innerHTML = `
            <h3 class="stage-title">${newTitle}</h3>
            <div class="stage-card">
                <div class="form-group">
                    <label for="stage-name-${stageCount}">Nome</label>
                    <input type="text" id="stage-name-${stageCount}" value="${newTitle}">
                </div>
                <button class="delete-stage-btn"><i class="fa-solid fa-trash-can"></i> Excluir etapa</button>
            </div>
        `;
        stagesContainer.insertBefore(newColumn, addStageColumn);
    };

    const handleContainerClick = (e) => {
        // Lógica para deletar um card
        const deleteButton = e.target.closest('.delete-stage-btn');
        if (deleteButton) {
            const columnToDelete = deleteButton.closest('.stage-column');
            if (columnToDelete) {
                columnToDelete.remove();
            }
        }
    };

    // Adiciona os event listeners
    addCardButton.addEventListener('click', addNewStage);
    stagesContainer.addEventListener('click', handleContainerClick);
});
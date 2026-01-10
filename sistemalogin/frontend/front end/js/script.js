// Espera todo o HTML da página ser carregado ANTES de executar qualquer código.
// Este é o ÚNICO 'DOMContentLoaded' que você deve ter.
document.addEventListener('DOMContentLoaded', () => {

    // ========================================================================
    // LÓGICA COMUM A TODAS AS PÁGINAS
    // ========================================================================

    // --- Lógica para o Dropdown do Perfil do Usuário ---
    const userProfileIcon = document.getElementById('user-profile-icon');
    const dropdownMenu = document.getElementById('profile-dropdown-menu');

    if (userProfileIcon && dropdownMenu) {
        userProfileIcon.addEventListener('click', (event) => {
            // Impede que o clique no ícone feche o menu imediatamente
            event.stopPropagation(); 
            dropdownMenu.classList.toggle('hidden');
            userProfileIcon.classList.toggle('active');
        });

        // Fecha o dropdown se clicar em qualquer lugar da janela
        window.addEventListener('click', () => {
            if (!dropdownMenu.classList.contains('hidden')) {
                dropdownMenu.classList.add('hidden');
                userProfileIcon.classList.remove('active');
            }
        });
    }

    // ========================================================================
    // LÓGICA ESPECÍFICA DA PÁGINA DE DASHBOARD (negocios.html)
    // ========================================================================

    const kanbanBoard = document.querySelector('.kanban-board');
    if (kanbanBoard) {
        // --- Lógica para o Modal de "Adicionar Negócio" ---
        const openModalButton = document.querySelector('.btn-new-deal');
        const addDealModal = document.querySelector('.modal'); // Assumindo que este é o modal de negócio
        const overlay = document.querySelector('.modal-overlay');

        const openDealModal = () => {
            if (addDealModal && overlay) {
                addDealModal.classList.remove('hidden');
                overlay.classList.remove('hidden');
            }
        };

        const closeDealModal = () => {
            if (addDealModal && overlay) {
                addDealModal.classList.add('hidden');
                overlay.classList.add('hidden');
            }
        };
        
        if (openModalButton) {
            openModalButton.addEventListener('click', openDealModal);
        }

        if (overlay) {
            overlay.addEventListener('click', (event) => (event.target === overlay) && closeDealModal());
        }

        // --- Lógica para o Seletor de Funil Personalizado ---
        const funnelWrapper = document.querySelector('.custom-select-wrapper');
        if (funnelWrapper) {
            const trigger = funnelWrapper.querySelector('.custom-select');
            const options = funnelWrapper.querySelector('.custom-select-options');
            const triggerText = trigger.querySelector('.custom-select-trigger');
            
            trigger.addEventListener('click', (e) => {
                e.stopPropagation();
                options.classList.toggle('hidden');
            });

            options.addEventListener('click', (e) => {
                const item = e.target.closest('li');
                if (!item) return;

                const value = item.getAttribute('data-value');
                if (value === 'novo') {
                    window.location.href = 'funil.html';
                } else {
                    triggerText.textContent = item.textContent;
                    const currentSelected = options.querySelector('li.selected');
                    if(currentSelected) currentSelected.classList.remove('selected');
                    item.classList.add('selected');
                }
                options.classList.add('hidden');
            });
            
            window.addEventListener('click', () => !options.classList.contains('hidden') && options.classList.add('hidden'));
        }
    }


    // ========================================================================
    // LÓGICA ESPECÍFICA DA PÁGINA DE CONFIGURAÇÕES (conf.html)
    // ========================================================================

    const settingsContainer = document.querySelector('.settings-container');
    if (settingsContainer) {
        // --- Lógica para Troca de Abas ---
        const menuLinks = settingsContainer.querySelectorAll('.settings-menu a[data-target]');
        const contentPanes = settingsContainer.querySelectorAll('.settings-content');
        const pageTitle = document.getElementById('page-title');

        menuLinks.forEach(link => {
            link.addEventListener('click', (event) => {
                event.preventDefault();
                const targetId = link.getAttribute('data-target');
                const targetPane = document.getElementById(targetId);

                menuLinks.forEach(item => item.parentElement.classList.remove('active'));
                link.parentElement.classList.add('active');
                
                contentPanes.forEach(pane => pane.classList.add('hidden'));
                if (targetPane) targetPane.classList.remove('hidden');
                
                pageTitle.textContent = `Configurações / ${link.textContent.trim()}`;
            });
        });
        
        // --- Lógica do Modal de Excluir Usuário ---
        const deleteUserButtons = document.querySelectorAll('.delete-user-btn');
        const deleteModal = document.getElementById('delete-user-modal');
        if (deleteModal) {
            const userNameInModal = document.getElementById('user-name-to-delete');
            const confirmDeleteButton = deleteModal.querySelector('.btn-danger');
            const closeDeleteButtons = deleteModal.querySelectorAll('.modal-close-btn, .btn-secondary');

            const closeDeleteModal = () => deleteModal.classList.add('hidden');

            deleteUserButtons.forEach(button => {
                button.addEventListener('click', () => {
                    const userName = button.closest('.user-item').querySelector('.user-name').textContent;
                    userNameInModal.textContent = userName;
                    deleteModal.classList.remove('hidden');
                });
            });

            closeDeleteButtons.forEach(button => button.addEventListener('click', closeDeleteModal));
            deleteModal.addEventListener('click', e => (e.target === deleteModal) && closeDeleteModal());
            confirmDeleteButton.addEventListener('click', () => {
                alert(`Usuário "${userNameInModal.textContent}" foi excluído!`);
                closeDeleteModal();
            });
        }

        // --- Lógica do Modal de Adicionar Usuário ---
        const addUserBtn = document.getElementById('add-user-btn');
        const addUserModal = document.getElementById('add-user-modal');
        if (addUserModal) {
            const addUserForm = document.getElementById('add-user-form');
            const closeAddButtons = addUserModal.querySelectorAll('.modal-close-btn, [data-close-modal]');

            const openAddUserModal = () => addUserModal.classList.remove('hidden');
            const closeAddUserModal = () => {
                addUserModal.classList.add('hidden');
                addUserForm.reset();
            };

            if (addUserBtn) addUserBtn.addEventListener('click', openAddUserModal);
            
            closeAddButtons.forEach(button => button.addEventListener('click', closeAddUserModal));
            addUserModal.addEventListener('click', e => (e.target === addUserModal) && closeAddUserModal());
            addUserForm.addEventListener('submit', event => {
                event.preventDefault();
                const password = document.getElementById('new-user-password').value;
                const passwordConfirm = document.getElementById('new-user-password-confirm').value;

                if (password !== passwordConfirm) {
                    alert('As senhas não coincidem!');
                    return;
                }
                alert('Novo usuário adicionado com sucesso!');
                closeAddUserModal();
            });
        }
    }

});
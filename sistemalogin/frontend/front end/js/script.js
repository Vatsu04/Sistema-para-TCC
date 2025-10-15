// --- Lógica para o Modal de "Adicionar Negócio" ---
const openModalButton = document.querySelector('.btn-new-deal');
const modal = document.querySelector('.modal');
const overlay = document.querySelector('.modal-overlay');

const openModal = function() {
    if (modal && overlay) {
        modal.classList.remove('hidden');
        overlay.classList.remove('hidden');
    }
};

const closeModal = function() {
    if (modal && overlay) {
        modal.classList.add('hidden');
        overlay.classList.add('hidden');
    }
};

if (openModalButton) {
    openModalButton.addEventListener('click', openModal);
}

if (overlay) {
    overlay.addEventListener('click', function(event) {
        if (event.target === overlay) {
            closeModal();
        }
    });
}

// --- Lógica para o Dropdown do Perfil do Usuário ---
const userProfileIcon = document.getElementById('user-profile-icon');
const dropdownMenu = document.getElementById('profile-dropdown-menu');

if (userProfileIcon && dropdownMenu) {
    userProfileIcon.addEventListener('click', (event) => {
        event.stopPropagation();
        dropdownMenu.classList.toggle('hidden');
        userProfileIcon.classList.toggle('active');
    });

    window.addEventListener('click', () => {
        if (!dropdownMenu.classList.contains('hidden')) {
            dropdownMenu.classList.add('hidden');
            userProfileIcon.classList.remove('active');
        }
    });

    dropdownMenu.addEventListener('click', (event) => {
        event.stopPropagation();
    });
}

// --- Lógica para as abas da página de Configurações ---
document.addEventListener('DOMContentLoaded', () => {
    const settingsMenu = document.querySelector('.settings-menu');
    const pageTitle = document.getElementById('page-title');
    
    if (settingsMenu && pageTitle) {
        const menuLinks = settingsMenu.querySelectorAll('a');
        const contentPanes = document.querySelectorAll('.settings-content');

        // --- LÓGICA DE CLIQUE CORRIGIDA ---
        settingsMenu.addEventListener('click', (e) => {
            const clickedLink = e.target.closest('a');
            if (!clickedLink) return;

            const targetId = clickedLink.dataset.target;
            
            // AGORA, SÓ EXECUTA O CÓDIGO DA ABA SE O LINK TIVER 'data-target'
            if (targetId) {
                e.preventDefault(); // Previne a ação do link APENAS para as abas

                // Atualiza o estado ativo no menu
                menuLinks.forEach(link => link.parentElement.classList.remove('active'));
                clickedLink.parentElement.classList.add('active');

                // Mostra o conteúdo correto
                contentPanes.forEach(pane => {
                    if (pane.id === targetId) {
                        pane.classList.remove('hidden');
                    } else {
                        pane.classList.add('hidden');
                    }
                });

                // Atualiza o título da página
                const linkText = clickedLink.innerText.trim();
                pageTitle.textContent = `Configurações / ${linkText}`;
            }
            // Se não tiver 'data-target', o código não faz nada e o link "Sair" funciona normalmente.
        });
        
        const checkUrlForTab = () => {
            const params = new URLSearchParams(window.location.search);
            const tabId = params.get('tab');
            if (tabId) {
                const targetLink = settingsMenu.querySelector(`a[data-target="${tabId}"]`);
                if (targetLink) {
                    targetLink.click();
                }
            }
        };

        checkUrlForTab();
    }
});

// --- Lógica para o Seletor de Funil Personalizado ---
const funnelWrapper = document.querySelector('.custom-select-wrapper');

if (funnelWrapper) {
    const trigger = funnelWrapper.querySelector('.custom-select');
    const options = funnelWrapper.querySelector('.custom-select-options');
    const triggerText = trigger.querySelector('.custom-select-trigger');
    const optionItems = options.querySelectorAll('li');

    // Abre/fecha o seletor
    trigger.addEventListener('click', () => {
        options.classList.toggle('hidden');
    });

    // Lógica para quando uma opção é clicada
    optionItems.forEach(item => {
        item.addEventListener('click', () => {
            const value = item.getAttribute('data-value');
            
            if (value === 'novo') {
                // Ação para "Novo Funil"
                window.location.href = 'funil.html';
                options.classList.add('hidden');
            } else {
                // Ação para as outras opções
                triggerText.textContent = item.textContent;
                
                // Remove a seleção antiga e adiciona a nova
                funnelWrapper.querySelector('li.selected').classList.remove('selected');
                item.classList.add('selected');
                
                // Fecha o menu
                options.classList.add('hidden');
            }
        });
    });

    // Fecha o seletor se clicar fora dele
    window.addEventListener('click', (e) => {
        if (!funnelWrapper.contains(e.target)) {
            options.classList.add('hidden');
        }
    });
}
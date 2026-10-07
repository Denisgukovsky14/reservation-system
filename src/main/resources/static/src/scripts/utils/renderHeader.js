function renderHeader() {
    const user = JSON.parse(localStorage.getItem('user'));
    const headerPlaceholder = document.getElementById('header-placeholder');

    if (!headerPlaceholder) {
        console.error('header-placeholder не найден!');
        return;
    }

    let headerHTML = `
        <header>
            <div class="header-container">
                <a href="/src/pages/home/home.html" class="logo">
                    <div class="logo-icon">
                        <span></span>
                        <span></span>
                        <span></span>
                    </div>
                    DesignHub
                </a>
                <nav>
                    <a href="#about">О проекте</a>
                    <a href="#features">Возможности</a>
                    <a href="javascript:void(0)" id="openPricing">Тарифы</a>
                    <a href="#contacts">Контакты</a>
                </nav>
                <div class="header-actions">
    `;

    if (user) {
        let firstName = user.firstName || user.name || 'Пользователь';
        let lastName = user.lastName || user.surname || '';
        const avatarUrl = user.profileImage || '/src/images/defaultAvatar.webp';
        const userType = user.userType || (user.firstName ? 'contractor' : 'customer');

        headerHTML += `
            <div class="user-menu">
                <a href="/src/pages/profile/user_personal_account_page.html?type=${userType}&id=${user.id}" class="user-profile-link">
                    <img src="${avatarUrl}" alt="avatar" class="user-avatar" onerror="this.src='/src/images/defaultAvatar.webp'">
                    <span class="user-name">${firstName} ${lastName}</span>
                </a>
                <button id="logoutBtn" class="btn btn-dark">Выйти</button>
            </div>
        `;
    } else {
        headerHTML += `
            <a href="/src/pages/auth/authorization.html?tab=register" class="btn btn-blue">Регистрация</a>
            <a href="/src/pages/auth/authorization.html?tab=login" class="btn btn-dark" style="margin-right: 10px;">Вход</a>
        `;
    }

    headerHTML += `
                    <button id="themeToggle" class="theme-toggle" type="button" aria-label="Переключить тему">
                        <svg class="icon-sun" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><circle cx="12" cy="12" r="4"/><path d="M12 2v2M12 20v2M4.93 4.93l1.41 1.41M17.66 17.66l1.41 1.41M2 12h2M20 12h2M4.93 19.07l1.41-1.41M17.66 6.34l1.41-1.41"/></svg>
                        <svg class="icon-moon" viewBox="0 0 24 24" fill="currentColor"><path d="M21 12.79A9 9 0 1 1 11.21 3 7 7 0 0 0 21 12.79z"/></svg>
                    </button>
                </div>
            </div>
        </header>
    `;

    headerPlaceholder.innerHTML = headerHTML;

    // ======== КЛЮЧЕВОЙ МОМЕНТ: инициализация модалки ПОСЛЕ вставки ========
    // Не ждём событий, сразу проверяем, что кнопка в DOM и вешаем обработчик
    const openPricingLink = document.getElementById('openPricing');
    if (openPricingLink) {
        // Удаляем старые обработчики (если они есть) — чтобы не дублировались
        const newLink = openPricingLink.cloneNode(true);
        openPricingLink.parentNode.replaceChild(newLink, openPricingLink);

        newLink.addEventListener('click', function(e) {
            e.preventDefault();
            loadPricingModal();
        });
    }
}


async function loadPricingModal() {
    // Проверяем, не открыта ли уже модалка
    let modal = document.getElementById('pricingModal');
    if (modal) {
        modal.classList.add('active');
        document.body.classList.add('modal-open');
        return;
    }

    try {
        const response = await fetch('/src/components/pricing/pricing.html');
        if (!response.ok) {
            throw new Error(`HTTP ${response.status}`);
        }
        const html = await response.text();

        const temp = document.createElement('div');
        temp.innerHTML = html;
        const modalElement = temp.firstElementChild;
        if (modalElement) {
            document.body.appendChild(modalElement);
            initPricingModal();
        } else {
            console.error('Не удалось найти элемент модалки в HTML');
        }
    } catch (error) {
        console.error('Ошибка загрузки модального окна:', error);
        alert('Не удалось загрузить тарифы. Попробуйте позже.');
    }
}

function initPricingModal() {
    const modal = document.getElementById('pricingModal');
    const closeBtn = document.getElementById('closePricing');

    if (!modal) return;

    function openModal() {
        modal.classList.add('active');
        document.body.classList.add('modal-open');
    }

    function closeModal() {
        modal.classList.remove('active');
        document.body.classList.remove('modal-open');
    }

    if (closeBtn) {
        closeBtn.addEventListener('click', closeModal);
    }

    modal.addEventListener('click', function(e) {
        if (e.target === modal) {
            closeModal();
        }
    });

    document.addEventListener('keydown', function(e) {
        if (e.key === 'Escape' && modal.classList.contains('active')) {
            closeModal();
        }
    });

    // Открываем сразу, если модалка только что загрузилась
    openModal();
}



document.addEventListener('click', function(e) {
    if (e.target && e.target.id === 'logoutBtn') {
        if (confirm('Вы действительно хотите выйти?')) {
            localStorage.removeItem('user');
            localStorage.removeItem('token');
            window.location.href = '/src/pages/home/home.html';
        }
    }
});


document.addEventListener('click', function(e) {
    if (e.target && e.target.closest('#themeToggle')) {
        const isLight = document.documentElement.getAttribute('data-theme') === 'light';
        if (isLight) {
            document.documentElement.removeAttribute('data-theme');
            localStorage.setItem('theme', 'dark');
        } else {
            document.documentElement.setAttribute('data-theme', 'light');
            localStorage.setItem('theme', 'light');
        }
    }
});

document.addEventListener('DOMContentLoaded', renderHeader);
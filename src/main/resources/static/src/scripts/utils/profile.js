// profile.js

async function loadUserProfile() {
    // Получаем параметры из URL
    const urlParams = new URLSearchParams(window.location.search);
    const userType = urlParams.get('type');
    const userId = urlParams.get('id');

    if (!userType || !userId) {
        console.error('Не указан тип пользователя или ID');
        window.location.href = '/src/pages/home/home.html';
        return;
    }

    try {
        const response = await apiFetch(`/api/profile/${userType}/${userId}`);
        if (!response.ok) throw new Error('Ошибка загрузки профиля');

        const profile = await response.json();
        console.log('Данные профиля:', profile);

        // Общие поля для всех типов пользователей
        document.getElementById('profile-name').textContent = `${profile.firstName} ${profile.lastName}`;
        document.getElementById('profile-company').textContent = profile.companyName || 'Не указано';
        document.getElementById('contact-phone').textContent = profile.phone || '—';
        document.getElementById('contact-email').textContent = profile.email || '—';
        document.getElementById('company-address').textContent = profile.address || '—';
        document.getElementById('company-name').textContent = profile.companyName || '—';
        document.getElementById('business-type').textContent = profile.businessType || '—';

        if (profile.createdAt) {
            const date = new Date(profile.createdAt);
            document.getElementById('created-at').textContent = date.toLocaleDateString('ru-RU');
        } else {
            document.getElementById('created-at').textContent = '—';
        }

        document.getElementById('profile-welcome').textContent =
            `Добро пожаловать, ${profile.firstName} ${profile.lastName}!`;

        const avatarImg = document.getElementById('profile-avatar');
        if (profile.profileImage) {
            avatarImg.src = profile.profileImage;
            avatarImg.onerror = () => { avatarImg.src = '/src/images/defaultAvatar.webp'; };
        } else {
            avatarImg.src = '/src/images/defaultAvatar.webp';
        }

        // Разное для подрядчика и заказчика
        if (profile.type === 'contractor') {
            // Подрядчик — показываем aboutCompany и услуги
            document.getElementById('about-company').textContent = profile.aboutCompany || '—';

            // Загружаем услуги
            if (profile.servicesIds) {
                const servicesContainer = document.getElementById('services-list');
                const servicesResponse = await apiFetch(`/api/services?ids=${profile.servicesIds}`);
                if (servicesResponse.ok) {
                    const services = await servicesResponse.json();
                    if (services.length > 0) {
                        const groupedServices = {};
                        services.forEach(service => {
                            if (!groupedServices[service.name]) {
                                groupedServices[service.name] = [];
                            }
                            groupedServices[service.name].push(service.description);
                        });

                        let servicesHtml = '';
                        for (const [name, descriptions] of Object.entries(groupedServices)) {
                            servicesHtml += `
                                <div class="service-group">
                                    <h3>${name}</h3>
                                    <ul>
                                        ${descriptions.map(desc => `<li>${desc}</li>`).join('')}
                                    </ul>
                                </div>
                            `;
                        }
                        servicesContainer.innerHTML = servicesHtml;
                    } else {
                        servicesContainer.innerHTML = '<p>Нет услуг</p>';
                    }
                } else {
                    servicesContainer.innerHTML = '<p>Нет услуг</p>';
                }
            } else {
                document.getElementById('services-list').innerHTML = '<p>Нет услуг</p>';
            }

            // Скрываем блок "О компании" для подрядчика? Нет, показываем как aboutCompany
            // Всё ок

        } else if (profile.type === 'customer') {
            // Заказчик — показываем about (о себе)
            document.getElementById('about-company').textContent = profile.about || '—';
            // У заказчика нет услуг — скрываем блок или показываем заглушку
            const servicesSection = document.querySelector('#services-list')?.closest('section');
            if (servicesSection) {
                servicesSection.style.display = 'none';
            }
        }

    } catch (error) {
        console.error('Ошибка загрузки профиля:', error);
        const errorBlocks = ['contact-phone', 'contact-email', 'company-address', 'company-name', 'about-company'];
        errorBlocks.forEach(id => {
            const el = document.getElementById(id);
            if (el && el.textContent === '—') el.textContent = 'Ошибка загрузки';
        });
    }
    loadUserRating();
}

// Загрузка рейтинга пользователя
async function loadUserRating() {
    const user = JSON.parse(localStorage.getItem('user'));
    if (!user || !user.id) return;

    try {
        const response = await apiFetch(`/api/reviews/average/${user.id}?userType=${user.userType}`);
        const data = await response.json();
        const rating = data.average || 0;

        // Обновляем отображение звёзд
        const stars = document.querySelectorAll('#reviews-stars .star');
        stars.forEach((star, index) => {
            if (index < Math.round(rating)) {
                star.classList.add('active');
                star.textContent = '★';
            } else {
                star.classList.remove('active');
                star.textContent = '☆';
            }
        });

        // Обновляем текстовый рейтинг
        const avgSpan = document.getElementById('average-rating');
        if (avgSpan) avgSpan.textContent = rating.toFixed(1);
    } catch (error) {
        console.error('Ошибка загрузки рейтинга:', error);
    }
}

// Модальное окно со списком отзывов
// Модальное окно со списком отзывов
async function openReviewsModal() {
    const user = JSON.parse(localStorage.getItem('user'));
    if (!user || !user.id) return;

    const modal = document.getElementById('reviewsListModal');
    const content = document.getElementById('reviewsListContent');
    if (!modal || !content) return;

    modal.style.display = 'flex';
    content.innerHTML = '<div class="loading">Загрузка отзывов...</div>';

    try {
        const response = await apiFetch(`/api/reviews/user/${user.id}?userType=${user.userType}`);
        const reviews = await response.json();

        if (reviews.length === 0) {
            content.innerHTML = '<div class="empty-state">Пока нет отзывов</div>';
            return;
        }

        // Загружаем реальные имена авторов отзывов
        const reviewsWithNames = [];
        for (const review of reviews) {
            let authorName = 'Пользователь';

            try {
                // Определяем автора по receiverType
                if (review.receiverType === 'customer') {
                    // Отзыв о заказчике → автор — подрядчик
                    const res = await apiFetch(`/api/contractors/${review.contractorId}`);
                    if (res.ok) {
                        const data = await res.json();
                        authorName = data.companyName || `${data.firstName} ${data.lastName}`;
                    }
                } else if (review.receiverType === 'contractor') {
                    // Отзыв о подрядчике → автор — заказчик
                    const res = await apiFetch(`/api/customers/${review.customerId}`);
                    if (res.ok) {
                        const data = await res.json();
                        authorName = `${data.name} ${data.surname}`;
                    }
                }
            } catch(e) {
                console.error('Ошибка загрузки автора отзыва:', e);
            }

            reviewsWithNames.push({ ...review, authorName });
        }

        content.innerHTML = reviewsWithNames.map(review => `
            <div class="review-item">
                <div class="review-item-header">
                    <span class="review-item-author">${escapeHtml(review.authorName)}</span>
                    <span class="review-item-rating">${'★'.repeat(review.rating)}${'☆'.repeat(5 - review.rating)}</span>
                </div>
                <div class="review-item-title">${escapeHtml(review.title)}</div>
                <div class="review-item-comment">${escapeHtml(review.comment)}</div>
                <div class="review-item-date">${new Date(review.createdAt).toLocaleDateString('ru-RU')}</div>
            </div>
        `).join('');

    } catch (error) {
        console.error('Ошибка загрузки отзывов:', error);
        content.innerHTML = '<div class="empty-state">Ошибка загрузки</div>';
    }
}

function escapeHtml(str) {
    if (!str) return '';
    return str.replace(/[&<>]/g, function(m) {
        if (m === '&') return '&amp;';
        if (m === '<') return '&lt;';
        if (m === '>') return '&gt;';
        return m;
    });
}

// Навешиваем обработчик на кнопку "Отзывы"
const reviewsBtn = document.getElementById('openReviewsBtn');
if (reviewsBtn) {
    reviewsBtn.addEventListener('click', openReviewsModal);
}

// Закрытие модального окна
const closeReviewsBtn = document.getElementById('closeReviewsModalBtn');
if (closeReviewsBtn) {
    closeReviewsBtn.addEventListener('click', () => {
        const modal = document.getElementById('reviewsListModal');
        if (modal) modal.style.display = 'none';
    });
}

const reviewsModal = document.getElementById('reviewsListModal');
if (reviewsModal) {
    reviewsModal.addEventListener('click', (e) => {
        if (e.target === reviewsModal) reviewsModal.style.display = 'none';
    });
}

// Вызываем загрузку рейтинга после загрузки профиля
// Добавь этот вызов в конец loadUserProfile() или в конец файла
if (document.getElementById('reviews-stars')) {
    loadUserRating();
}



document.addEventListener('DOMContentLoaded', loadUserProfile);
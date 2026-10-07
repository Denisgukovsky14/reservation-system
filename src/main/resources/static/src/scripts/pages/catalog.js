//alert('catalog.js загружен (финальная версия)');

// ========== РАБОТА С ЛИМИТОМ ОТКЛИКОВ ==========

// Кнопка сброса для тестирования
const resetBtn = document.getElementById('resetLimitsBtn');
if (resetBtn) {
    resetBtn.addEventListener('click', () => {
        localStorage.removeItem('sentContractorIds');
        localStorage.removeItem('blockedUntil');
        localStorage.removeItem('usedResponses');
        alert('Лимит откликов сброшен! Обновите страницу.');
        location.reload();
    });
}

// Получить список ID подрядчиков, которым уже отправили запрос
function getSentContractorIds() {
    const sent = localStorage.getItem('sentContractorIds');
    return sent ? JSON.parse(sent) : [];
}

// Сохранить список отправленных
function setSentContractorIds(ids) {
    localStorage.setItem('sentContractorIds', JSON.stringify(ids));
}

// Добавить подрядчика в список отправленных
function addSentContractor(contractorId) {
    const sent = getSentContractorIds();
    if (!sent.includes(contractorId)) {
        sent.push(contractorId);
        setSentContractorIds(sent);
    }
}

// Проверить, отправляли ли уже этому подрядчику
function isContractorSent(contractorId) {
    const sent = getSentContractorIds();
    return sent.includes(contractorId);
}

// Получить количество использованных откликов
function getUsedResponses() {
    const sent = getSentContractorIds();
    return sent.length;
}

// Получить время блокировки
function getBlockedUntil() {
    const blocked = localStorage.getItem('blockedUntil');
    return blocked ? parseInt(blocked) : 0;
}

// Заблокировать на 2 часа
function blockResponses() {
    const blockedUntil = Date.now() + (2 * 60 * 60 * 1000);
    localStorage.setItem('blockedUntil', blockedUntil);
}

// Проверить, можно ли отправить отклик (учитывая и лимит, и конкретного подрядчика)
function canSendResponse(contractorId) {
    // Если этому подрядчику уже отправляли
    if (isContractorSent(contractorId)) {
        return { canSend: false, reason: 'Вы уже отправили запрос этому подрядчику.' };
    }

    const used = getUsedResponses();
    const blockedUntil = getBlockedUntil();
    const now = Date.now();

    // Если время блокировки ещё не прошло
    if (blockedUntil > now) {
        const remainingMs = blockedUntil - now;
        const remainingMinutes = Math.ceil(remainingMs / (60 * 1000));
        return {
            canSend: false,
            reason: `Вы использовали все 3 отклика. Новые отклики будут доступны через ${remainingMinutes} минут.`
        };
    }

    // Если блокировка прошла — сбрасываем всё
    if (blockedUntil > 0 && blockedUntil <= now) {
        localStorage.removeItem('sentContractorIds');
        localStorage.removeItem('blockedUntil');
        return { canSend: true, reason: null, remaining: 3 };
    }

    // Если использовано меньше 3 — можно
    if (used < 3) {
        return { canSend: true, reason: null, remaining: 3 - used };
    }

    // Если использовано 3 — блокируем
    if (used >= 3) {
        blockResponses();
        return {
            canSend: false,
            reason: `Вы использовали все 3 отклика. Новые отклики будут доступны через 2 часа.`
        };
    }

    return { canSend: true, reason: null };
}

// Отправить отклик (добавить подрядчика в список)
function useResponse(contractorId) {
    addSentContractor(contractorId);

    // Если достигли 3 — блокируем
    if (getUsedResponses() >= 3) {
        blockResponses();
    }
}

// Обновить состояние всех кнопок
function updateButtonsState() {
    const used = getUsedResponses();
    const blockedUntil = getBlockedUntil();
    const now = Date.now();
    const isBlocked = blockedUntil > now;

    document.querySelectorAll('.contact-btn').forEach(btn => {
        const contractorId = parseInt(btn.getAttribute('data-contractor-id'));
        const isSent = isContractorSent(contractorId);

        // Кнопка неактивна, если: уже отправлено ИЛИ лимит исчерпан ИЛИ блокировка
        if (isSent || (isBlocked && used >= 3)) {
            btn.disabled = true;
            btn.style.opacity = '0.5';
            btn.style.cursor = 'not-allowed';
            if (isSent) {
                btn.title = 'Запрос уже отправлен';
            } else {
                btn.title = 'Лимит откликов исчерпан';
            }
        } else {
            btn.disabled = false;
            btn.style.opacity = '1';
            btn.style.cursor = 'pointer';
            btn.title = 'Отправить запрос подрядчику';
        }
    });

    // Обновляем счётчик на странице (если есть)
    const counterEl = document.getElementById('responses-counter');
    if (counterEl) {
        if (isBlocked && used >= 3) {
            const remainingMs = blockedUntil - now;
            const remainingMinutes = Math.ceil(remainingMs / (60 * 1000));
            counterEl.textContent = `Откликов сегодня: ${used}/3 (следующие через ${remainingMinutes} мин)`;
        } else {
            counterEl.textContent = `Откликов сегодня: ${used}/3 (осталось ${3 - used})`;
        }
    }
}

// ========== ОТОБРАЖЕНИЕ КАРТОЧЕК ==========

function displayContractorsCards(contractors) {
    const container = document.getElementById('cards-view');

    if (!contractors || contractors.length === 0) {
        container.innerHTML = '<div class="empty-state"><h3>Подрядчики не найдены</h3></div>';
        return;
    }

    container.innerHTML = contractors.map(contractor => `
        <div class="contractor-card" data-id="${contractor.id}">
            <div class="card-info">
                <div class="card-meta">${contractor.businessType || 'Частный мастер'} • ${contractor.address || 'Регион не указан'}</div>
                <h3 class="card-name">${contractor.companyName || contractor.firstName + ' ' + contractor.lastName}</h3>
                <p class="card-desc">${contractor.aboutCompany ? contractor.aboutCompany.substring(0, 150) + '...' : 'Информация отсутствует'}</p>
                <button class="btn btn-blue contact-btn" data-contractor-id="${contractor.id}" data-contractor-name="${contractor.companyName || contractor.firstName + ' ' + contractor.lastName}">
                    Связаться
                </button>
            </div>
            <div class="card-image" style="background-image: url('${contractor.profileImage || 'https://images.unsplash.com/photo-1581091226825-a6a2a5aee158?auto=format&fit=crop&w=600&q=80'}');"></div>
        </div>
    `).join('');

    attachButtonHandlers();
    updateButtonsState();
}

// ========== ОБРАБОТЧИК КНОПОК ==========

function attachButtonHandlers() {
    document.querySelectorAll('.contact-btn').forEach(btn => {
        btn.removeEventListener('click', handleContactClick);
        btn.addEventListener('click', handleContactClick);
    });
}

async function handleContactClick(e) {
    e.preventDefault();
    e.stopPropagation();

    const btn = e.currentTarget;
    const contractorId = parseInt(btn.getAttribute('data-contractor-id'));
    const contractorName = btn.getAttribute('data-contractor-name');
    const dealId = localStorage.getItem('currentDealId');

    if (!dealId) {
        showCustomAlert('Ошибка', 'Не найдена активная заявка', false);
        return;
    }

    const { canSend, reason } = canSendResponse(contractorId);

    if (!canSend) {
        showCustomAlert('Доступ ограничен', reason, false);
        return;
    }

    // Отправляем отклик (увеличиваем счётчик локально)
    useResponse(contractorId);

    // Отправляем запрос на сервер
    try {
        const response = await apiFetch(`/api/deals/${dealId}/select-contractor`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ contractorId: contractorId })
        });

        const data = await response.json();

        if (data.success) {
            // Показываем успешный алерт
            showCustomAlert(
                'Запрос отправлен!',
                `Подрядчик: ${contractorName}\n\nЗапрос на сотрудничество отправлен. Подрядчик получит уведомление и свяжется с вами в ближайшее время.\n\nИспользовано откликов: ${getUsedResponses()}/3`,
                true
            );

            // НЕ ПЕРЕХОДИМ на страницу сделки! Остаёмся в каталоге
            // Просто обновляем состояние кнопок
            updateButtonsState();

            // Опционально: перезагружаем список подрядчиков, чтобы обновить кнопки
            // loadContractors();
        } else {
            showCustomAlert('Ошибка', data.message || 'Не удалось отправить запрос', false);
            rollbackResponse(contractorId);
            updateButtonsState();
        }
    } catch (error) {
        console.error('Ошибка:', error);
        showCustomAlert('Ошибка', 'Не удалось соединиться с сервером', false);
        rollbackResponse(contractorId);
        updateButtonsState();
    }
}

// ========== ТАБЛИЧНОЕ ПРЕДСТАВЛЕНИЕ ==========

function displayContractorsTable(contractors) {
    const tbody = document.querySelector('#table-view tbody');
    if (!tbody) return;

    if (!contractors || contractors.length === 0) {
        tbody.innerHTML = `<tr><td colspan="5" style="text-align: center;">Подрядчики не найдены</td></tr>`;
        return;
    }

    tbody.innerHTML = contractors.map(contractor => `
        <tr>
            <td>${contractor.companyName || contractor.firstName + ' ' + contractor.lastName}</td>
            <td>По вашему запросу</td>
            <td>${contractor.address || '—'}</td>
            <td><div class="rating-bar"><div class="rating-fill" style="width: 90%;"></div></div></td>
            <td><button class="btn btn-small contact-btn" data-contractor-id="${contractor.id}" data-contractor-name="${contractor.companyName || contractor.firstName + ' ' + contractor.lastName}">Связаться</button></td>
        </tr>
    `).join('');

    attachButtonHandlers();
    updateButtonsState();
}

// ========== ЗАГРУЗКА ПОДРЯДЧИКОВ ==========

async function loadContractors() {
    const dealId = localStorage.getItem('currentDealId');
    if (!dealId) return;

    try {
        const dealResponse = await apiFetch(`/api/deals/${dealId}`);
        const deal = await dealResponse.json();
        if (!deal.servicesIds) return;

        const url = `/api/contractors/search-by-services?servicesIds=${deal.servicesIds}`;
        const response = await apiFetch(url);
        const contractors = await response.json();

        displayContractorsCards(contractors);
        displayContractorsTable(contractors);
    } catch (error) {
        console.error('Ошибка загрузки подрядчиков:', error);
    }
}

// ========== ЗАПУСК ==========
loadContractors();

// Периодически обновляем состояние кнопок (каждую минуту)
setInterval(() => {
    updateButtonsState();
}, 60000);

// ========== КАСТОМНЫЙ АЛЕРТ ==========

function showCustomAlert(title, message, isSuccess = true) {
    const alertEl = document.getElementById('customAlert');
    const titleEl = document.getElementById('alertTitle');
    const messageEl = document.getElementById('alertMessage');
    const iconEl = document.querySelector('.custom-alert-icon');

    titleEl.textContent = title;
    messageEl.textContent = message;

    if (isSuccess) {
        iconEl.textContent = '✅';
        iconEl.style.color = '#22c55e';
    } else {
        iconEl.textContent = '⚠️';
        iconEl.style.color = '#ef4444';
    }

    alertEl.style.display = 'flex';

    // Закрытие по кнопке
    const okBtn = document.getElementById('alertOkBtn');
    const closeAlert = () => {
        alertEl.style.display = 'none';
        okBtn.removeEventListener('click', closeAlert);
    };
    okBtn.addEventListener('click', closeAlert);

    // Закрытие по клику вне окна
    alertEl.addEventListener('click', (e) => {
        if (e.target === alertEl) {
            closeAlert();
        }
    });
}
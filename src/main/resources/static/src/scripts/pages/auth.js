// ========== ЭЛЕМЕНТЫ ФОРМЫ ВХОДА ==========
const loginForm = document.getElementById('loginForm');
const emailInput = document.getElementById('email');
const passInput = document.getElementById('password');
const toggleBtn = document.getElementById('toggleBtn');
const eyeIcon = document.getElementById('eyeIcon');

// ========== ЭЛЕМЕНТЫ ФОРМЫ РЕГИСТРАЦИИ ==========
const registerForm = document.getElementById('registerForm');
const regName = document.getElementById('regName');
const regSurname = document.getElementById('regSurname');
const regPatronymic = document.getElementById('regPatronymic');
const regPhone = document.getElementById('regPhone');
const regEmail = document.getElementById('regEmail');
const regPassword = document.getElementById('regPassword');
const toggleRegBtn = document.getElementById('toggleRegBtn');
const eyeRegIcon = document.getElementById('eyeRegIcon');
const regCaptcha = document.getElementById('regCaptcha');

// ========== ПЕРЕКЛЮЧЕНИЕ МЕЖДУ ФОРМАМИ ==========
const loginSection = document.getElementById('loginSection');
const registerSection = document.getElementById('registerSection');
const showRegisterBtn = document.getElementById('showRegisterBtn');
const showLoginBtn = document.getElementById('showLoginBtn');

if (showRegisterBtn) {
    showRegisterBtn.addEventListener('click', () => {
        loginSection.style.display = 'none';
        registerSection.style.display = 'block';
        clearAllErrors();
    });
}

if (showLoginBtn) {
    showLoginBtn.addEventListener('click', () => {
        registerSection.style.display = 'none';
        loginSection.style.display = 'block';
        clearAllErrors();
    });
}

// ========== ПОКАЗ/СКРЫТИЕ ПАРОЛЯ ==========
if (toggleBtn) {
    toggleBtn.addEventListener('click', () => {
        const isPass = passInput.type === 'password';
        passInput.type = isPass ? 'text' : 'password';
        eyeIcon.style.color = isPass ? 'var(--primary-blue)' : 'var(--text-muted)';
    });
}

if (toggleRegBtn && eyeRegIcon && regPassword) {
    toggleRegBtn.addEventListener('click', () => {
        const isPass = regPassword.type === 'password';
        regPassword.type = isPass ? 'text' : 'password';
        eyeRegIcon.style.color = isPass ? 'var(--primary-blue)' : 'var(--text-muted)';
    });
}

// ========== ВАЛИДАЦИЯ EMAIL ==========
const validateEmail = (email) => {
    return String(email)
        .toLowerCase()
        .match(/^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/);
};

// ========== ВАЛИДАЦИЯ ТЕЛЕФОНА (российский номер, минимум 10 цифр) ==========
const validatePhone = (phone) => {
    const digits = phone.replace(/\D/g, '');
    return digits.length >= 10 && digits.length <= 12;
};

// ========== ПРОВЕРКА НАДЁЖНОСТИ ПАРОЛЯ ==========
function checkPasswordStrength(password) {
    let score = 0;
    if (password.length >= 8) score++;
    if (password.match(/[a-z]/)) score++;
    if (password.match(/[A-Z]/)) score++;
    if (password.match(/[0-9]/)) score++;
    if (password.match(/[^a-zA-Z0-9]/)) score++;
    return score;
}

// ========== ИНДИКАТОР НАДЁЖНОСТИ ПАРОЛЯ ==========
if (regPassword) {
    const strengthIndicator = document.createElement('div');
    strengthIndicator.id = 'password-strength';
    strengthIndicator.style.cssText = `
        margin-top: 6px;
        font-size: 12px;
        font-weight: 500;
        transition: all 0.2s;
        color: var(--text-muted);
    `;
    regPassword.parentNode.insertAdjacentElement('afterend', strengthIndicator);

    regPassword.addEventListener('input', () => {
        const val = regPassword.value;
        const score = checkPasswordStrength(val);

        const levels = ['Очень слабый', 'Слабый', 'Средний', 'Хороший', 'Сильный', 'Отличный'];
        const colors = ['#ff4d4f', '#ff7875', '#faad14', '#52c41a', '#1890ff', '#722ed1'];

        if (val.length === 0) {
            strengthIndicator.textContent = '';
            strengthIndicator.style.color = 'var(--text-muted)';
            return;
        }

        const index = Math.min(score, 5);
        strengthIndicator.textContent = 'Надёжность: ' + levels[index];
        strengthIndicator.style.color = colors[index];
    });
}

// ========== СКРОЛЛ К ПЕРВОЙ ОШИБКЕ ==========
function scrollToFirstError(form) {
    const firstError = form.querySelector('.form-group.error');
    if (firstError) {
        firstError.scrollIntoView({
            behavior: 'smooth',
            block: 'center'
        });

        firstError.style.transition = 'background-color 0.3s';
        firstError.style.backgroundColor = 'rgba(255, 77, 79, 0.1)';
        setTimeout(() => {
            firstError.style.backgroundColor = 'transparent';
        }, 1500);
    }
}

// ========== РАБОТА С ОШИБКАМИ ==========
function showError(inputElement, message) {
    const formGroup = inputElement.closest('.form-group');
    if (!formGroup) return;

    formGroup.classList.add('error');
    const errorSpan = formGroup.querySelector('.error-message');
    if (errorSpan) {
        errorSpan.textContent = message;
        errorSpan.style.display = 'block';
    }
}

function clearError(inputElement) {
    const formGroup = inputElement.closest('.form-group');
    if (!formGroup) return;

    formGroup.classList.remove('error');
    const errorSpan = formGroup.querySelector('.error-message');
    if (errorSpan) {
        errorSpan.style.display = 'none';
    }
}

function clearAllErrors() {
    document.querySelectorAll('.form-group.error').forEach(group => {
        group.classList.remove('error');
        const errorSpan = group.querySelector('.error-message');
        if (errorSpan) {
            errorSpan.style.display = 'none';
        }
    });
}

function showGeneralError(form, message) {
    const oldError = form.querySelector('.general-error');
    if (oldError) oldError.remove();

    const errorDiv = document.createElement('div');
    errorDiv.className = 'general-error';
    errorDiv.style.cssText = `
        padding: 12px 16px;
        margin-bottom: 20px;
        background: #fff2f0;
        border: 1px solid #ff4d4f;
        border-radius: 8px;
        color: #ff4d4f;
        font-size: 14px;
        grid-column: 1 / -1;
    `;
    errorDiv.textContent = message;
    form.prepend(errorDiv);

    setTimeout(() => {
        if (errorDiv.parentNode) {
            errorDiv.remove();
        }
    }, 5000);
}

// ========== УБИРАЕМ ОШИБКИ ПРИ ВВОДЕ ==========
document.querySelectorAll('input').forEach(input => {
    input.addEventListener('input', () => {
        clearError(input);
        const form = input.closest('form');
        if (form) {
            const generalError = form.querySelector('.general-error');
            if (generalError) generalError.remove();
        }
    });
});

// ========== ОБРАБОТЧИК ФОРМЫ ВХОДА ==========
if (loginForm) {
    loginForm.addEventListener('submit', async (e) => {
        e.preventDefault();

        clearAllErrors();
        const generalError = loginForm.querySelector('.general-error');
        if (generalError) generalError.remove();

        let isValid = true;

        if (!validateEmail(emailInput.value)) {
            showError(emailInput, 'Введите корректный адрес почты');
            isValid = false;
        }

        if (passInput.value.length < 8) {
            showError(passInput, 'Пароль должен содержать не менее 8 символов');
            isValid = false;
        }

        if (!isValid) {
            showGeneralError(loginForm, 'Пожалуйста, исправьте ошибки в форме');
            scrollToFirstError(loginForm);
            return;
        }

        try {
            const response = await apiFetch('/api/auth/login', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    email: emailInput.value,
                    password: passInput.value
                })
            });

            const data = await response.json();

            if (data.success) {
                localStorage.setItem('user', JSON.stringify(data.user));
                localStorage.setItem('token', data.token);
                window.location.href = '/src/pages/home/home.html';
            } else {
                showGeneralError(loginForm, data.message || 'Ошибка входа. Проверьте email и пароль');
                showError(emailInput, '');
                showError(passInput, '');
                scrollToFirstError(loginForm);
            }
        } catch (error) {
            showGeneralError(loginForm, 'Ошибка соединения с сервером. Попробуйте позже');
        }
    });
}

// ========== ОБРАБОТЧИК ФОРМЫ РЕГИСТРАЦИИ ==========
if (registerForm) {
    registerForm.addEventListener('submit', async (e) => {
        e.preventDefault();

        clearAllErrors();
        const generalError = registerForm.querySelector('.general-error');
        if (generalError) generalError.remove();

        let isValid = true;

        if (!regName.value.trim()) {
            showError(regName, 'Введите имя');
            isValid = false;
        }

        if (!regSurname.value.trim()) {
            showError(regSurname, 'Введите фамилию');
            isValid = false;
        }

        if (!validateEmail(regEmail.value)) {
            showError(regEmail, 'Введите корректный адрес почты');
            isValid = false;
        }

        if (!validatePhone(regPhone.value)) {
            showError(regPhone, 'Введите корректный номер телефона (не менее 10 цифр)');
            isValid = false;
        }

        const password = regPassword.value;
        const score = checkPasswordStrength(password);

        if (password.length < 8) {
            showError(regPassword, 'Пароль должен содержать не менее 8 символов');
            isValid = false;
        } else if (score < 3) {
            let hint = 'Пароль слишком слабый. ';
            if (!password.match(/[A-Z]/)) hint += 'Добавьте заглавную букву. ';
            if (!password.match(/[0-9]/)) hint += 'Добавьте цифру. ';
            if (!password.match(/[^a-zA-Z0-9]/)) hint += 'Добавьте спецсимвол.';
            showError(regPassword, hint);
            isValid = false;
        }

        if (regCaptcha && !regCaptcha.checked) {
            const captchaGroup = regCaptcha.closest('.form-group');
            if (captchaGroup) {
                captchaGroup.classList.add('error');
                const errorSpan = captchaGroup.querySelector('.error-message');
                if (errorSpan) {
                    errorSpan.textContent = 'Подтвердите, что вы не робот';
                    errorSpan.style.display = 'block';
                }
            }
            isValid = false;
        }

        if (!isValid) {
            showGeneralError(registerForm, 'Пожалуйста, исправьте ошибки в форме');
            scrollToFirstError(registerForm);
            return;
        }

        const registerData = {
            firstName: regName.value.trim(),
            lastName: regSurname.value.trim(),
            patronymic: regPatronymic ? regPatronymic.value.trim() : '',
            phone: regPhone.value.trim(),
            email: regEmail.value.trim(),
            password: regPassword.value,
            companyName: document.getElementById('regCompanyName')?.value?.trim() || '',
            businessType: document.getElementById('regBusinessType')?.value || 'Отсутствует',
            address: document.getElementById('regAddress')?.value?.trim() || '',
            about: document.getElementById('regAbout')?.value?.trim() || ''
        };

        try {
            const response = await apiFetch('/api/auth/register', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(registerData)
            });

            const data = await response.json();

            if (data.success) {
                const successDiv = document.createElement('div');
                successDiv.style.cssText = `
                    padding: 12px 16px;
                    margin-bottom: 20px;
                    background: #f6ffed;
                    border: 1px solid #52c41a;
                    border-radius: 8px;
                    color: #52c41a;
                    font-size: 14px;
                    grid-column: 1 / -1;
                `;
                successDiv.textContent = 'Регистрация прошла успешно. Теперь вы можете войти.';
                registerForm.prepend(successDiv);

                setTimeout(() => {
                    if (successDiv.parentNode) {
                        successDiv.remove();
                    }
                    if (registerSection) registerSection.style.display = 'none';
                    if (loginSection) loginSection.style.display = 'block';
                    if (registerForm) registerForm.reset();
                }, 2000);
            } else {
                let errorMessage = data.message || 'Ошибка регистрации';

                if (errorMessage.toLowerCase().includes('already exists') ||
                    errorMessage.toLowerCase().includes('duplicate') ||
                    errorMessage.toLowerCase().includes('существует')) {
                    errorMessage = 'Пользователь с таким email уже существует';
                    showError(regEmail, 'Этот email уже зарегистрирован');
                    scrollToFirstError(registerForm);
                } else if (errorMessage.toLowerCase().includes('phone')) {
                    showError(regPhone, 'Этот номер телефона уже используется');
                    scrollToFirstError(registerForm);
                } else {
                    showGeneralError(registerForm, errorMessage);
                }
            }
        } catch (error) {
            showGeneralError(registerForm, 'Ошибка соединения с сервером. Попробуйте позже');
        }
    });
}
document.addEventListener("DOMContentLoaded", () => {
    // ========== ЭЛЕМЕНТЫ ==========
    const step1Panel = document.getElementById('step1');
    const step2Panel = document.getElementById('step2');
    const nextBtn1 = document.querySelector('#step1 .next-step');
    const prevBtn2 = document.querySelector('#step2 .prev-step');
    const step1Circle = document.querySelector('.step[data-step="1"]');
    const step2Circle = document.querySelector('.step[data-step="2"]');
    const showContractorsBtn = document.getElementById('showContractorsBtn');

    // Поля формы с id
    const projectNameInput = document.getElementById('projectName');
    const categorySelect = document.getElementById('category');
    const budgetInput = document.getElementById('budget');
    const deadlineInput = document.getElementById('deadline');
    const descriptionTextarea = document.getElementById('description');
    const areaInput = document.getElementById('area');
    const heightInput = document.getElementById('height');
    const fileInput = document.getElementById('fileUpload');

    // Переменные для хранения выбранных услуг и файлов
    let selectedServices = [];
    let servicesData = [];
    let selectedFiles = [];

    // ========== ФУНКЦИИ ДЛЯ РАБОТЫ С ФАЙЛАМИ ==========

    // Обновление отображения списка файлов
    function updateFileList() {
        const fileListContainer = document.getElementById('fileList');
        if (!fileListContainer) return;

        if (selectedFiles.length === 0) {
            fileListContainer.innerHTML = '<div class="file-empty">Файлы не выбраны</div>';
            return;
        }

        let html = '';
        for (let i = 0; i < selectedFiles.length; i++) {
            const file = selectedFiles[i];
            html += `
                <div class="file-item" data-index="${i}">
                    ${file.type.startsWith('image/') ?
                        `<img src="${URL.createObjectURL(file)}" class="file-preview" alt="preview">` :
                        '<span class="file-preview">📄</span>'
                    }
                    <span class="file-name" title="${file.name}">${file.name}</span>
                    <span class="file-size">(${(file.size / 1024).toFixed(1)} KB)</span>
                    <button type="button" class="remove-file" data-index="${i}">&times;</button>
                </div>
            `;
        }
        fileListContainer.innerHTML = html;

        // Добавляем обработчики на кнопки удаления
        const removeButtons = document.querySelectorAll('.remove-file');
        for (let i = 0; i < removeButtons.length; i++) {
            removeButtons[i].addEventListener('click', function(e) {
                const idx = parseInt(this.getAttribute('data-index'));
                selectedFiles.splice(idx, 1);

                const dataTransfer = new DataTransfer();
                for (let j = 0; j < selectedFiles.length; j++) {
                    dataTransfer.items.add(selectedFiles[j]);
                }
                fileInput.files = dataTransfer.files;

                updateFileList();
                //alert('Файл удалён, осталось: ' + selectedFiles.length);
            });
        }
    }

    // Удаление файла из списка
    function removeFile(index) {
        selectedFiles.splice(index, 1);

        // Обновляем input[type=file]
        const dataTransfer = new DataTransfer();
        selectedFiles.forEach(file => {
            dataTransfer.items.add(file);
        });
        fileInput.files = dataTransfer.files;

        updateFileList();
    }

    // Обработчик выбора файлов
    function handleFileSelect(event) {
        const files = Array.from(event.target.files);
        //alert('Выбрано файлов: ' + files.length);

        for (let i = 0; i < files.length; i++) {
            const file = files[i];
            //alert('Файл: ' + file.name + ' (' + (file.size / 1024).toFixed(1) + ' KB)');

            const exists = selectedFiles.some(f => f.name === file.name && f.size === file.size);
            if (!exists) {
                selectedFiles.push(file);
                //alert('Файл добавлен в список');
            }
        }

        // Обновляем input
        const dataTransfer = new DataTransfer();
        for (let i = 0; i < selectedFiles.length; i++) {
            dataTransfer.items.add(selectedFiles[i]);
        }
        fileInput.files = dataTransfer.files;

        updateFileList();
        //alert('Всего файлов в selectedFiles: ' + selectedFiles.length);
    }

    // Настройка Drag & Drop
    function setupDragAndDrop() {
        const uploadBox = document.getElementById('uploadBox');
        if (!uploadBox) return;

        uploadBox.addEventListener('dragover', (e) => {
            e.preventDefault();
            uploadBox.classList.add('drag-over');
        });

        uploadBox.addEventListener('dragleave', (e) => {
            e.preventDefault();
            uploadBox.classList.remove('drag-over');
        });

        uploadBox.addEventListener('drop', (e) => {
            e.preventDefault();
            uploadBox.classList.remove('drag-over');

            const files = Array.from(e.dataTransfer.files);
            files.forEach(file => {
                const exists = selectedFiles.some(f => f.name === file.name && f.size === file.size);
                if (!exists) {
                    selectedFiles.push(file);
                }
            });

            const dataTransfer = new DataTransfer();
            selectedFiles.forEach(file => {
                dataTransfer.items.add(file);
            });
            fileInput.files = dataTransfer.files;

            updateFileList();
        });
    }

    // ========== ФУНКЦИИ ДЛЯ РАБОТЫ С УСЛУГАМИ ==========

    // Загрузка услуг с сервера
    async function loadServices() {
        const container = document.getElementById('services-selector');
        if (!container) return;

        try {
            const response = await apiFetch('/api/services/all');
            const services = await response.json();
            servicesData = services;

            const grouped = {};
            services.forEach(service => {
                if (!grouped[service.name]) {
                    grouped[service.name] = [];
                }
                grouped[service.name].push({
                    id: service.id,
                    name: service.name,
                    description: service.description
                });
            });

            let html = '';
            for (const [groupName, items] of Object.entries(grouped)) {
                html += `
                    <div class="service-group" data-group="${groupName}">
                        <h4 onclick="toggleGroup('${groupName}')">
                            <span class="toggle-icon">▶</span> ${groupName}
                        </h4>
                        <div class="service-items" id="group-${groupName.replace(/\s/g, '')}" style="display: none;">
                            ${items.map(item => `
                                <div class="service-item" data-id="${item.id}" data-name="${item.name}" data-desc="${item.description || ''}" onclick="toggleService(this)">
                                    <span>${item.description || item.name}</span>
                                </div>
                            `).join('')}
                        </div>
                    </div>
                `;
            }
            container.innerHTML = html;
        } catch (error) {
            console.error('Ошибка загрузки услуг:', error);
            container.innerHTML = '<div class="error">Ошибка загрузки услуг</div>';
        }
    }

    window.toggleGroup = function(groupName) {
        const groupId = groupName.replace(/\s/g, '');
        const itemsDiv = document.getElementById(`group-${groupId}`);
        const toggleIcon = document.querySelector(`.service-group[data-group="${groupName}"] .toggle-icon`);
        if (itemsDiv) {
            const isVisible = itemsDiv.style.display === 'flex';
            itemsDiv.style.display = isVisible ? 'none' : 'flex';
            if (toggleIcon) toggleIcon.textContent = isVisible ? '▶' : '▼';
        }
    };

    window.toggleService = function(element) {
        element.classList.toggle('selected');
        const serviceId = element.getAttribute('data-id');
        const serviceName = element.getAttribute('data-name');
        const serviceDesc = element.getAttribute('data-desc');

        if (element.classList.contains('selected')) {
            selectedServices.push({ id: serviceId, name: serviceName, desc: serviceDesc });
        } else {
            selectedServices = selectedServices.filter(s => s.id != serviceId);
        }
        updateSelectedServicesDisplay();
    };

    function updateSelectedServicesDisplay() {
        let displayDiv = document.getElementById('selected-services-display');
        if (!displayDiv) {
            const container = document.getElementById('services-selector');
            displayDiv = document.createElement('div');
            displayDiv.id = 'selected-services-display';
            displayDiv.className = 'selected-services-list';
            container.appendChild(displayDiv);
        }

        if (selectedServices.length === 0) {
            displayDiv.innerHTML = '<strong>Выбранные услуги:</strong> <span style="color: var(--text-dim);">Ничего не выбрано</span>';
        } else {
            displayDiv.innerHTML = `
                <strong>Выбранные услуги:</strong>
                <ul>
                    ${selectedServices.map(s => `<li>${s.desc || s.name}</li>`).join('')}
                </ul>
            `;
        }
    }

    // ========== ОБЩИЕ ФУНКЦИИ ==========

    function showStep(step) {
        if (step === 1) {
            step1Panel.classList.add('active');
            step2Panel.classList.remove('active');
            step1Circle.classList.add('active');
            step1Circle.classList.remove('completed');
            step2Circle.classList.remove('active', 'completed');
        } else if (step === 2) {
            step1Panel.classList.remove('active');
            step2Panel.classList.add('active');
            step1Circle.classList.add('completed');
            step1Circle.classList.remove('active');
            step2Circle.classList.add('active');
        }
        window.scrollTo({ top: 0, behavior: 'smooth' });
    }

    function validateStep1() {
        if (!projectNameInput?.value.trim()) {
            //alert('Пожалуйста, укажите название проекта');
            projectNameInput?.focus();
            return false;
        }
        if (!budgetInput?.value || parseFloat(budgetInput.value) <= 0) {
            //alert('Пожалуйста, укажите корректный бюджет');
            budgetInput?.focus();
            return false;
        }
        if (!deadlineInput?.value) {
            //alert('Пожалуйста, укажите дедлайн');
            deadlineInput?.focus();
            return false;
        }
        if (selectedServices.length === 0) {
            //alert('Пожалуйста, выберите хотя бы одну услугу');
            return false;
        }
        return true;
    }

    function collectFormData() {
        let fullDescription = descriptionTextarea?.value || '';
        const area = areaInput?.value;
        const height = heightInput?.value;
        const category = categorySelect?.value || '';
        const projectNameRaw = projectNameInput?.value || '';

        let finalProjectName = projectNameRaw;
        if (category && projectNameRaw) {
            finalProjectName = `${category}: ${projectNameRaw}`;
        } else if (category && !projectNameRaw) {
            finalProjectName = category;
        }

        if (area || height) {
            const techParams = [];
            if (area) techParams.push(`Площадь: ${area} кв.м`);
            if (height) techParams.push(`Высота потолков: ${height} м`);
            fullDescription = `${fullDescription}\n\n--- Технические параметры ---\n${techParams.join(', ')}`;
        }

        const servicesIds = selectedServices.map(s => s.id).join(',');

        return {
            projectName: finalProjectName,
            category: category,
            budget: parseFloat(budgetInput?.value) || 0,
            deadline: deadlineInput?.value || '',
            description: fullDescription,
            servicesIds: servicesIds
        };
    }

    async function uploadFilesToServer(files) {
       // alert('uploadFilesToServer вызвана, файлов: ' + files.length);

        if (!files || files.length === 0) return [];

        const formData = new FormData();
        for (let i = 0; i < files.length; i++) {
            formData.append('files', files[i]);
           // alert('Добавлен файл в FormData: ' + files[i].name);
        }

        try {
            //alert('Отправляем запрос на /api/uploads');
            const response = await apiFetch('/api/uploads', {
                method: 'POST',
                body: formData
            });
            //alert('Ответ получен, статус: ' + response.status);

            const data = await response.json();
            //alert('Ответ сервера: success=' + data.success + ', urls=' + (data.urls ? data.urls.length : 0));

            if (data.success) {
                return data.urls;
            } else {
                throw new Error(data.message || 'Ошибка загрузки файлов');
            }
        } catch (error) {
            //alert('Ошибка в uploadFilesToServer: ' + error.message);
            throw error;
        }
    }

    async function createDealAndRedirect() {
        //alert('1. createDealAndRedirect вызвана');

        const user = JSON.parse(localStorage.getItem('user'));
        if (!user || !user.id) {
            //alert('2. Ошибка: пользователь не авторизован');
            window.location.href = '/src/pages/auth/authorization.html?tab=login';
            return;
        }
        //alert('3. Пользователь найден, ID: ' + user.id + ', тип: ' + (user.userType || 'не указан'));

        //alert('4. selectedFiles.length: ' + selectedFiles.length);

        const originalBtnText = showContractorsBtn?.textContent;
        if (showContractorsBtn) {
            showContractorsBtn.textContent = 'Загрузка...';
            showContractorsBtn.disabled = true;
        }

        try {
            // Загружаем файлы
            let imageUrls = [];
            if (selectedFiles.length > 0) {
                //alert('5. Начинаем загрузку ' + selectedFiles.length + ' файлов');
                imageUrls = await uploadFilesToServer(selectedFiles);
                //alert('6. Загружено файлов: ' + imageUrls.length + ', URLs: ' + JSON.stringify(imageUrls));
            } else {
                //alert('5. Нет файлов для загрузки, пропускаем');
            }

            // Собираем данные формы
            //alert('7. Собираем данные формы');
            const formData = collectFormData();
            //alert('8. Данные формы: проект="' + formData.projectName + '", бюджет=' + formData.budget + ', услуги=' + formData.servicesIds);

            const additionalImages = imageUrls.join(',');
            //alert('9. additionalImages: "' + additionalImages + '"');

            const dealData = {
                customerId: user.id,
                projectName: formData.projectName,
                price: formData.budget,
                description: formData.description,
                additionalImages: additionalImages,
                endDate: formData.deadline,
                servicesIds: formData.servicesIds,
                status: 'PENDING'
            };

            //alert('10. Отправляем POST запрос на /api/deals');
            const response = await apiFetch('/api/deals', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(dealData)
            });

            //alert('11. Ответ получен, статус: ' + response.status);

            const data = await response.json();
            //alert('12. Ответ сервера: success=' + data.success + ', message=' + (data.message || 'нет') + ', dealId=' + (data.dealId || 'нет'));

            if (data.success) {
                localStorage.setItem('currentDealId', data.dealId);
                //alert('13. Сделка создана! Переход на каталог');
                window.location.href = '/src/pages/catalog/catalog.html';
            } else {
                //alert('14. Ошибка от сервера: ' + data.message);
            }
        } catch (error) {
            console.error('Ошибка:', error);
            //alert('15. Исключение: ' + (error.message || 'Неизвестная ошибка'));
        } finally {
            if (showContractorsBtn) {
                showContractorsBtn.textContent = originalBtnText;
                showContractorsBtn.disabled = false;
            }
        }
    }

    // ========== ИНИЦИАЛИЗАЦИЯ ==========

    loadServices();

    // Инициализация файлового менеджера
    const selectFilesBtn = document.getElementById('selectFilesBtn');
    if (selectFilesBtn) {
        selectFilesBtn.addEventListener('click', () => {
            fileInput.click();
        });
    }
    if (fileInput) {
        fileInput.addEventListener('change', handleFileSelect);
    }
    setupDragAndDrop();
    updateFileList();

    // Обработчики кнопок навигации
    if (nextBtn1) {
        nextBtn1.addEventListener('click', function(e) {
            e.preventDefault();
            if (validateStep1()) {
                showStep(2);
            }
        });
    }

    if (prevBtn2) {
        prevBtn2.addEventListener('click', function(e) {
            e.preventDefault();
            showStep(1);
        });
    }

    if (showContractorsBtn) {
        showContractorsBtn.addEventListener('click', function(e) {
            e.preventDefault();
            createDealAndRedirect();
        });
    }
});
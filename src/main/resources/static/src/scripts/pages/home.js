// Логика слайдов
const tabs = document.querySelectorAll('.tab');
const contents = document.querySelectorAll('.tab-content');

tabs.forEach(tab => {
    tab.addEventListener('click', () => {
        const role = tab.dataset.role;
        tabs.forEach(t => t.classList.remove('active'));
        tab.classList.add('active');
        contents.forEach(c => c.classList.remove('active'));
        document.getElementById(role).classList.add('active');
    });
});
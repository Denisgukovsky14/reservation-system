async function loadComponent(selector, url) {
    try {
        const response = await fetch(url);
        const html = await response.text();
        document.querySelector(selector).innerHTML = html;
    } catch (error) {
        console.error("Ошибка загрузки компонента:", error);
    }
}

// Загружаем только футер (хедер теперь через renderHeader.js)
loadComponent("#footer-placeholder", "/src/components/footer/footer.html");


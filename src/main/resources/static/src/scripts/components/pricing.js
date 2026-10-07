function initPricingModal() {
    const modal = document.getElementById('pricingModal');
    const openBtn = document.getElementById('openPricing');
    const closeBtn = document.getElementById('closePricing');

    if (openBtn && modal) {
        openBtn.addEventListener('click', () => {
            modal.classList.add('active');
            document.body.classList.add('modal-open');
        });
    }

    if (closeBtn && modal) {
        closeBtn.addEventListener('click', () => {
            modal.classList.remove('active');
            document.body.classList.remove('modal-open');
        });
    }

    if (modal) {
        window.addEventListener('click', (e) => {
            if (e.target === modal) {
                modal.classList.remove('active');
                document.body.classList.remove('modal-open');
            }
        });
    }
}

// Автоматическая инициализация, если кнопка уже в DOM
if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', initPricingModal);
} else {
    initPricingModal();
}
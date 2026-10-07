// Обёртка над fetch: сама подставляет "Authorization: Bearer <токен>" из localStorage,
// если он там есть. Сигнатура такая же, как у обычного fetch — просто замени fetch( на apiFetch(.
// При 401 (нет валидного входа — не путать с 403 "это не твоё") чистит хранилище и кидает на логин.
function apiFetch(url, options = {}) {
    const token = localStorage.getItem('token');
    const headers = new Headers(options.headers || {});
    if (token) {
        headers.set('Authorization', `Bearer ${token}`);
    }
    return fetch(url, { ...options, headers }).then(response => {
        if (response.status === 401) {
            localStorage.removeItem('user');
            localStorage.removeItem('token');
            if (!location.pathname.includes('/pages/auth/')) {
                location.href = '/src/pages/auth/authorization.html';
            }
        }
        return response;
    });
}

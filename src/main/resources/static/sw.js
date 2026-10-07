// sw.js — Service Worker для «Дизайн-Хаба»

const CACHE_NAME = 'designhub-v1';
const STATIC_ASSETS = [
  '/',
  '/home',
  '/src/styles/main.css',
  '/src/scripts/utils/renderHeader.js',
  '/src/scripts/utils/loadComponents.js',
  // Добавь сюда все статические ресурсы: CSS, JS, иконки, шрифты
  // Например:
  // '/src/styles/pages/home.css',
  // '/src/scripts/pages/auth.js',
  // ...
  // Если у тебя много файлов, проще кэшировать динамически (см. ниже)
];

// Установка Service Worker — кэшируем статику
self.addEventListener('install', event => {
  event.waitUntil(
    caches.open(CACHE_NAME)
      .then(cache => {
        console.log('[SW] Кэширование статических ресурсов');
        return cache.addAll(STATIC_ASSETS);
      })
      .then(() => self.skipWaiting())
  );
});

// Активация — удаляем старые кэши
self.addEventListener('activate', event => {
  event.waitUntil(
    caches.keys().then(cacheNames => {
      return Promise.all(
        cacheNames.map(cache => {
          if (cache !== CACHE_NAME) {
            console.log('[SW] Удаление старого кэша:', cache);
            return caches.delete(cache);
          }
        })
      );
    })
    .then(() => self.clients.claim())
  );
});

// Перехват запросов
self.addEventListener('fetch', event => {
  const request = event.request;

  // Стратегия: для API-запросов — network-first (или network-only)
  if (request.url.includes('/api/')) {
    // API не кэшируем, всегда ходим в сеть
    event.respondWith(fetch(request).catch(() => {
      // Можно отдать заглушку для офлайн-режима, но обычно для API лучше выдать ошибку
      return new Response(JSON.stringify({ error: 'Нет соединения с сервером' }), {
        status: 503,
        headers: { 'Content-Type': 'application/json' }
      });
    }));
    return;
  }

  // Для статики — cache-first (сначала кэш, потом сеть)
  event.respondWith(
    caches.match(request)
      .then(cachedResponse => {
        if (cachedResponse) {
          return cachedResponse;
        }
        // Если в кэше нет — идём в сеть
        return fetch(request).then(response => {
          // Кэшируем новый ресурс
          const responseClone = response.clone();
          caches.open(CACHE_NAME).then(cache => {
            cache.put(request, responseClone);
          });
          return response;
        });
      })
  );
});
package com.sarcofuckusLesson.designhub;

// "Кто сейчас стучится" — то, что фильтр достаёт из проверенного токена.
// userType: "customer" или "contractor".
public record AuthenticatedUser(Long id, String userType) {}

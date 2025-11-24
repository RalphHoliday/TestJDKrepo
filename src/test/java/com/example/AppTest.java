package com.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Модульный тест для бизнес-метода greet(). Запускается Surefire-плагином Maven.
 * Результаты тестов будут собраны Jenkins и показаны в отчёте JUnit.
 */
class AppTest {

    /**
     * Базовый тест: проверяем, что greet("World") возвращает ожидаемое значение.
     */
    @Test
    void testGreet() {
        assertEquals("Hello, World!", App.greet("World"));
    }

    @Test
    void testGreetWithDifferentName() {
        assertEquals("Hello, Alice!", App.greet("Alice"));
        assertEquals("Hello, Bob!", App.greet("Bob"));
    }

    @Test
    void testGreetFormal() {
        assertEquals("Hello, Mr. Smith!", App.greetFormal("Mr.", "Smith"));
        assertEquals("Hello, Dr. Brown!", App.greetFormal("Dr.", "Brown"));
    }

    @Test
    void testGreetWithEmptyName() {
        assertEquals("Hello, !", App.greet(""));
    }

    @Test
    void testGreetWithNull() {
        assertEquals("Hello, null!", App.greet(null));
    }
}
package com.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Модульный тест для бизнес-метода greet(). Запускается Surefire-плагином Maven.
 * Результаты тестов будут собраны Jenkins и показаны в отчёте JUnit.
 */
class AppTest {

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
        assertEquals("Hello, Stranger!", App.greet(""));
        assertEquals("Hello, Stranger!", App.greet("   "));
    }

    @Test
    void testGreetWithNull() {
        assertEquals("Hello, Stranger!", App.greet(null));
    }

    @Test
    void testGreetFormalWithNull() {
        assertEquals("Hello!", App.greetFormal(null, "Smith"));
        assertEquals("Hello!", App.greetFormal("Mr.", null));
        assertEquals("Hello!", App.greetFormal(null, null));
    }
}
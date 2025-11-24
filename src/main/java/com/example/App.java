package com.example;

/**
 * Простейшее Java-приложение, которое демонстрирует бизнес-метод greet()
 * и точку входа main(). Этот класс попадёт в исполняемый JAR.
 */
public class App {

    /**
     * Точка входа. При запуске через `java -jar` будет выполнен метод main().
     * результат работы бизнес-метода greet().
     */
    public static void main(String[] args) {
        System.out.println("=== Java Maven CI/CD Demo ===");
        System.out.println(greet("World"));
        System.out.println(greetFormal("Mr.", "Smith"));
    }

    /**
     * Бизнес-метод: формирует приветствие.
     */
    public static String greet(String name) {
        if (name == null || name.trim().isEmpty()) {
            return "Hello, Stranger!";
        }
        return "Hello, " + name + "!";
    }

    /**
     * Дополнительный метод для демонстрации новых возможностей
     */
    public static String greetFormal(String title, String name) {
        if (title == null || name == null) {
            return "Hello!";
        }
        return "Hello, " + title + " " + name + "!";
    }
}
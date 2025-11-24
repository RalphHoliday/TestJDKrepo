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
        System.out.println(greet("World"));
    }

    /**
     * Бизнес-метод: формирует приветствие.
     */
    public static String greet(String name) {
        // Здесь могла бы быть любая логика --- валидация, обработка и т.п.
        return "Hello, " + name + "!";
    }

    /**
     * Дополнительный метод для демонстрации новых возможностей
     */
    public static String greetFormal(String title, String name) {
        return "Hello, " + title + " " + name + "!";
    }
}
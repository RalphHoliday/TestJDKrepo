/*
Declarative Pipeline для Jenkins.
Демонстрирует полный цикл: checkout → build → test → package → отчёты → (условный) деплой.
Работает как на локальном Jenkins, так и на CI-сервере.
*/

pipeline {
    // Где будет выполняться пайплайн. 'any' --- любой Jenkins-агент.
    agent any

    // Общие опции конвейера
    options {
        // Добавляет метки времени в лог сборки
        timestamps()
        // Обрезает историю сборок, хранит только последние 20
        buildDiscarder(logRotator(numToKeepStr: '20'))
        // Запрещает параллельные сборки одного job (полезно для локального демо)
        disableConcurrentBuilds()
    }

    // Переменные окружения для стадий
    environment {
        // Опции Maven: падать при ошибках тестов
        MAVEN_OPTS = '-Dmaven.test.failure.ignore=false'
    }

    // Триггеры запуска конвейера
    triggers {
        // Периодически проверяет SCM (репозиторий) на изменения каждые ~2 минуты
        pollSCM('H/2 * * * *')
        // Для продакшена лучше настраивать webhooks, но локально pollSCM удобнее.
    }

    stages {
        // Стадия для исправления возможных проблем с правами Git в Windows
        stage('Fix Git Ownership') {
            steps {
                bat 'git config --global --add safe.directory E:/repos/java-maven-ci-demo.git'
            }
        }

        stage('Checkout') {
            steps {
                // Извлечение кода из Git. Для локального демо используем file:// путь к bare-репозиторию.
                checkout([$class: 'GitSCM',
                    branches: [[name: '*/${BRANCH_NAME ?: "main"}']],
                    userRemoteConfigs: [[url: 'file:///E:/repos/java-maven-ci-demo.git']]
                ])
            }
        }

        stage('Build') {
            steps {
                // Проверка версии Maven
                bat 'mvn -v'
                // Компиляция проекта. Параметры:
                // -B: batch mode (без интерактивных вопросов)
                // -U: принудительно обновить снапшоты (на случай зависимостей)
                bat 'mvn -B -U clean compile'
            }
        }

        stage('Test') {
            steps {
                // Запуск модульных тестов (JUnit 5) и сбор покрытия JaCoCo
                bat 'mvn -B test'
            }
            post {
                // Действия после стадии Test
                always {
                    // Публикация результатов JUnit (Surefire создаёт XML-отчёты в target/surefire-reports)
                    junit '**/target/surefire-reports/*.xml'
                    // Публикация HTML-отчёта JaCoCo (генерируется в target/site/jacoco/index.html)
                    publishHTML([reportDir: 'target/site/jacoco',
                                reportFiles: 'index.html',
                                reportName: 'JaCoCo Coverage',
                                keepAll: true])
                }
            }
        }

        stage('Package') {
            steps {
                // Сборка исполняемого JAR. Артефакт будет в target/java-maven-ci-demo-1.0.0.jar
                bat 'mvn -B package'
            }
            post {
                success {
                    // Архивирование собранных артефактов в Jenkins (видны в UI job)
                    archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
                }
            }
        }

        stage('Quality gates') {
            // Стадия качества включается только для веток develop и main
            when {
                anyOf {
                    branch 'develop'
                    branch 'main'
                }
            }
            steps {
                // Здесь можно подключить статический анализ: SpotBugs, Checkstyle, SonarQube.
                echo 'Quality checks placeholder: линтеры, статанализ, SonarQube и т.д.'
                bat 'mvn -B checkstyle:checkstyle || echo "Checkstyle not configured, continuing..."'
            }
        }

        stage('Deploy (local)') {
            // Условный «деплой»: выполняем только для main
            when {
                branch 'main'
            }
            steps {
                script {
                    echo "Starting deployment for branch: ${env.BRANCH_NAME}"
                    // Останавливаем предыдущую версию приложения если запущена
                    bat 'taskkill /F /IM java.exe 2>nul || echo "No Java process to kill"'
                    // Запуск приложения в фоне и вывод логов в app.log
                    // В учебных целях это имитация CD (continuous deployment).
                    bat 'start /B java -jar target/java-maven-ci-demo-1.0.0.jar > app.log 2>&1'
                    echo "Application deployed successfully. Check app.log for output."
                }
            }
        }
    }

    // Глобальные действия после конвейера
    post {
        always {
            echo "Pipeline execution completed for ${env.BRANCH_NAME}"
            // Очистка: останавливаем приложение после завершения пайплайна
            bat 'taskkill /F /IM java.exe 2>nul || echo "Cleanup completed"'
        }
        success {
            echo "Build successful for ${env.BRANCH_NAME}"
            // Можно добавить уведомления: email, Slack и т.д.
        }
        failure {
            echo "Build failed for ${env.BRANCH_NAME}"
        }
        unstable {
            echo "Build unstable for ${env.BRANCH_NAME} - tests failed"
        }
    }
}
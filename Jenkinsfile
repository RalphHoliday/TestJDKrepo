/*
Declarative Pipeline для Jenkins.
Адаптирован для проекта в E:\Java\TestJDK
*/

pipeline {
    agent any

    options {
        timestamps()
        buildDiscarder(logRotator(numToKeepStr: '10'))
        disableConcurrentBuilds()
    }

    environment {
        MAVEN_OPTS = '-Dmaven.test.failure.ignore=false'
        PROJECT_DIR = 'E:\\Java\\TestJDK'
    }

    triggers {
        // Проверяем изменения каждые 3 минуты
        pollSCM('H/3 * * * *')
    }

    stages {
        stage('Checkout') {
            steps {
                script {
                    echo "Checking out from local repository: ${PROJECT_DIR}"
                    // Для локального проекта используем директорию напрямую
                    bat "xcopy \"${PROJECT_DIR}\\*\" \".\\\" /E /I /Y"
                }
            }
        }

        stage('Build') {
            steps {
                echo "Building project..."
                bat 'mvn -v'
                bat 'mvn -B -U clean compile'
            }
        }

        stage('Test') {
            steps {
                echo "Running tests..."
                bat 'mvn -B test'
            }
            post {
                always {
                    // Публикация результатов JUnit тестов
                    junit '**/target/surefire-reports/*.xml'
                    // Создаем простой отчет о тестах
                    bat 'echo === TEST RESULTS === > test-report.txt'
                    bat 'type target\\surefire-reports\\*.txt >> test-report.txt 2>nul || echo No test reports found >> test-report.txt'
                    archiveArtifacts artifacts: 'test-report.txt', fingerprint: true
                }
            }
        }

        stage('Package') {
            steps {
                echo "Packaging application..."
                bat 'mvn -B package -DskipTests'
            }
            post {
                success {
                    // Архивируем собранный JAR
                    archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
                    // Создаем информацию о сборке
                    bat 'echo Build: %BUILD_NUMBER% > build-info.txt'
                    bat 'echo Branch: %GIT_BRANCH% >> build-info.txt 2>nul || echo Branch: local >> build-info.txt'
                    bat 'echo Date: %DATE% %TIME% >> build-info.txt'
                    archiveArtifacts artifacts: 'build-info.txt', fingerprint: true
                }
            }
        }

        stage('Verify') {
            steps {
                echo "Verifying package..."
                script {
                    // Проверяем что JAR создан и работает
                    bat 'java -jar target/java-maven-ci-demo-1.0.0.jar --version 2>nul || java -jar target/java-maven-ci-demo-1.0.0.jar > verify-output.txt'
                    archiveArtifacts artifacts: 'verify-output.txt', fingerprint: true
                }
            }
        }

        stage('Demo Deploy') {
            when {
                expression {
                    // Выполняем для всех сборок в демо-целях
                    return true
                }
            }
            steps {
                echo "Demo deployment..."
                script {
                    // Просто демонстрируем что приложение работает
                    bat 'echo === DEMO DEPLOYMENT === > deployment.log'
                    bat 'echo Application would be deployed here >> deployment.log'
                    bat 'echo JAR file: target/java-maven-ci-demo-1.0.0.jar >> deployment.log'
                    bat 'echo Build completed successfully! >> deployment.log'
                    archiveArtifacts artifacts: 'deployment.log', fingerprint: true

                    // Показываем что приложение работает
                    bat 'java -jar target/java-maven-ci-demo-1.0.0.jar'
                }
            }
        }
    }

    post {
        always {
            echo "Pipeline execution completed for build: ${currentBuild.number}"
            // Очистка временных файлов
            bat 'del test-report.txt build-info.txt verify-output.txt deployment.log 2>nul || echo Cleanup completed'
        }
        success {
            echo "✅ Build ${currentBuild.number} completed successfully!"
            // Можно добавить уведомления здесь
        }
        failure {
            echo "❌ Build ${currentBuild.number} failed!"
        }
        unstable {
            echo "⚠️ Build ${currentBuild.number} is unstable - tests failed"
        }
    }
}
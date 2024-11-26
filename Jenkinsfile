pipeline {
    agent any

    environment {
        BUILD_DIR="target"
        WAR_FILE="TicketProject-1.0-SNAPSHOT.war"
        TOMCAT_WEBAPPS_DIR="/home/pavel/apache-tomcat/webapps"
        TOMCAT_BIN_DIR = "/home/pavel/apache-tomcat/bin"
        BACKUP_DIR="TicketProject/backups"
    }

    stages {
        stage('Checkout') {
            steps {
                echo "Клонирование репозитория..."
                // Клонируем репозиторий
                checkout scm
            }
        }

        stage('Build') {
            steps {
                echo "Сборка проекта..."
                // Сборка проекта с помощью Maven
                sh "mvn clean package"
            }
        }

        stage('Backup Current WAR') {
            steps {
                script {
                    // Проверяем, существует ли текущий WAR-файл
                    if (fileExists("${TOMCAT_WEBAPPS_DIR}/${WAR_FILE}")) {
                        echo "Создание резервной копии текущего WAR-файла..."
                        sh """
                            mkdir -p ${BACKUP_DIR}
                            cp ${TOMCAT_WEBAPPS_DIR}/${WAR_FILE} ${BACKUP_DIR}/${WAR_FILE}.\$(date +%Y%m%d%H%M%S)
                        """
                    } else {
                        echo "Резервное копирование не требуется, файл не найден."
                    }
                }
            }
        }

        stage('Stop Tomcat') {
            steps {
                echo "Остановка Tomcat..."
                // Останавливаем Tomcat
                sh "${TOMCAT_BIN_DIR}/shutdown.sh || true"
                sleep 5
            }
        }

        stage('Deploy New WAR') {
            steps {
                echo "Деплой нового WAR-файла..."
                // Копируем новый WAR-файл в директорию Tomcat
                sh """
                    cp ${BUILD_DIR}/${WAR_FILE} ${TOMCAT_WEBAPPS_DIR}/
                """
            }
        }

        stage('Clear Tomcat Cache') {
            steps {
                echo "Очистка временных файлов Tomcat..."
                // Удаляем временные файлы Tomcat
                sh """
                    rm -rf ${TOMCAT_WEBAPPS_DIR}/../work/*
                """
            }
        }

        stage('Start Tomcat') {
            steps {
                echo "Запуск Tomcat..."
                // Запускаем Tomcat
                sh "${TOMCAT_BIN_DIR}/startup.sh"
            }
        }
    }

    post {
        success {
            echo "Деплой завершен успешно!"
        }
        failure {
            echo "Деплой завершился с ошибкой."
        }
    }
}

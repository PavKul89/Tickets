pipeline {
    agent any

    environment {
        // Переменные среды
        DEPLOY_PATH = '/usr/share/tomcat9' // Путь к папке с приложениями Tomcat
        GIT_REPO = 'git@github.com:PavKul89/Tickets.git'
        BRANCH = 'main'
        APP_NAME = 'TicketProject-1.0-SNAPSHOT.war' // Имя вашего артефакта
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: "${BRANCH}", url: "${GIT_REPO}"
            }
        }

        stage('Build') {
            steps {
                sh 'mvn clean package'
            }
        }

        stage('Deploy to Tomcat') {
            steps {
                script {
                    // Удаление предыдущего артефакта
                    sh "rm -f ${DEPLOY_PATH}/${APP_NAME}"
                    // Копирование нового .war файла
                    sh "cp target/${APP_NAME} ${DEPLOY_PATH}/"
                }
            }
        }
    }

    post {
        success {
            echo 'Deployment successful!'
        }
        failure {
            echo 'Deployment failed!'
        }
    }
}

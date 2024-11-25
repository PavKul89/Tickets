pipeline {
    agent any

    environment {
        // Переменные среды
        DEPLOY_PATH = '/path/to/tomcat/webapps/' // Путь к папке с приложениями Tomcat
        GIT_REPO = 'https://github.com/your-repo/ticket.git'
        BRANCH = 'master'
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
                // Предполагается, что используется Maven
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

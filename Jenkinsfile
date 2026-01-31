pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                git branch: 'master', url: 'https://github.com/ashishmohit/Dockertest.git'
            }
        }

        stage('Build Docker Image') {
            steps {
                bat 'docker build -t javaapp .'
            }
        }

        stage('Run Tests') {
            steps {
                bat 'docker rm -f selenium || exit 0'
                bat 'docker rm -f javaapp || exit 0'
                bat 'docker run -d --name selenium -p 4444:4444 selenium/standalone-chromium:latest'
                bat 'timeout /t 15'
                bat 'docker run --name javaapp --rm --link selenium javaapp'
            }
        }
    }
}

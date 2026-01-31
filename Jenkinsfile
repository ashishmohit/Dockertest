pipeline {
    agent any

    stages {

        stage('Checkout Code') {
            steps {
                git branch: 'master',
                    url: 'https://github.com/ashishmohit/Dockertest.git'
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    bat 'docker build -t selenium/standalone-chromium:latest .'
                }
            }
        }

        stage('Run Tests in Docker') {
            steps {
                script {
                    bat 'docker run --name selenium --rm selenium/standalone-chromium:latest'
                }
            }
        }
    }
}

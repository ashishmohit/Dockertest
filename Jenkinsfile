pipeline {
    agent any

    stages {
        stage('Checkout Code') {
            steps {
                git branch: 'master', url: 'https://github.com/ashishmohit/Dockertest.git'
            }
        }

        stage('Run Tests with Docker Compose') {
            steps {
                script {
                    // Clean up previous runs
                    bat 'docker-compose down || exit 0'
                    bat 'docker rm -f selenium javaapp || exit 0'
                    
                    // Build and run with health checks
                    bat 'docker-compose up --build --abort-on-container-exit'
                }
            }
        }
    }

    post {
        always {
            bat 'docker-compose down || exit 0'
            bat 'docker system prune -f || exit 0'
        }
    }
}

pipeline {
    agent any

    stages {

        stage('Checkout Code') {
            steps {
                git branch: 'master',
                    url: 'https://github.com/ashishmohit/Dockertest.git'
            }
        }

        stage('Build Test Image') {
            steps {
                bat 'docker build -t javaapp .'
            }
        }

        stage('Run Selenium Grid & Tests') {
            steps {

                // Stop old containers
                bat 'docker rm -f selenium || exit 0'
                bat 'docker rm -f javaapp || exit 0'

                // Start Selenium browser
                bat 'docker run -d --name selenium -p 4444:4444 selenium/standalone-chromium:latest'

                // Wait for Selenium to come up
                bat 'timeout /t 15'

                // Run your Java Selenium tests
                bat 'docker run --name javaapp --rm --link selenium javaapp'
            }
        }
    }
}

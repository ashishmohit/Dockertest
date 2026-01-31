pipeline {
    agent any

    environment {
        SELENIUM_URL = "http://localhost:4444/wd/hub"
    }

    stages {

        stage('Checkout Code') {
            steps {
                git branch: 'master', url: 'https://github.com/ashishmohit/Dockertest.git'
            }
        }

        stage('Build Java Docker Image') {
            steps {
                bat 'docker build -t javaapp .'
            }
        }

        stage('Run Selenium & Java Tests') {
            steps {
                // Remove old containers if exist
                bat 'docker rm -f selenium || exit 0'
                bat 'docker rm -f javaapp || exit 0'

                // Start Selenium container in background
                bat 'docker run -d --name selenium -p 4444:4444 selenium/standalone-chromium:latest'

                // Wait for Selenium to be ready (localhost works on Windows)
                powershell """
                Write-Host 'Waiting for Selenium to start...'
                while ((Invoke-WebRequest ${env.SELENIUM_URL.replace('/wd/hub','')}/status -UseBasicParsing -ErrorAction SilentlyContinue) -eq \$null) {
                    Start-Sleep -Seconds 2
                }
                Write-Host 'Selenium is ready!'
                """

                // Run Java tests container
                bat 'docker run --rm --name javaapp --link selenium javaapp'
            }
        }
    }

    post {
        always {
            // Cleanup containers after pipeline
            bat 'docker rm -f selenium || exit 0'
            bat 'docker rm -f javaapp || exit 0'
        }
    }
}

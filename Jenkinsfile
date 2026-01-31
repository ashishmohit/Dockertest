pipeline {
    agent any

    environment {
        SELENIUM_URL = "http://selenium:4444"
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

                // Wait for Selenium to be ready (polling)
                bat '''
                powershell -Command "
                Write-Host 'Waiting for Selenium to start...';
                while((Invoke-WebRequest ${env.SELENIUM_URL}/status -UseBasicParsing -ErrorAction SilentlyContinue) -eq \$null) {
                    Start-Sleep -s 2
                };
                Write-Host 'Selenium is ready!';
                "
                '''

                // Run Java tests
                bat 'docker run --name javaapp --rm --link selenium javaapp'
            }
        }
    }

    post {
        always {
            // Cleanup containers
            bat 'docker rm -f selenium || exit 0'
            bat 'docker rm -f javaapp || exit 0'
        }
    }
}

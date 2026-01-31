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

                // Remove old containers
                bat 'docker rm -f selenium || exit 0'
                bat 'docker rm -f javaapp || exit 0'

                // Start Selenium container
                bat 'docker run -d --name selenium -p 4444:4444 selenium/standalone-chromium:latest'

                // Wait for Selenium to be ready (robust try/catch)
                powershell """
                Write-Host 'Waiting for Selenium to start...'
                \$seleniumReady = \$false
                while (-not \$seleniumReady) {
                    try {
                        \$resp = Invoke-WebRequest ${env.SELENIUM_URL.replace('/wd/hub','')}/status -UseBasicParsing -ErrorAction Stop
                        if (\$resp.StatusCode -eq 200) { \$seleniumReady = \$true }
                    } catch {
                        Write-Host 'Selenium not ready yet, waiting 2s...'
                        Start-Sleep -Seconds 2
                    }
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
            // Cleanup
            bat 'docker rm -f selenium || exit 0'
            bat 'docker rm -f javaapp || exit 0'
        }
    }
}

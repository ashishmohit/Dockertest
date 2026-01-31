pipeline {
    agent any

    environment {
        SELENIUM_CONTAINER = "selenium"
        JAVA_CONTAINER = "javaapp"
        SELENIUM_URL = "http://localhost:4444" // Use localhost for Windows Jenkins
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

                // Remove old containers if they exist
                bat 'docker rm -f %SELENIUM_CONTAINER% || exit 0'
                bat 'docker rm -f %JAVA_CONTAINER% || exit 0'

                // Start Selenium container in detached mode
                bat 'docker run -d --name %SELENIUM_CONTAINER% -p 4444:4444 selenium/standalone-chromium:latest'

                // Wait until Selenium is ready by polling localhost:4444/status
                powershell """
                Write-Host 'Waiting for Selenium to start...'

                \$seleniumReady = \$false
                \$timeout = 60  # max wait time in seconds
                \$elapsed = 0

                while (-not \$seleniumReady -and \$elapsed -lt \$timeout) {
                    try {
                        \$response = Invoke-WebRequest ${env.SELENIUM_URL}/status -UseBasicParsing -ErrorAction SilentlyContinue
                        if (\$response.StatusCode -eq 200) {
                            \$seleniumReady = \$true
                            Write-Host 'Selenium is ready!'
                            break
                        } else {
                            Write-Host 'Selenium not ready yet, waiting 2s...'
                            Start-Sleep -Seconds 2
                            \$elapsed += 2
                        }
                    } catch {
                        Write-Host 'Selenium not ready yet, waiting 2s...'
                        Start-Sleep -Seconds 2
                        \$elapsed += 2
                    }
                }

                if (-not \$seleniumReady) {
                    throw 'Selenium did not start in time!'
                }
                """

                // Run Java tests container, pointing to Selenium on localhost
                bat 'docker run --rm --name %JAVA_CONTAINER% -e SELENIUM_URL=${env.SELENIUM_URL} javaapp'
            }
        }
    }

    post {
        always {
            // Cleanup containers
            bat 'docker rm -f %SELENIUM_CONTAINER% || exit 0'
            bat 'docker rm -f %JAVA_CONTAINER% || exit 0'
        }
    }
}

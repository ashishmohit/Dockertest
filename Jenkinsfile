pipeline {
    agent any

    environment {
        SELENIUM_CONTAINER = "selenium"
        JAVA_CONTAINER = "javaapp"
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

                // Start Selenium container in background
                bat 'docker run -d --name %SELENIUM_CONTAINER% -p 4444:4444 selenium/standalone-chromium:latest'

                // Wait for Selenium to be ready by checking logs
                powershell """
                Write-Host 'Waiting for Selenium to start...'

                \$seleniumReady = \$false
                \$timeout = 60  # seconds
                \$elapsed = 0

                while (-not \$seleniumReady -and \$elapsed -lt \$timeout) {
                    try {
                        \$logs = docker logs %SELENIUM_CONTAINER% 2>&1
                        if (\$logs -match 'Selenium Server is up and running') {
                            \$seleniumReady = \$true
                            Write-Host 'Selenium is ready!'
                            break
                        } else {
                            Write-Host 'Selenium not ready yet, waiting 2s...'
                            Start-Sleep -Seconds 2
                            \$elapsed += 2
                        }
                    } catch {
                        Write-Host 'Error checking Selenium logs, retrying...'
                        Start-Sleep -Seconds 2
                        \$elapsed += 2
                    }
                }

                if (-not \$seleniumReady) {
                    throw 'Selenium did not start in time!'
                }
                """

                // Run Java tests container linked to Selenium
                bat 'docker run --rm --name %JAVA_CONTAINER% --link %SELENIUM_CONTAINER% javaapp'
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

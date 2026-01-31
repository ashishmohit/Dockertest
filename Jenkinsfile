pipeline {
    agent any

    environment {
        SELENIUM_URL = "http://localhost:4444"
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

                // Start Selenium container
                bat 'docker run -d --name selenium -p 4444:4444 selenium/standalone-chromium:latest'

                // Wait for Selenium to be ready
                powershell """
                Write-Host 'Waiting for Selenium to start...'
                \$seleniumReady = \$false
                \$timeout = 60
                \$elapsed = 0
                while (-not \$seleniumReady -and \$elapsed -lt \$timeout) {
                    try {
                        \$resp = Invoke-WebRequest http://localhost:4444/status -UseBasicParsing -ErrorAction SilentlyContinue
                        if (\$resp.StatusCode -eq 200) {
                            \$seleniumReady = \$true
                            Write-Host 'Selenium is ready!'
                            break
                        }
                    } catch {
                        Write-Host 'Selenium not ready yet, waiting 2s...'
                    }
                    Start-Sleep -Seconds 2
                    \$elapsed += 2
                }
                if (-not \$seleniumReady) {
                    throw 'Selenium did not start in time!'
                }
                """

                // Run Java tests container, pass Selenium URL
                bat 'docker run --rm --name javaapp -e SELENIUM_URL=http://host.docker.internal:4444 javaapp'
            }
        }
    }

    post {
        always {
            bat 'docker rm -f selenium || exit 0'
            bat 'docker rm -f javaapp || exit 0'
        }
    }
}

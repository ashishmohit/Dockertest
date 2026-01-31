pipeline {
    agent any

    environment {
        // Use network name so containers can communicate internally
        SELENIUM_URL = "http://selenium-hub:4444/wd/hub"
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

        stage('Setup Selenium Grid') {
            steps {
                // Remove old containers
                bat 'docker rm -f selenium-hub selenium-chrome || exit 0'

                // Create Docker network if it doesn't exist
                bat 'docker network inspect grid || docker network create grid'

                // Start Selenium Hub
                bat 'docker run -d --net grid --name selenium-hub -p 4444:4444 selenium/hub:latest'

                // Start Chrome node
                bat '''
                docker run -d --net grid --name selenium-chrome ^
                -e SE_EVENT_BUS_HOST=selenium-hub ^
                -e SE_EVENT_BUS_PUBLISH_PORT=4442 ^
                -e SE_EVENT_BUS_SUBSCRIBE_PORT=4443 ^
                selenium/node-chrome:latest
                '''

                // Wait for Hub to be ready
                powershell """
                Write-Host 'Waiting for Selenium Hub to start...'
                \$hubReady = \$false
                \$timeout = 60
                \$elapsed = 0
                while (-not \$hubReady -and \$elapsed -lt \$timeout) {
                    try {
                        \$resp = Invoke-WebRequest http://localhost:4444/status -UseBasicParsing -ErrorAction SilentlyContinue
                        if (\$resp.StatusCode -eq 200) {
                            \$hubReady = \$true
                            Write-Host 'Selenium Hub is ready!'
                            break
                        }
                    } catch {
                        Write-Host 'Selenium Hub not ready yet, waiting 2s...'
                    }
                    Start-Sleep -Seconds 2
                    \$elapsed += 2
                }
                if (-not \$hubReady) { throw 'Selenium Hub did not start in time!' }
                """
            }
        }

        stage('Run Java Tests') {
            steps {
                // Run Java app connected to the Selenium Hub via the same network
                bat 'docker run --rm --net grid --name javaapp -e SELENIUM_URL=http://selenium-hub:4444/wd/hub javaapp'
            }
        }
    }

    post {
        always {
            // Cleanup containers
            bat 'docker rm -f selenium-hub selenium-chrome javaapp || exit 0'
        }
    }
}

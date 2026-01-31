pipeline {
    agent any

    environment {
        SELENIUM_URL = "http://host.docker.internal:4444/wd/hub"
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

        stage('Start Selenium Grid') {
            steps {
                // Remove old containers
                bat 'docker rm -f selenium-hub selenium-chrome || exit 0'

                // Start hub
                bat 'docker run -d -p 4444:4444 --net grid --name selenium-hub selenium/hub:latest'

                // Start node
                bat 'docker run -d --net grid --name selenium-chrome --link selenium-hub:hub -e SE_EVENT_BUS_HOST=hub -e SE_EVENT_BUS_PUBLISH_PORT=4442 -e SE_EVENT_BUS_SUBSCRIBE_PORT=4443 selenium/node-chrome:latest'

                // Wait for hub to be ready
                powershell """
                Write-Host 'Waiting for Selenium Hub to start...'
                \$hubReady = \$false
                \$timeout = 60
                \$elapsed = 0
                while (-not \$hubReady -and \$elapsed -lt \$timeout) {
                    try {
                        \$resp = Invoke-WebRequest ${env.SELENIUM_URL}/status -UseBasicParsing -ErrorAction SilentlyContinue
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
                bat 'docker run --rm --name javaapp -e SELENIUM_URL=http://host.docker.internal:4444/wd/hub javaapp'
            }
        }
    }

    post {
        always {
            bat 'docker rm -f selenium-hub selenium-chrome javaapp || exit 0'
        }
    }
}

pipeline {
    agent {
        docker {
            image 'maven:3.9.5-eclipse-temurin-17'
            args '--privileged -v /var/run/docker.sock:/var/run/docker.sock'
        }
    }

    environment {
        DOCKER_HOST = "unix:///var/run/docker.sock"
    }

    stages {
        stage('Build & Test') {
            steps {
                script {

                    sh 'docker --version'
                    sh 'docker run --rm alpine:3.14 echo "Docker daemon radi"'

                    // Build i test sa TestContainers
                    sh 'mvn clean compile test -Dtest=DockerCheckTest'
                }
            }
            post {
                success {
                    echo "✅ SVE PROŠLO: Docker-in-Docker i TestContainers rade u pipeline-u"
                }
                failure {
                    echo "❌ NEŠTO NE RADI: Proveri Docker daemon i DinD konfiguraciju"
                }
            }
        }
    }
}
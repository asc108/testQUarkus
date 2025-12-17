pipeline {
    agent {
        kubernetes {
            yaml '''
apiVersion: v1
kind: Pod
spec:
  containers:
  - name: maven
    image: maven:3.9.5-eclipse-temurin-17
    command: ["cat"]
    tty: true
    volumeMounts:
    - mountPath: /var/run/docker.sock
      name: docker-sock
  volumes:
  - name: docker-sock
    hostPath:
      path: /var/run/docker.sock
'''
        }
    }

    environment {
        DOCKER_HOST = "unix:///var/run/docker.sock"
        TESTCONTAINERS_HOST_OVERRIDE = "docker-host"
    }

    stages {
        stage('Docker Check') {
            steps {
                container('maven') {
                    script {
                        echo "🔍 Proveravam Docker instalaciju..."
                        sh 'docker --version'
                        sh 'docker info | grep -i "containers\|server"'

                        echo "🚀 Testiram osnovni Docker container..."
                        sh 'docker run --rm alpine:3.14 echo "✅ Docker daemon radi"'
                    }
                }
            }
        }

        stage('Build & Test') {
            steps {
                container('maven') {
                    script {
                        echo "🏗️ Build Quarkus projekta..."
                        sh 'mvn clean compile'

                        echo "🧪 Pokrećem TestContainers test..."
                        // -Dtest za specifičan test, ili sve testove
                        sh 'mvn test -Dtest=DockerCheckTest'
                    }
                }
            }
            post {
                success {
                    echo "✅ SVE PROŠLO: TestContainers radi u pipeline-u!"
                }
                failure {
                    echo "❌ NEŠTO NE RADI: Proveri Docker konfiguraciju"
                    sh 'docker ps -a' // debug info
                }
            }
        }
    }
}
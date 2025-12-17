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
    command: ['cat']
    tty: true
    resources:
      requests:
        cpu: "500m"
        memory: "1Gi"
      limits:
        cpu: "1"
        memory: "2Gi"
    volumeMounts:
    - name: docker-sock
      mountPath: /var/run/docker.sock
    - name: maven-cache
      mountPath: /root/.m2
  volumes:
  - name: docker-sock
    hostPath:
      path: /var/run/docker.sock
      type: File
  - name: maven-cache
    emptyDir: {}
'''
        }
    }

    environment {
        DOCKER_HOST = "unix:///var/run/docker.sock"
        TESTCONTAINERS_HOST_OVERRIDE = "docker-host"
        MAVEN_OPTS = "-Dmaven.repo.local=/root/.m2/repository"
    }

    stages {
        stage('Clone Repository') {
            steps {
                git branch: 'main',
                    url: 'URL_VAŠEG_GIT_REPO_A',
                    credentialsId: 'VAŠ_CREDENTIAL_ID'
            }
        }

        stage('Docker Daemon Check') {
            steps {
                container('maven') {
                    script {
                        echo "🔍 Proveravam Docker daemon..."
                        sh """
                            docker version
                            echo "---"
                            docker info --format '{{.ServerVersion}}'
                            echo "---"
                        """

                        sh '''
                            docker run --rm alpine:3.14 echo "✅ Docker-in-Docker radi u pod-u"
                        '''
                    }
                }
            }
        }

        stage('Build & TestContainers Test') {
            steps {
                container('maven') {
                    script {
                        echo "🏗️ Building Quarkus project..."
                        sh 'mvn clean compile -DskipTests'

                        echo "🧪 Running TestContainers test..."
                        sh 'mvn test -Dtest=DockerCheckTest'
                    }
                }
            }

            post {
                success {
                    echo "✅ SVE PROŠLO! TestContainers radi u K8s pipeline-u."
                    echo "Docker-in-Docker konfiguracija je ispravna."
                }
                failure {
                    echo "❌ TestContainers test pao!"
                    echo "Proveriti:"
                    echo "1. Da li hostPath /var/run/docker.sock postisti na node-u"
                    echo "2. Da li Jenkins service account ima dozvole"
                    echo "3. Da li je Docker instaliran na K8s node-ovima"

                    container('maven') {
                        sh '''
                            echo "=== DEBUG INFO ==="
                            ls -la /var/run/docker.sock || true
                            docker ps -a || true
                            whoami
                            id
                        '''
                    }
                }
            }
        }
    }

    post {
        always {
            echo "🧹 Cleanup completed"
        }
    }
}
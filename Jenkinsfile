pipeline {
    agent {
        kubernetes {
            label 'quarkus-test-${UUID.randomUUID()}'
            yaml '''
apiVersion: v1
kind: Pod
spec:
  containers:
  - name: maven-docker
    image: maven:3.9.6-eclipse-temurin-21
    command: ["cat"]
    tty: true
    securityContext:
      privileged: true  # ⚠️ Potrebno za Docker
    volumeMounts:
    - name: docker-sock
      mountPath: /var/run/docker.sock
    env:
    - name: DOCKER_HOST
      value: unix:///var/run/docker.sock
  volumes:
  - name: docker-sock
    hostPath:
      path: /var/run/docker.sock
'''
        }
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
                container('maven-docker') {
                    sh '''
                        echo "=== Current directory ==="
                        pwd
                        ls -la
                        echo ""

                        echo "=== Check Docker ==="
                        docker version
                        echo ""

                        echo "=== Check Maven ==="
                        mvn --version
                        echo ""

                        echo "=== Check docker-compose ==="
                        which docker-compose || pip install docker-compose
                    '''
                }
            }
        }

        stage('Simple Test - Just Maven') {
            steps {
                container('maven-docker') {
                    sh '''
                        echo "=== 1. Try to compile first ==="
                        mvn clean compile -DskipTests
                        echo "✅ Compile successful"

                        echo "=== 2. Check if tests exist ==="
                        find . -name "*Test.java" -type f
                    '''
                }
            }
        }

        stage('Test WITHOUT Docker Compose') {
            steps {
                container('maven-docker') {
                    sh '''
                        echo "=== Running tests WITHOUT external dependencies ==="
                        # Probaj samo unit testove
                        mvn test -Dtest=PersonResourceTest 2>&1 | tail -100
                    '''
                }
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                }
            }
        }
    }

    post {
        always {
            echo "=== Pipeline finished ==="
        }
    }
}
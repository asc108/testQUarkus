pipeline {
    agent {
        kubernetes {
            yaml '''
apiVersion: v1
kind: Pod
spec:
  containers:
  - name: builder
    image: maven:3.9.5-eclipse-temurin-17
    command: ['sleep']
    args: ['infinity']
    volumeMounts:
    - name: docker-sock
      mountPath: /var/run/docker.sock
  - name: docker
    image: docker:24.0-cli
    command: ['sleep']
    args: ['infinity']
    volumeMounts:
    - name: docker-sock
      mountPath: /var/run/docker.sock
    securityContext:
      privileged: true
  volumes:
  - name: docker-sock
    emptyDir: {}
'''
        }
    }
    
    stages {
        stage('Test DinD') {
            steps {
                container('docker') {
                    script {
                        echo "=== DOCKER CHECK ==="
                        sh '''
                            docker version
                            docker run --rm alpine:3.14 echo "Docker CLI works"
                        '''
                    }
                }
                
                container('builder') {
                    script {
                        echo "=== BUILD & TEST ==="
                        sh '''
                            # Install docker CLI in maven container
                            apt-get update && apt-get install -y docker.io curl
                            docker --version
                            
                            # Test Docker connection
                            docker run --rm alpine:3.14 echo "Docker works with Maven"
                            
                            # Run TestContainers test
                            mvn clean test -Dtest=DockerCheckTest
                        '''
                    }
                }
            }
        }
    }
}

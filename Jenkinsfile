pipeline {
    agent {
        kubernetes {
            yaml '''
apiVersion: v1
kind: Pod
spec:
  containers:
  - name: test
    image: maven:3.9.5-eclipse-temurin-17
    command: ['sleep']
    args: ['infinity']
    volumeMounts:
    - name: docker-sock
      mountPath: /var/run/docker.sock
  volumes:
  - name: docker-sock
    hostPath:
      path: /var/run/docker.sock
'''
        }
    }
    
    stages {
        stage('Test DinD') {
            steps {
                container('test') {
                    sh '''
                        # 1. Docker check
                        echo "=== DOCKER CHECK ==="
                        docker version
                        
                        # 2. Simple container test
                        echo "=== CONTAINER TEST ==="
                        docker run --rm alpine:3.14 echo "Docker works in K8s pod"
                        
                        # 3. TestContainers test
                        echo "=== TESTCONTAINERS TEST ==="
                        mvn clean test -Dtest=DockerCheckTest -q
                        
                        echo "✅ SUCCESS: DinD ready for pipeline!"
                    '''
                }
            }
        }
    }
}

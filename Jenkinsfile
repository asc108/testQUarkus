pipeline {
    agent {
        label 'dind-agent'
    }
    stages {
        stage('Full TestContainers Sanity Check') {
            steps {
                container('maven') {
                    script {
                        // KORAK 0: INSTALIRAJ DOCKER CLI (OBVEZNO)
                        echo "=== 0. Installing Docker CLI ==="
                        sh 'apt-get update && apt-get install -y docker.io'
                        
                        echo "=== 1. Quick Docker Check ==="
                        sh 'docker run --rm alpine:3.14 echo "Docker daemon ready"'
                        
                        echo "=== 2. Run Maven Test with TestContainers ==="
                        sh './mvnw clean test -Dtest=DockerCheckTest -B'
                    }
                }
            }
            post {
                success {
                    echo "✅🎉 POTPUN USPEH! TestContainers radi."
                }
                failure {
                    echo "⚠️ TestContainers test pao."
                }
            }
        }
    }
}

pipeline {
    agent {
        label 'dind-agent'
    }
    stages {
        stage('Full Setup Check') {
            steps {
                container('maven') {
                    script {
                        // 1. INSTALL DOCKER CLI
                        echo "=== INSTALL DOCKER CLI ==="
                        sh '''
    apt-get update
    apt-get install -y docker.io
    docker --version
'''
                        '''
                        
                        // 2. VERIFY MAVEN
                        echo "=== VERIFY MAVEN ==="
                        sh 'mvn --version'
                        
                        // 3. TEST DOCKER -> DINd CONNECTION (Crucial)
                        echo "=== TEST DOCKER DAEMON CONNECTION ==="
                        sh '''
                            docker version
                            echo "✅ DinD daemon connection verified (Client + Server versions shown)."
                        '''
                    }
                }
            }
        }
    }
}

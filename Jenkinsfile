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
                            apt-get update && apt-get install -y curl lsb-release ca-certificates
                            mkdir -p /etc/apt/keyrings
                            curl -fsSL https://download.docker.com/linux/debian/gpg -o /etc/apt/keyrings/docker.asc
                            chmod a+r /etc/apt/keyrings/docker.asc
                            echo "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.asc] \
                            https://download.docker.com/linux/debian $(lsb_release -cs) stable" | tee /etc/apt/sources.list.d/docker.list > /dev/null
                            apt-get update && apt-get install -y docker-ce-cli
                            docker --version
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

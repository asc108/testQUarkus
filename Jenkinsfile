pipeline {
    agent {
        label 'dind-agent'
    }
    stages {
        stage('Final Sanity Check') {
            steps {
                container('maven') {
                    script {
                        // 1. Install Docker CLI (Ubuntu-compatible method)
                        echo "=== INSTALL DOCKER CLI ==="
                        sh '''
                            apt-get update && apt-get install -y docker.io
                            docker --version
                        '''

                        // 2. Verify Maven
                        echo "=== VERIFY MAVEN ==="
                        sh 'mvn --version'

                        // 3. CRITICAL: Test Docker -> DinD Connection
                        echo "=== TEST DOCKER DAEMON CONNECTION ==="
                        sh '''
                            docker version
                            echo "✅ DinD daemon connection verified."
                        '''
                        
                        // 4. Quick TestContainers Debug
                        echo "=== QUICK TESTCONTAINERS DEBUG ==="
                        sh '''
                            # Show the environment TestContainers will see
                            echo "DOCKER_HOST=$DOCKER_HOST"
                            # Set the socket override to prevent socket checks
                            export TESTCONTAINERS_DOCKER_SOCKET_OVERRIDE="/var/run/docker.sock"
                            echo "TESTCONTAINERS_DOCKER_SOCKET_OVERRIDE=$TESTCONTAINERS_DOCKER_SOCKET_OVERRIDE"
                        '''
                    }
                }
            }
            post {
                success {
                    echo "✅ MAJOR MILESTONE: DinD environment is fully ready."
                    echo "Next step: Run the actual TestContainers test."
                }
                failure {
                    echo "❌ Setup failed. Check the Docker CLI installation step."
                }
            }
        }
    }
}

pipeline {
    agent {
        label 'dind-agent'
    }
    stages {
        stage('Test DinD API Compatibility') {
            steps {
                container('maven') {
                    script {
                        // Instalacija Docker CLI (svaki put u fresh container-u)
                        sh 'apt-get update && apt-get install -y docker.io'
                        
                        // Provera da li DinD prihvata API verziju 1.40
                        sh '''
                            echo "=== Testing Docker API version ==="
                            # Ova komanda će pokazati koju API verziju daemon podržava
                            DOCKER_API_VERSION=1.40 docker version --format '{{.Client.APIVersion}} {{.Server.APIVersion}}' 2>&1 || true
                            echo "---"
                            
                            # Standardna provera
                            docker version
                            echo "✅ DinD ready with API compatibility"
                        '''
                    }
                }
            }
        }
        
        stage('Run TestContainers Test') {
            steps {
                container('maven') {
                    script {
                        echo "=== Running TestContainers Test ==="
                        sh '''
                            # Postavi environment varijable
                            export DOCKER_HOST="tcp://localhost:2375"
                            export TESTCONTAINERS_DOCKER_SOCKET_OVERRIDE="/var/run/docker.sock"
                            export TESTCONTAINERS_DEBUG="true"
                            
                            echo "DOCKER_HOST=$DOCKER_HOST"
                            mvn clean test -Dtest=UserResourceTest -B -e
                        '''
                    }
                }
            }
            post {
                success {
                    echo "✅🎉 KONAČAN USPEH! TestContainers radi sa DinD-om!"
                }
                failure {
                    echo "❌ Test pao. Proverite TestContainers debug logove."
                }
            }
        }
    }
}

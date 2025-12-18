pipeline {
    agent {
        label 'dind-agent'
    }
    
    options {
        // Timeout za ceo pipeline - spreči zaglavljivanje
        timeout(time: 30, unit: 'MINUTES')
        
        // Ne čuvaj previše buildova
        buildDiscarder(logRotator(numToKeepStr: '10'))
    }
    
    stages {
        stage('Setup') {
            steps {
                container('maven') {
                    script {
                        // Instalacija Docker CLI
                        sh 'apt-get update && apt-get install -y docker.io'
                        
                        // Provera DinD konekcije
                        sh '''
                            echo "=== Docker Environment ==="
                            docker version
                            echo "=== DinD Health Check ==="
                            docker info | grep -E 'Containers|Images|Server Version'
                        '''
                    }
                }
            }
        }
        
        stage('Build and Test') {
            steps {
                container('maven') {
                    script {
                        sh '''
                            # TestContainers konfiguracija
                            export DOCKER_HOST="tcp://localhost:2375"
                            export TESTCONTAINERS_RYUK_DISABLED="true"
                            
                            # Kreiraj testcontainers.properties
                            mkdir -p src/test/resources
                            cat > src/test/resources/testcontainers.properties << 'EOF'
ryuk.container.image=
checks.disable=true
EOF
                            
                            echo "Starting Maven build and test..."
                            mvn clean test -B -e \
                                -Dtestcontainers.ryuk.disabled=true
                        '''
                    }
                }
            }
        }
    }
    
    post {
        always {
            container('maven') {
                script {
                    echo "=== Post-build Cleanup ==="
                    sh '''
                        # Prikaz stanja pre čišćenja
                        echo "Containers before cleanup:"
                        docker ps -a --format "table {{.ID}}\t{{.Image}}\t{{.Status}}" || true
                        
                        # Zaustavi sve TestContainers containere
                        echo "Stopping TestContainers..."
                        docker ps -aq --filter "label=org.testcontainers=true" | xargs -r docker stop || true
                        
                        # Obriši sve TestContainers containere
                        echo "Removing TestContainers..."
                        docker ps -aq --filter "label=org.testcontainers=true" | xargs -r docker rm -f || true
                        
                        # Obriši neiskorišćene volumes (čuva disk space)
                        echo "Pruning volumes..."
                        docker volume prune -f || true
                        
                        # Obriši neiskorišćene networks
                        echo "Pruning networks..."
                        docker network prune -f || true
                        
                        # Prikaz stanja posle čišćenja
                        echo "Containers after cleanup:"
                        docker ps -a --format "table {{.ID}}\t{{.Image}}\t{{.Status}}" || true
                        
                        echo "Disk usage:"
                        docker system df || true
                    '''
                }
            }
        }
        success {
            echo "✅ Build successful! Tests passed with DinD setup."
        }
        failure {
            echo "❌ Build failed. Check logs above for details."
        }
    }
}

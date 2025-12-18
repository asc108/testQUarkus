pipeline {
    agent {
        label 'dind-agent'
    }

    stages {
        stage('Install Tools & Check Docker') {
            steps {
                container('maven') {
                    script {
                        echo "=== 1. INSTALLING DOCKER CLI & MAVEN ==="
                        sh '''
                            apt-get update
                            apt-get install -y docker.io maven netcat-openbsd
                            echo "--- Versions ---"
                            docker --version
                            mvn --version
                        '''

                        echo "=== 2. QUICK DOCKER CHECK ==="
                        sh 'docker run --rm alpine:3.14 echo "✅ Docker CLI -> DinD connection WORKS"'
                    }
                }
            }
        }

        stage('Debug TestContainers Environment') {
            steps {
                container('maven') {
                    script {
                        echo "=== 3. DEBUG: DOCKER ENVIRONMENT ==="
                        sh '''
                            echo "--- Current DOCKER_HOST env var ---"
                            echo "DOCKER_HOST=$DOCKER_HOST"
                            echo ""
                            echo "--- Checking port 2375 on localhost ---"
                            if nc -zv localhost 2375 2>/dev/null; then
                                echo "✅ Port 2375 is OPEN and reachable"
                            else
                                echo "❌ Cannot reach localhost:2375"
                            fi
                            echo ""
                            echo "--- Testing Docker connection via CLI ---"
                            docker info --format '{{.ServerVersion}}' && echo "✅ Docker daemon responds" || echo "❌ Docker daemon not responding"
                        '''
                    }
                }
            }
        }

        stage('Run TestContainers Test') {
    steps {
        container('maven') {
            script {
                echo "=== 4. RUNNING TESTCONTAINERS TEST (WITH DEBUG) ==="
                sh '''
    # KLJUČNO: Konfiguracija za TestContainers sa debug logovima
    export DOCKER_HOST="tcp://localhost:2375"
    export TESTCONTAINERS_DOCKER_SOCKET_OVERRIDE="/var/run/docker.sock"
    export TESTCONTAINERS_HOST_OVERRIDE="localhost"
    export TESTCONTAINERS_DEBUG="true"  # <-- DODAJ OVO
    
    echo "TestContainers config:"
    echo "  DOCKER_HOST=$DOCKER_HOST"
    echo "  TESTCONTAINERS_DEBUG=$TESTCONTAINERS_DEBUG"
    
    # Pokreni test i sačuvaj detaljne logove
    mvn clean test -Dtest=UserResourceTest -B -e 2>&1 | tee mvn-test-output.log
'''
            }
        }
    }
}
    }

    post {
        always {
            echo "=== PIPELINE FINISHED ==="
        }
    }
}

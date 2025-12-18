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
                        echo "=== 4. RUNNING TESTCONTAINERS TEST ==="
                        sh '''
    # KLJUČNO: Konfiguracija za TestContainers da koristi samo TCP
    export DOCKER_HOST="tcp://localhost:2375"
    export TESTCONTAINERS_DOCKER_SOCKET_OVERRIDE="/var/run/docker.sock"
    export TESTCONTAINERS_HOST_OVERRIDE="localhost"
    
    echo "TestContainers config:"
    echo "  DOCKER_HOST=$DOCKER_HOST"
    echo "  TESTCONTAINERS_DOCKER_SOCKET_OVERRIDE=$TESTCONTAINERS_DOCKER_SOCKET_OVERRIDE"
    echo "  TESTCONTAINERS_HOST_OVERRIDE=$TESTCONTAINERS_HOST_OVERRIDE"
    
    mvn clean test -Dtest=UserResourceTest -B -e
'''
                    }
                }
            }
            post {
                success {
                    echo "✅🎉 POTPUN USPEH! TestContainers test sa PostgreSQL-om radi u DinD pipeline-u!"
                }
                failure {
                    echo "❌ Test failed. Check the 'Run TestContainers Test' stage logs for details."
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

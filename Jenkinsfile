pipeline {
    agent {
        label 'dind-agent'
    }

    stages {
        stage('1. Basic DinD Check') {
    steps {
        container('maven') {
            script {
                echo "=== 1a. INSTALL DOCKER CLI ==="
                sh '''
                    apt-get update && apt-get install -y curl
                    curl -fsSL "https://download.docker.com/linux/static/stable/x86_64/docker-29.1.3.tgz" -o docker.tgz
                    tar -xzf docker.tgz --strip-components=1 -C /usr/local/bin docker/docker
                    rm docker.tgz
                    docker --version
                '''
                
                echo "=== 1b. TEST DOCKER DAEMON ==="
                sh '''
                    # ISPRAVLJENO: koristite docker:cli umesto docker:30-cli
                    docker run --rm docker:cli docker version
                    echo "✅ DinD daemon RESPONDS"
                '''
            }
        }
    }
}

        stage('2. Configure TestContainers') {
            steps {
                container('maven') {
                    script {
                        echo "=== 2. SET TESTCONTAINERS ENV VARS ==="
                        sh '''
                            export DOCKER_HOST="tcp://localhost:2375"
                            export TESTCONTAINERS_DOCKER_SOCKET_OVERRIDE="/var/run/docker.sock"
                            export TESTCONTAINERS_HOST_OVERRIDE="localhost"
                            export TESTCONTAINERS_DOCKER_MACHINE_OVERRIDE="true"
                            
                            echo "CONFIGURATION:"
                            echo "DOCKER_HOST=$DOCKER_HOST"
                            echo "TESTCONTAINERS_DOCKER_SOCKET_OVERRIDE=$TESTCONTAINERS_DOCKER_SOCKET_OVERRIDE"
                            echo "TESTCONTAINERS_HOST_OVERRIDE=$TESTCONTAINERS_HOST_OVERRIDE"
                            echo "TESTCONTAINERS_DOCKER_MACHINE_OVERRIDE=$TESTCONTAINERS_DOCKER_MACHINE_OVERRIDE"
                            
                            java -cp ".:*" org.testcontainers.dockerclient.DockerClientProviderStrategy 2>&1 | head -30 || true
                        '''
                    }
                }
            }
        }

        stage('3. Run TestContainers Test') {
            steps {
                container('maven') {
                    script {
                        echo "=== 3. RUN TEST ==="
                        sh '''
                            export DOCKER_HOST="tcp://localhost:2375"
                            export TESTCONTAINERS_DOCKER_SOCKET_OVERRIDE="/var/run/docker.sock"
                            export TESTCONTAINERS_HOST_OVERRIDE="localhost"
                            export TESTCONTAINERS_DOCKER_MACHINE_OVERRIDE="true"
                            export TESTCONTAINERS_DEBUG="true"
                            
                            echo "🚀 Running mvn test with TestContainers..."
                            # PROMENA OVDE: koristite ' jednostruke navodnike za pattern
                            mvn clean test -Dtest=UserResourceTest -B 2>&1 | grep -A 20 -B 5 'TESTCONTAINERS'
                        '''
                    }
                }
            }
            post {
                success {
                    echo "✅🎉 SANITY CHECK COMPLETE!"
                }
                failure {
                    echo "❌ Final check failed."
                }
            }
        }
    }
}

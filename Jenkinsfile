pipeline {
    agent {
        label 'dind-agent'
    }

    stages {
        // KORAK 1: Osnovna provera - da li DinD i Docker CLI rade
        stage('1. Basic DinD Check') {
            steps {
                container('maven') {
                    script {
                        echo "=== 1a. INSTALL DOCKER CLI ==="
                        sh '''
                            apt-get update && apt-get install -y curl
                            # Preuzmi Docker CLI koji MATCH verziju DinD-a (29.1.3)
                            curl -fsSL "https://download.docker.com/linux/static/stable/x86_64/docker-29.1.3.tgz" -o docker.tgz
                            tar -xzf docker.tgz --strip-components=1 -C /usr/local/bin docker/docker
                            rm docker.tgz
                            docker --version
                        '''
                        
                        echo "=== 1b. TEST DOCKER DAEMON ==="
                        sh '''
                            # Ova komanda će FAIL-ovati ako DinD ne radi
                            docker run --rm docker:30-cli docker version
                            echo "✅ DinD daemon RESPONDS"
                        '''
                    }
                }
            }
        }

        // KORAK 2: Provera TestContainers konfiguracije - DODAJEMO JEDNU VAŽNU VARIJABLU
        stage('2. Configure TestContainers') {
            steps {
                container('maven') {
                    script {
                        echo "=== 2. SET TESTCONTAINERS ENV VARS ==="
                        sh '''
                            # OVE TRI VARIJABLE SU KLJUČNE za DinD
                            export DOCKER_HOST="tcp://localhost:2375"
                            export TESTCONTAINERS_DOCKER_SOCKET_OVERRIDE="/var/run/docker.sock"
                            export TESTCONTAINERS_HOST_OVERRIDE="localhost"
                            
                            # OVA ČETVRTA JE NOVA I VAŽNA - isključuje provjeru za docker-machine
                            export TESTCONTAINERS_DOCKER_MACHINE_OVERRIDE="true"
                            
                            echo "CONFIGURATION:"
                            echo "DOCKER_HOST=$DOCKER_HOST"
                            echo "TESTCONTAINERS_DOCKER_SOCKET_OVERRIDE=$TESTCONTAINERS_DOCKER_SOCKET_OVERRIDE"
                            echo "TESTCONTAINERS_HOST_OVERRIDE=$TESTCONTAINERS_HOST_OVERRIDE"
                            echo "TESTCONTAINERS_DOCKER_MACHINE_OVERRIDE=$TESTCONTAINERS_DOCKER_MACHINE_OVERRIDE"
                            
                            # Test: pokušaj da direktno pozoveš TestContainers da proveri Docker
                            java -cp ".:*" org.testcontainers.dockerclient.DockerClientProviderStrategy 2>&1 | head -30 || true
                        '''
                    }
                }
            }
        }

        // KORAK 3: Pokretanje testa SA SVIM VARIJABLAMA
        stage('3. Run TestContainers Test') {
            steps {
                container('maven') {
                    script {
                        echo "=== 3. RUN TEST ==="
                        sh '''
                            # PONOVO POSTAVI SVE VARIJABLE (zbog sigurnosti)
                            export DOCKER_HOST="tcp://localhost:2375"
                            export TESTCONTAINERS_DOCKER_SOCKET_OVERRIDE="/var/run/docker.sock"
                            export TESTCONTAINERS_HOST_OVERRIDE="localhost"
                            export TESTCONTAINERS_DOCKER_MACHINE_OVERRIDE="true"
                            export TESTCONTAINERS_DEBUG="true"
                            
                            echo "🚀 Running mvn test with TestContainers..."
                            mvn clean test -Dtest=UserResourceTest -B 2>&1 | grep -A 20 -B 5 "TESTCONTAINERS\|Could not find"
                        '''
                    }
                }
            }
            post {
                success {
                    echo "✅🎉 SANITY CHECK COMPLETE: TestContainers works in DinD!"
                }
                failure {
                    echo "❌ Final check failed. Need to examine TestContainers debug logs."
                }
            }
        }
    }
}

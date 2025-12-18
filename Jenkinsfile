pipeline {
    agent {
        label 'dind-agent'
    }
    stages {
        stage('Run TestContainers Test') {
            steps {
                container('maven') {
                    script {
                        echo "=== INSTALL DOCKER CLI ==="
                        sh 'apt-get update && apt-get install -y docker.io'
                        
                        echo "=== RUN TESTCONTAINERS TEST ==="
                        sh '''
                            # Set ALL required environment variables for TestContainers
                            export DOCKER_HOST="tcp://localhost:2375"
                            export TESTCONTAINERS_DOCKER_SOCKET_OVERRIDE="/var/run/docker.sock"
                            export TESTCONTAINERS_HOST_OVERRIDE="localhost"
                            export TESTCONTAINERS_DOCKER_MACHINE_OVERRIDE="true"
                            export TESTCONTAINERS_DEBUG="true"
                            
                            echo "TestContainers Environment:"
                            echo "DOCKER_HOST=$DOCKER_HOST"
                            
                            # Run the test and capture ALL output
                            mvn clean test -Dtest=UserResourceTest -B -e
                        '''
                    }
                }
            }
            post {
                success {
                    echo "✅🎉 ULTIMATE SUCCESS! TestContainers works in Jenkins DinD pipeline!"
                }
                failure {
                    echo "⚠️ Test failed. Check Maven/TestContainers logs above."
                }
            }
        }
    }
}

pipeline {
    agent {
        label 'dind-agent'
    }
    stages {
        stage('Full TestContainers Sanity Check') {
            steps {
                container('maven') {
                    script {
                        echo "=== 0. Installing Tools ==="
                        sh '''
                            apt-get update
                            apt-get install -y docker.io maven
                            docker --version
                            mvn --version
                        '''

                        echo "=== 1. Quick Docker Check ==="
                        sh 'docker run --rm alpine:3.14 echo "Docker daemon ready"'

                        echo "=== 2. Run Maven Test with TestContainers ==="
                        // PROMENA OVDE: Koristite 'UserResourceTest'
                        sh 'mvn clean test -Dtest=UserResourceTest -B'
                    }
                }
            }
            post {
                success {
                    echo "✅🎉 POTPUN USPEH! DinD + TestContainers rade sa realnim PostgreSQL-om."
                }
                failure {
                    echo "⚠️ Test pao."
                }
            }
        }
    }
}
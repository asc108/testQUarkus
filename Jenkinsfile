pipeline {
    agent {
        label 'dind-agent'
    }
    stages {
        stage('Full TestContainers Sanity Check') {
            steps {
                container('maven') {
                    script {
                        // KORAK 0: INSTALIRAJ POTREBNE ALATE
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
                        // Koristi 'mvn' jer nemamo Maven Wrapper u ovom test projektu
                        sh 'mvn clean test -Dtest=DockerCheckTest -B'
                    }
                }
            }
            post {
                success {
                    echo "✅🎉 POTPUN USPEH! DinD + TestContainers rade."
                    echo "Sada možete da primenite ovaj Pod Template na REALNE projekte vaše organizacije."
                    echo "Oni će koristiti './mvnw' i imaće već podešene dozvole."
                }
                failure {
                    echo "⚠️ Test pao. Proverite TestContainers testni kod."
                }
            }
        }
    }
}

pipeline {
    agent {
        label 'dind-agent'
    }
    stages {
        stage('Full TestContainers Sanity Check') {
            steps {
                container('maven') {
                    script {
                        echo "=== 1. Quick Docker Check ==="
                        sh 'docker run --rm alpine:3.14 echo "Docker daemon ready"'
                        
                        echo "=== 2. Run Maven Test with TestContainers ==="
                        // Ovo pokreće vaš test koji koristi TestContainers
                        sh './mvnw clean test -Dtest=DockerCheckTest -B'
                    }
                }
            }
            post {
                success {
                    echo "✅🎉 POTPUN USPEH! TestContainers radi u Jenkins DinD pipeline-u. Vaš setup je spreman za projekte sa integracionim testovima (Postgres, itd.)."
                }
                failure {
                    echo "⚠️ TestContainers test pao. Proverite: 1) da li je testni kod ispravan, 2) da li TestContainers može da povuče potrebne Docker image-e (npr. postgres:13)."
                }
            }
        }
    }
}

pipeline {
    agent { label 'dind-agent' }
    stages {
        stage('Test') {
            steps {
                container('maven') {
                    sh '''
                        mvn --version
                        docker --version
                    '''
                }
            }
        }
    }
}

pipeline {
    agent any
    
    stages {
        stage('Build') {
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }
        
        stage('Test with Testcontainers') {
            steps {
                sh 'mvn test'
            }
        }
        
        stage('Build Docker Image') {
            steps {
                sh 'docker build -t simple-quarkus:latest .'
            }
        }
        
        stage('Test Docker') {
            steps {
                sh '''
                    docker-compose up -d
                    sleep 10
                    curl -f http://localhost:8080/users || exit 1
                    docker-compose down
                '''
            }
        }
    }
    
    post {
        always {
            junit '**/target/surefire-reports/*.xml'
            cleanWs()
        }
    }
}
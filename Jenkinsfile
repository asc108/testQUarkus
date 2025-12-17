pipeline {
    agent any
    
    environment {
        DOCKER_REGISTRY = 'your-registry.com'
        IMAGE_NAME = 'quarkus-demo'
        IMAGE_TAG = "${BUILD_NUMBER}"
        // Maven ce koristiti system Maven ili Maven wrapper
        MAVEN_OPTS = '-Dmaven.repo.local=.m2/repository'
    }
    
    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }
        
        stage('Build') {
            steps {
                sh './mvnw clean package -DskipTests'
            }
        }
        
        stage('Test') {
            steps {
                sh './mvnw test'
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                }
            }
        }
        
        stage('Build Docker Image') {
            steps {
                script {
                    sh """
                        docker build -t ${IMAGE_NAME}:${IMAGE_TAG} .
                        docker tag ${IMAGE_NAME}:${IMAGE_TAG} ${IMAGE_NAME}:latest
                    """
                }
            }
        }
        
        stage('Push to Registry') {
            when {
                branch 'main'
            }
            steps {
                script {
                    sh """
                        docker tag ${IMAGE_NAME}:${IMAGE_TAG} ${DOCKER_REGISTRY}/${IMAGE_NAME}:${IMAGE_TAG}
                        docker push ${DOCKER_REGISTRY}/${IMAGE_NAME}:${IMAGE_TAG}
                    """
                }
            }
        }
        
        stage('Deploy') {
            when {
                branch 'main'
            }
            steps {
                sh 'docker-compose up -d'
            }
        }
    }
    
    post {
        always {
            cleanWs()
        }
        success {
            echo 'Pipeline completed successfully!'
        }
        failure {
            echo 'Pipeline failed!'
        }
    }
}
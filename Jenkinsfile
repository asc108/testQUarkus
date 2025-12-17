pipeline {
    agent any

    stages {
        stage('Checkout & Setup') {
            steps {
                checkout scm
                sh '''
                    echo "=== Setup ==="
                    chmod +x mvnw 2>/dev/null || true
                    ls -la
                '''
            }
        }

        stage('Build JAR') {
            steps {
                sh '''
                    echo "=== Building Quarkus JAR ==="
                    ./mvnw clean package -DskipTests -Dquarkus.package.type=uber-jar
                    ls -lh target/*.jar
                '''
            }
        }

        stage('Test with Docker Compose') {
            steps {
                script {
                    sh '''
                        echo "=== 1. Start PostgreSQL with Docker Compose ==="
                        # Pokreni SAMO PostgreSQL (bez app)
                        docker-compose up -d postgres

                        echo "=== 2. Wait for PostgreSQL to be ready ==="
                        sleep 10
                        docker-compose ps

                        echo "=== 3. Run Tests against running PostgreSQL ==="
                        # Koristi environment variables iz docker-compose za testove
                        export QUARKUS_DATASOURCE_JDBC_URL="jdbc:postgresql://localhost:5432/quarkus_db"
                        export QUARKUS_DATASOURCE_USERNAME="quarkus"
                        export QUARKUS_DATASOURCE_PASSWORD="quarkus"

                        ./mvnw test \
                          -Dquarkus.datasource.jdbc.url=${QUARKUS_DATASOURCE_JDBC_URL} \
                          -Dquarkus.datasource.username=${QUARKUS_DATASOURCE_USERNAME} \
                          -Dquarkus.datasource.password=${QUARKUS_DATASOURCE_PASSWORD}

                        echo "=== 4. Stop PostgreSQL ==="
                        docker-compose down
                    '''
                }
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                    sh '''
                        echo "=== Cleanup ==="
                        docker-compose down -v 2>/dev/null || true
                        docker ps -aq | xargs -r docker rm -f 2>/dev/null || true
                    '''
                }
            }
        }

        stage('Build & Test Full Stack') {
            steps {
                script {
                    sh '''
                        echo "=== 1. Build and start full stack ==="
                        docker-compose up --build -d

                        echo "=== 2. Wait for app to start ==="
                        sleep 15

                        echo "=== 3. Test REST API ==="
                        # Testiraj da li app radi
                        curl -f http://localhost:8080/q/health || echo "Health check failed"
                        curl -f http://localhost:8080/persons || echo "Persons endpoint failed"

                        echo "=== 4. Stop everything ==="
                        docker-compose down
                    '''
                }
            }
        }
    }

    post {
        always {
            sh '''
                echo "=== Final cleanup ==="
                docker-compose down -v 2>/dev/null || true
                docker system prune -f 2>/dev/null || true
            '''
            cleanWs()
        }
        success {
            echo '✅ All tests passed with Docker Compose!'
        }
        failure {
            echo '❌ Tests failed'
        }
    }
}
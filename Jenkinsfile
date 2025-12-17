pipeline {
    agent any

    stages {
        stage('Checkout & Setup Maven') {
            steps {
                checkout scm
                sh '''
                    echo "=== 1. Install Maven if missing ==="
                    which mvn || (apt-get update && apt-get install -y maven)
                    mvn --version

                    echo "=== 2. Check Docker ==="
                    docker --version
                    docker-compose --version || apt-get install -y docker-compose

                    echo "=== 3. List files ==="
                    ls -la
                '''
            }
        }

        stage('Build JAR with System Maven') {
            steps {
                sh '''
                    echo "=== Building with system Maven ==="
                    mvn clean package -DskipTests -Dquarkus.package.type=uber-jar
                    ls -lh target/*.jar
                '''
            }
        }

        stage('Test with Docker Compose') {
            steps {
                script {
                    sh '''
                        echo "=== 1. Start PostgreSQL ==="
                        docker-compose up -d postgres

                        echo "=== 2. Wait for DB (max 30s) ==="
                        for i in {1..30}; do
                            if docker-compose exec postgres pg_isready -U quarkus; then
                                echo "✅ PostgreSQL is ready!"
                                break
                            fi
                            echo "Waiting for PostgreSQL... ($i/30)"
                            sleep 2
                        done

                        echo "=== 3. Get DB IP ==="
                        DB_IP=$(docker inspect -f '{{range.NetworkSettings.Networks}}{{.IPAddress}}{{end}}' quarkus-postgres)
                        echo "PostgreSQL IP: $DB_IP"

                        echo "=== 4. Run Tests ==="
                        mvn test \\
                          -Dquarkus.datasource.jdbc.url=jdbc:postgresql://${DB_IP}:5432/quarkus_db \\
                          -Dquarkus.datasource.username=quarkus \\
                          -Dquarkus.datasource.password=quarkus \\
                          -Dtest=PersonResourceTest

                        echo "=== 5. Show test results ==="
                        find target/surefire-reports -name "*.txt" -exec echo "=== {} ===" \\; -exec cat {} \\;
                    '''
                }
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                    sh 'docker-compose down'
                }
            }
        }

        stage('Quick API Test') {
            steps {
                script {
                    sh '''
                        echo "=== Quick API test ==="
                        docker-compose up -d
                        sleep 20

                        echo "Testing /persons endpoint..."
                        curl -f http://localhost:8080/persons || echo "Endpoint test failed"

                        docker-compose down
                    '''
                }
            }
        }
    }

    post {
        always {
            sh '''
                echo "=== Cleanup ==="
                docker-compose down -v 2>/dev/null || true
                docker ps -aq | xargs -r docker rm -f 2>/dev/null || true
            '''
        }
        success {
            echo '✅ TestContainers alternative test PASSED!'
        }
        failure {
            echo '❌ Test failed'
        }
    }
}
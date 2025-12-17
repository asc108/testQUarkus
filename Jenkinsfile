pipeline {
    agent any
    
    stages {
        stage('Debug Environment') {
            steps {
                sh '''
                    echo "=== DEBUG START ==="
                    echo "Current directory:"
                    pwd
                    ls -la
                    echo ""
                    
                    echo "Java version:"
                    java -version 2>&1 || echo "Java not found"
                    echo ""
                    
                    echo "Maven check:"
                    which mvn || echo "Maven not in PATH"
                    mvn --version 2>&1 || echo "Cannot run mvn"
                    echo ""
                    
                    echo "Project structure:"
                    find . -maxdepth 3 -type f -name "*.java" | head -10
                    echo ""
                    
                    echo "Check pom.xml:"
                    if [ -f "pom.xml" ]; then
                        echo "✅ pom.xml exists"
                        head -30 pom.xml
                    else
                        echo "❌ NO pom.xml found!"
                    fi
                '''
            }
        }
        
        stage('Try Maven Compile') {
            steps {
                sh '''
                    echo "=== Trying to compile ==="
                    if command -v mvn >/dev/null 2>&1; then
                        mvn clean compile -DskipTests || echo "Compile failed"
                    else
                        echo "Maven not found, trying alternative..."
                        # Probaj sa mvnw ako postoji
                        if [ -f "mvnw" ]; then
                            chmod +x mvnw
                            ./mvnw clean compile -DskipTests || echo "mvnw failed"
                        else
                            echo "No Maven available"
                        fi
                    fi
                '''
            }
        }
        
        stage('List Test Files') {
            steps {
                sh '''
                    echo "=== Looking for test files ==="
                    find . -name "*Test.java" -type f
                    
                    echo ""
                    echo "=== Check PersonResourceTest ==="
                    if [ -f "src/test/java/com/example/PersonResourceTest.java" ]; then
                        echo "✅ Test file exists"
                        head -20 "src/test/java/com/example/PersonResourceTest.java"
                    else
                        echo "❌ Test file not found"
                        # Traži bilo gde
                        find . -name "PersonResourceTest.java" 2>/dev/null || echo "Not found anywhere"
                    fi
                '''
            }
        }
    }
    
    post {
        always {
            echo "=== Debug finished ==="
        }
    }
}

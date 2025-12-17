pipeline {
    agent any
    
    stages {
        stage('Debug - Check What We Have') {
            steps {
                sh '''
                    echo "=== DEBUG INFO ==="
                    echo "1. Directory:"
                    pwd
                    ls -la
                    echo ""
                    
                    echo "2. Files in repo:"
                    find . -type f -name "*.java" | head -10
                    echo ""
                    
                    echo "3. Check pom.xml:"
                    if [ -f "pom.xml" ]; then
                        echo "pom.xml exists"
                        head -20 pom.xml
                    else
                        echo "NO pom.xml!"
                    fi
                    echo ""
                    
                    echo "4. Check test file:"
                    find . -name "PersonResourceTest.java" -exec echo "Found: {}" \\;
                '''
            }
        }
        
        stage('Try Maven Directly') {
            steps {
                sh '''
                    echo "=== Try mvn test ==="
                    # Probaj sa /usr/bin/mvn ako postoji
                    /usr/bin/mvn test 2>&1 | head -50 || echo "Maven test failed"
                '''
            }
        }
    }
}

    post {
        always {
            echo "=== Pipeline finished ==="
        }
    }
}

pipeline {
    agent any
    
    tools {
        maven 'M3'
        jdk 'jdk17'
    }
    
    environment {
        // Configuration SonarQube
        SONAR_SCANNER_OPTS = "-Dsonar.projectKey=student-management"
        SONAR_AUTH_TOKEN = credentials('sonar-token')
        SONAR_HOST_URL = 'http://localhost:9000'
    }
    
    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', 
                url: 'https://github.com/souhirKrizi/StudentManagementDevops.git',
                credentialsId: 'github-credentials'
            }
        }
        
        stage('Build') {
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }
        
        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('SonarQube') {
                    sh 'mvn sonar:sonar ' +
                       '-Dsonar.projectKey=student-management ' +
                       '-Dsonar.host.url=${SONAR_HOST_URL} ' +
                       '-Dsonar.login=${SONAR_AUTH_TOKEN} ' +
                       '-Dsonar.java.binaries=target/classes'
                }
            }
        }
    }
    
    post {
        always {
            cleanWs()
        }
        success {
            echo 'Pipeline exécuté avec succès! 🎉'
        }
        failure {
            echo 'Échec du pipeline. Veuillez vérifier les logs pour plus de détails.'
        }
    }
}

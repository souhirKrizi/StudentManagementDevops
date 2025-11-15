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

    }
    
    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', 
                url: 'https://github.com/souhirKrizi/StudentManagementDevops.git',
                credentialsId: 'github-credentials' // À configurer dans Jenkins
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
                    sh 'mvn sonar:sonar -Dsonar.login=${SONAR_AUTH_TOKEN} -Dsonar.projectKey=student-management -Dsonar.java.binaries=target/classes'
                }
            }
        }
    }
    
    post {
        always {
            // Nettoyage de l'espace de travail
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

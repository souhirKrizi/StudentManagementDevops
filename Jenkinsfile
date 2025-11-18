pipeline {
    agent any

    tools {
        maven 'M3'
        jdk 'jdk17'
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main',
                    url: 'https://github.com/souhirKrizi/StudentManagementDevops.git'
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

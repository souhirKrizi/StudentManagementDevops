pipeline {
    agent any

    environment {
        DOCKERHUB_CREDENTIALS = credentials('docker-hub-credentials')
        DOCKER_IMAGE = 'souhirkrizi2002/studentmanagement'
        DOCKER_TAG = "${env.BUILD_NUMBER}"
    }

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

        stage('Build') {
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Test') {
            steps {
                sh 'mvn test'
            }
            post {
                always {
                    junit '**/target/surefire-reports/**/*.xml'
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    docker.withRegistry('https://registry.hub.docker.com', 'docker-hub-credentials') {
                        def customImage = docker.build("${env.DOCKER_IMAGE}:${env.DOCKER_TAG}")
                        customImage.push()
                        customImage.push('latest')
                    }
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
            echo "Image Docker disponible: ${env.DOCKER_IMAGE}:${env.DOCKER_TAG}"
        }
        failure {
            echo 'Échec du pipeline. Veuillez vérifier les logs pour plus de détails.'
        }
    }
}

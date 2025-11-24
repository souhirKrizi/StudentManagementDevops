pipeline {
    agent any

    environment {
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

        stage('Build and Push Docker Image') {
            environment {
                DOCKER_CREDS = credentials('StudentManagement')
            }
            steps {
                script {
                    sh "docker build -t ${env.DOCKER_IMAGE}:${env.DOCKER_TAG} ."
                    sh "echo ${DOCKER_CREDS_PSW} | docker login -u ${DOCKER_CREDS_USR} --password-stdin"
                    sh "docker tag ${env.DOCKER_IMAGE}:${env.DOCKER_TAG} ${env.DOCKER_IMAGE}:latest"
                    sh "docker push ${env.DOCKER_IMAGE}:${env.DOCKER_TAG}"
                    sh "docker push ${env.DOCKER_IMAGE}:latest"
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

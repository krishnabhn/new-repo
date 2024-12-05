pipeline {
    agent any
    environment {
        dockerHubCredentials = 'docker-hub-credentials'
        githubCredentials = 'github-credentials'
    }
    tools {
        jdk 'openJDK8'
        maven 'maven3'
    }

    stages {
        stage('SCM Checkout') {
            steps {
                git branch: 'main',
                    credentialsId: githubCredentials,
                    url: 'https://github.com/krishnabhn/example.git'
            }
        }
        stage('Maven Build') {
            steps {
                sh "mvn clean install"
            }
        }
        stage('Docker Build & Push') {
            steps {
                script {
                    withDockerRegistry(credentialsId: dockerHubCredentials, toolName: 'docker') {
                        sh "docker build -t krishnabhanu998/jenkins:v1 ."
                        sh "docker push krishnabhanu998/jenkins:v1"
                    }
                }
            }
        }
    }
}
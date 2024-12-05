pipeline {
    agent {
        label 'master'
    }
    stages {
    stage('SCM Checkout') {
            environment {
                githubCredentials = 'github-credentials'
            }
            steps {
                script {
                    git branch: 'main', changelog: false, credentialsId: '8ddf803e-6307-49a8-b6a3-6ae08a3e3cb2', poll: false, url: 'https://github.com/krishnabhn/example.git'
                }
            }
        }
    
        stage('Maven Build') {
            steps {
                sh 'mvn clean install'
            }
        }
        stage('Docker Build & Push') {
            steps {
                script {
                    withCredentials([usernamePassword(credentialsId: 'docker-hub-credentials', usernameVariable: 'DOCKER_USERNAME', passwordVariable: 'DOCKER_PASSWORD')]) {
                        sh '''docker login -u ${DOCKER_USERNAME} -p ${DOCKER_PASSWORD}
                        docker build -t krishnabhanu998/jenkins:${BUILD_NUMBER} .
                        docker push krishnabhanu998/jenkins:${BUILD_NUMBER}'''
                    }
                }
            }
        }
    }
}
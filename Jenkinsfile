pipeline {
    agent any

    options {
        timestamps()
        disableConcurrentBuilds()
        skipDefaultCheckout(true)
    }

    parameters {
        booleanParam(
            name: 'PUSH_IMAGES',
            defaultValue: false,
            description: 'Publier les images Docker dans le registre configure'
        )
        string(
            name: 'DOCKER_REGISTRY',
            defaultValue: 'docker.io',
            description: 'Registre Docker, par exemple docker.io'
        )
        string(
            name: 'IMAGE_NAMESPACE',
            defaultValue: 'codingfactory',
            description: 'Organisation ou utilisateur du registre Docker'
        )
    }

    environment {
        MAVEN_OPTS = '-Dmaven.repo.local=.m2/repository'
        IMAGE_TAG = "${BUILD_NUMBER}"
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Backend CI') {
            steps {
                script {
                    if (isUnix()) {
                        sh 'mvn -B clean test package'
                    } else {
                        bat 'mvn -B clean test package'
                    }
                }
            }
        }

        stage('Frontend CI') {
            steps {
                script {
                    if (isUnix()) {
                        sh 'cd frontend && npm ci && npm run test:ci && npm run build'
                    } else {
                        bat 'cd frontend && npm ci && npm run test:ci && npm run build'
                    }
                }
            }
        }

        stage('CD - Build Docker images') {
            when {
                branch 'main'
            }
            steps {
                script {
                    def imageNames = [
                        'eureka-server',
                        'chatbot-service',
                        'pfe-service',
                        'api-gateway',
                        'frontend'
                    ]

                    imageNames.each { imageName ->
                        def image = "${params.DOCKER_REGISTRY}/${params.IMAGE_NAMESPACE}/${imageName}:${env.IMAGE_TAG}"
                        if (isUnix()) {
                            sh "docker build --pull -t ${image} ${imageName}"
                        } else {
                            bat "docker build --pull -t ${image} ${imageName}"
                        }
                    }
                }
            }
        }

        stage('CD - Push Docker images') {
            when {
                allOf {
                    branch 'main'
                    expression { params.PUSH_IMAGES }
                }
            }
            steps {
                withCredentials([
                    usernamePassword(
                        credentialsId: 'docker-registry-credentials',
                        usernameVariable: 'DOCKER_USERNAME',
                        passwordVariable: 'DOCKER_PASSWORD'
                    )
                ]) {
                    script {
                        def registry = params.DOCKER_REGISTRY
                        def namespace = params.IMAGE_NAMESPACE
                        def imageNames = [
                            'eureka-server',
                            'chatbot-service',
                            'pfe-service',
                            'api-gateway',
                            'frontend'
                        ]

                        if (isUnix()) {
                            sh "echo \$DOCKER_PASSWORD | docker login ${registry} -u \$DOCKER_USERNAME --password-stdin"
                            imageNames.each { imageName ->
                                sh "docker push ${registry}/${namespace}/${imageName}:${env.IMAGE_TAG}"
                            }
                            sh 'docker logout ' + registry
                        } else {
                            bat "echo %DOCKER_PASSWORD%| docker login ${registry} -u %DOCKER_USERNAME% --password-stdin"
                            imageNames.each { imageName ->
                                bat "docker push ${registry}/${namespace}/${imageName}:${env.IMAGE_TAG}"
                            }
                            bat "docker logout ${registry}"
                        }
                    }
                }
            }
        }
    }

    post {
        always {
            junit allowEmptyResults: true, testResults: '**/target/surefire-reports/*.xml'
            archiveArtifacts allowEmptyArchive: true, artifacts: '**/target/*.jar,frontend/dist/**', fingerprint: true
        }
        success {
            echo 'Pipeline CI/CD terminee avec succes.'
        }
        failure {
            echo 'La pipeline a echoue. Consulter les logs de l etape en erreur.'
        }
    }
}

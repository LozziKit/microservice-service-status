 pipeline {
    agent any
    stages {
       stage('Build') {
           steps {
                dir (path: "./") {
                    sh './build'
                }
           }
       }
       stage('Redeploy') {
           steps {
                dir (path: "./topology/") {
                    sh 'docker-compose down --volumes'
                    sh 'docker-compose up --build -d'
                }
           }
       }

       stage('API tests') {
           steps {
               dir (path: "./topology/"){
                    sh './wait-for-it.sh -h localhost -p 8080 -t 30'
               }
               dir (path: "./service-users-specs/") {
                    sh 'mvn clean install'
               }
               echo 'Test results are available on Probe Dock: https://trial.probedock.io/avaliasystems/openaffectserver'
           }
       }

       stage('Undeploy') {
            steps {
                dir (path: "./topology/") {
                    sh 'docker-compose down --volumes'
                }
            }
       }

       stage('Validation') {
           steps {
               echo 'Test results are available on Probe Dock: https://trial.probedock.io/avaliasystems/openaffectserver'
           }
       }
    }
}

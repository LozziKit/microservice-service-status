 pipeline {
    agent any
    stages {
       stage('Test') {
           steps {
                dir (path: "./scripts") {
                    sh './test'
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

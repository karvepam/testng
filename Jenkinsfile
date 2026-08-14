
node {

    stage('Checkout') {

        echo 'Checking out code'

        checkout scm
    }

    stage('Build') {

        echo 'Building Project'

        bat 'mvn clean compile'
    }

    stage('Test') {

        echo 'Running Tests'

        bat 'mvn test'
    }

    stage('Package') {

        echo 'Packaging Application'

        bat 'mvn package'
    }

    stage('Deploy') {

        if(params.ENVIRONMENT == 'staging') {

            echo 'Deploying to Staging'
        }

        if(params.ENVIRONMENT == 'production') {

            echo 'Deploying to Production'
        }
    }
}

# Akka Demo Application

Simple implementation of Akka Actors and Akka Cluster

## Build

- `mvn clean install`  - command to build the project


## Run the App as Single Actor System

- `java -jar target/akka-demo-app-1.0-uber.jar` - command to run the app

## Send helloworld message to the App

-'curl -X GET http://localhost:8095/message/helloworld'


## Run the App as Clustered Actor System - Cluster 1

## Run the Cluster 1 (Seed Node)

- `java -Dconfig.resource=application-cluster.conf -jar target/akka-demo-app-1.0-uber.jar` - command to start the seed node

## Run the App as Clustered Actor System - Cluster 2

- `java -Dconfig.resource=application-cluster.conf -Dnetty_port=2522 -Dhttp_port=8096 -jar target/akka-demo-app-1.0-uber.jar` - command to start node 2 joining the seed node


## Send helloworld message to the Cluster App, verify the request to sent to the each cluster in round robin

-'curl -X GET http://localhost:8095/message/helloworld'

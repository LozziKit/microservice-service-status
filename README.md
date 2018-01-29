# microservice-service-status
## Introduction
This is the repository of the Service Status microservice included in the [LozziKit toolkit](https://github.com/LozziKit). It provides an interface to view the status of various services and check their uptime for the last 30 days.

## Quick-start
You can quickly deploy the server by executing the rightly named `deploy` script.
Based on this logic, the function of the `test` script is easily guessed.
For Windows and OS X users, the `test_win_osx` script will ajust the test URL to the default docker-machine ip : `192.168.99.100` rather than `localhost`. 

If you wish to setup a development environment, you can use your favorite IDE in conjunction with a `microservice` MySQL database that should have been populated with the 
following quartz script : `/topology/images/mysql/docker-entrypoint-initdb.d/quartz.sql`

For now, launching the server without Docker will make it configure itself with the `application.properties` file, found in the resources of the application.
The Docker layer simply overrides a few configuration property such as the datasource url (connecting to "adjacent machines" rather than localhost).

## Authors
- Benoît Gianinetti
- Colin Lavanchy
- Edward Ransome
- Michaël Spierer

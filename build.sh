#/usr/bin/bash

# Build server jar but skip unit test
(cd service-status-server; mvn -Dmaven.test.skip=true install)

docker build service-status-server -t amt/status-server

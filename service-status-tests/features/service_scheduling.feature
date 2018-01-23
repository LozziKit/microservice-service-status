Feature: Check the scheduled services are being contacted correctly

  Background:
    Given I have a service Server running

  Scenario: Added services shall be tracked accordingly
    Given I have the following services:
      |  name  |           url              |       description      |interval|
      | Google |http://mockserver:8080/google|Service google standard |    5   |
      |  myFTP | http://mockserver:8080/ftp  | Serveur ftp classique  |    5   |
      |ssh@home| http://mockserver:8080/ssh  |  Serveur ssh normal    |    5   |
    And they're added to the server for tracking
    Then they're supposed to have been contacted after their delay

  Scenario: Changing a service's delay should update its next check
    When a service's delay is changed
    Then its next trigger's fire time shall be updated

  Scenario: Removing a service should clear all further check
    When a service is deleted
    Then the scheduler shouldn't have any trace of future check

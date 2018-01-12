Feature: Check the scheduled services are being contacted correctly

  Background:
    Given there is a Service server
    And the server is up and running
    And I have a service checker
    And I have multiples services:
      | id | response | delay  |        url      |
      |  1 |    200   |   15   |  /test/service1 |
      |  2 |    503   |   5    |  /test/service2 |
      |  3 |    404   |   32   |  /test/service3 |
      |  4 |    513   |   60   |  /test/service4 |
    And the server is setup to answer accordingly
    And the service checker scheduled all tasks

  Scenario: Check all scheduled services are set to be contacted based on delay
    When each task shall be scheduled correctly
    Then each task shall be executed after their delay
    Then the server can be shutdown

  Scenario: Changing a service's delay should update its next check
    When a service's delay is changed
    Then its next trigger's fire time shall be updated
    Then the server can be shutdown

  Scenario: Removing a service should clear all further check
    When a service is deleted
    Then the scheduler shouln't have any trace of future check
    Then the server can be shutdown


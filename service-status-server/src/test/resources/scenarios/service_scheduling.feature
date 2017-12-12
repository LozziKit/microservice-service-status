Feature: Check the scheduled services are being contacted correctly

  Background:
    Given there is a Service server
    And the server is up and running
    And I have a service checker
    And I have multiples services:
      | id | response | delay |        url       |
      |  1 |    200   |   15   |  /test/service1 |
      |  2 |    503   |   5   |  /test/service2 |
      |  3 |    404   |   32   |  /test/service3 |
      |  4 |    513   |   60   |  /test/service4 |
    And the server is setup to answer accordingly

  Scenario: Check all scheduled services are set to be contacted based on delay
    When the service checker scheduled all tasks
    Then each task shall be scheduled correctly
    And each task shall be executed after their delay
    Then the server can be shutdown
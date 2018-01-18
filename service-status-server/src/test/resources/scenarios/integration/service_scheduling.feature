Feature: Check the scheduled services are being contacted correctly

  Background:
    Given I have a service Server
    And I have multiples services:
      | id | response | delay  |        url      |
      |  1 |    200   |   15   |  /test/service1 |
      |  2 |    503   |   5    |  /test/service2 |
      |  3 |    404   |   32   |  /test/service3 |
      |  4 |    513   |   58   |  /test/service4 |
    And the service checker scheduled all tasks

  Scenario: Check all scheduled services are set to be contacted based on delay
    Then each task shall be scheduled correctly

  Scenario: Changing a service's delay should update its next check
    When a service's delay is changed
    Then its next trigger's fire time shall be updated
    And the service checker is reset

  Scenario: Removing a service should clear all further check
    When a service is deleted
    Then the scheduler shouldn't have any trace of future check
    And the service checker is reset

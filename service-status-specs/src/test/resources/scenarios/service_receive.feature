Feature: Check we can receive Services

  Background:
    Given there is a Service server
    And I have a Service payload
    And I have added my Service to the server

  Scenario: Check if I can receive a Service
    Given I have my Service identifier
    When I send a GET request to the /service/id endpoint
    Then I receive a payload containing the Service

  Scenario: Check a non-existent service
    Given I have an invalid Service identifier
    When I send a GET request to the /service/id endpoint
    Then I receive a 404 status code

  Scenario: Check multiple Services
    Given I have added my Service to the server
    When I send a GET request to the /services endpoint
    Then I receive a payload containing all Services

  Scenario: Receive all statuses of a Service
    When I send a GET request to the /services/id/history
    Then I receive a payload containing a list of Statuses
    And the list is sorted chronologically

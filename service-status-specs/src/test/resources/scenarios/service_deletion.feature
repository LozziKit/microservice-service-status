Feature: Delete a Service

  Background:
    Given there is a Service server
    And I have added my Service to the server
    And I have my Service identifier

  Scenario: Delete my Service
    Given I have my Service identifier
    When I send a DELETE to the /service/id endpoint
    Then I receive a 204 status code
    When I send a GET request to the /service/id endpoint
    Then I receive a 404 status code

  Scenario: Delete a not existing Service
    Given I have an invalid Service identifier
    When I send a DELETE to the /service/id endpoint
    Then I receive a 404 status code
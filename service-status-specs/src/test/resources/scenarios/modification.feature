Feature: Modify a Service

  Background:
    Given there is a Service server for modification
    And I have added my Service to the server for modification
    And I have my Service identifier for modification

  Scenario: Modify my Service
    Given I have a Service payload for modification
    When I send a PUT request to the /service/id endpoint
    Then I receive a 200 status code for my modification

  Scenario: I try to modify a service that does not exist
    Given I have a Service payload for modification
    When I send a PUT request to the /service/id endpoint with an invalid ID
    Then I receive a 400 error code for my modification


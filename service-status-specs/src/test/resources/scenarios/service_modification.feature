Feature: Modify a Service

  Background:
    Given there is a Service server
    And I have added my Service to the server
    And I have my Service identifier

  Scenario: Modify my Service
    Given I have a Service payload for modification
    When I send a PUT request to the /service/id endpoint
    Then I receive a 204 status code

  Scenario: I try to modify a service that does not exist
    Given I have a Service payload for modification
    When I send a PUT request to the /service/id endpoint with an invalid ID
    Then I receive a 404 status code

  Scenario: I try to modify a Service with a null url
    Given I have a Service payload for modification
    And the modified payload URL field is null
    When I send a PUT request to the /service/id endpoint
    Then I receive an exception from the server
    And I receive a 422 status code

  Scenario: I try to modify a Service with a null name
    Given I have a Service payload for modification
    And the modified payload name field is null
    When I send a PUT request to the /service/id endpoint
    Then I receive an exception from the server
    And I receive a 422 status code

  Scenario: I try to modify a Service with a null port
    Given I have a Service payload for modification
    And the modified payload port field is null
    When I send a PUT request to the /service/id endpoint
    Then I receive an exception from the server
    And I receive a 422 status code

  Scenario: I try to modify a Service with a null interval
    Given I have a Service payload for modification
    And the modified payload interval field is null
    When I send a PUT request to the /service/id endpoint
    Then I receive an exception from the server
    And I receive a 422 status code



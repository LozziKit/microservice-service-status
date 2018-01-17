Feature: Create an Incident

  Background:
    Given there is a Service server
    And I have a Service payload
    And I have added my Service to the server
    And I have my Service identifier

  Scenario: Create an Incident
    Given I have an Incident payload
    When I send a POST request to the /service/serviceId/incidents endpoint
    Then I receive a 201 status code
    And I have my Incident identifier

  Scenario: Create an Incident with a null name field
    Given I have a Incident payload
    And the Incident name is null
    When I have added my Incident to the server
    Then I receive an exception from the server
    And I receive a 422 status code
    And I receive a name may not be null validation error message

  Scenario: Create an Incident with a null type field
    Given I have a Incident payload
    And the Incident type is invalid
    When I have added my Incident to the server
    Then I receive an exception from the server
    And I receive a 422 status code
    And I receive a type may not be null validation error message

  Scenario: Create an Incident with an invalid type field
    Given I have a Incident payload
    And the Incident type is null
    When I have added my Service to the server
    Then I receive an exception from the server
    And I receive a 422 status code
    And I receive a invalid type validation error message
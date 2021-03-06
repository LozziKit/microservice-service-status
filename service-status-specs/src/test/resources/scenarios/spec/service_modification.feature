Feature: Modify a Service

  Background:
    Given there is a Service server
    And I have a Service payload
    And I have added my Service to the server
    And I have my Service identifier

  Scenario: Modify my Service
    Given I have a Service payload
    When I send a PUT request to the /service/id endpoint
    Then I receive a 204 status code

  Scenario: I try to modify a service that does not exist
    Given I have an invalid Service identifier
    And I send a PUT request to the /service/id endpoint
    Then I receive a 404 status code

  Scenario: I try to modify a Service with a null url
    Given the Service URL is null
    When I send a PUT request to the /service/id endpoint
    Then I receive an exception from the server
    And I receive a 422 status code
    And I receive a url may not be null validation error message

  Scenario: I try to modify a Service with a null name
    Given the Service Name is null
    When I send a PUT request to the /service/id endpoint
    Then I receive an exception from the server
    And I receive a 422 status code
    And I receive a name may not be null validation error message

  Scenario: I try to modify a Service with a null description
    Given the Service Description is null
    When I send a PUT request to the /service/id endpoint
    Then I receive a 204 status code
    And the new Service description is null

  Scenario: I try to modify a Service with a negative port
    Given the Service port is negative
    When I send a PUT request to the /service/id endpoint
    Then I receive an exception from the server
    And I receive a 422 status code

  Scenario: I try to modify a Service with a port of zero
    Given the Service port is zero
    When I send a PUT request to the /service/id endpoint
    Then I receive an exception from the server
    And I receive a 422 status code

  Scenario: I try to modify a Service with a too big port
    Given the Service port is too big
    When I send a PUT request to the /service/id endpoint
    Then I receive an exception from the server
    And I receive a 422 status code

  Scenario: I try to modify a Service with a null interval
    Given the Service interval is null
    When I send a PUT request to the /service/id endpoint
    Then I receive an exception from the server
    And I receive a 422 status code
    And I receive a interval may not be null validation error message

  Scenario: I try to modify a Service with a too small interval
    Given the Service interval is to small
    When I send a PUT request to the /service/id endpoint
    Then I receive an exception from the server
    And I receive a 422 status code
    And I receive a interval must be greater than or equal to 5 validation error message

  Scenario: I try to modify a Service with a negative interval
    Given the Service interval is negative
    When I send a PUT request to the /service/id endpoint
    Then I receive an exception from the server
    And I receive a 422 status code
    And I receive a interval must be greater than or equal to 5 validation error message

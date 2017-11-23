Feature: Modify a Service

  Background:
    Given there is a Service server
    And I have added my Service to the server
    And I have my Service identifier

  Scenario: Modify my Service
    Given I have a Service payload
    When I send a PUT request to the /service/id endpoint
    Then I receive a 200 status code

  Scenario: I try to modify a service that does not exist
    Given I have a Service payload for modification
    When I send a PUT request to the /service/id endpoint with an invalid ID
    Then I receive a 400 status code

    Scenario: I try to modify a Service with a null url
      Given I have a Service payload for modification
      And the url field is null

Feature: Check we can add IncidentUpdates to an Incident

  Background:
    Given there is a Service server
    And I have a Service payload
    And I have added my Service to the server
    And I have an Incident payload
    And I have added my Incident to the server
    And I have an IncidentUpdate payload

  Scenario: Add an IncidentUpdate to an Incident
    Given I have my Service identifier
    And I have my Incident identifier
    When I have added my IncidentUpdate to the Server
    Then I receive a 201 status code

  Scenario: Add an IncidentUpdate to an Incident with a null type field
    Given I have my Service identifier
    And I have my Incident identifier
    And the IncidentUpdate type is null
    When I have added my IncidentUpdate to the Server
    Then I receive an exception from the server
    And I receive a 422 status code
    And I receive a type may not be null validation error message

  Scenario: Add an IncidentUpdate to an Incident with an invalid ServiceID
    Given I have an invalid Service identifier
    And I have my Incident identifier
    When I have added my IncidentUpdate to the Server
    Then I receive an exception from the server
    And I receive a 404 status code

  Scenario: Add an IncidentUpdate to an Incident with an invalid IncidentID
    Given I have my Service identifier
    And I have an invalid Incident identifier
    When I have added my IncidentUpdate to the Server
    Then I receive an exception from the server
    And I receive a 404 status code
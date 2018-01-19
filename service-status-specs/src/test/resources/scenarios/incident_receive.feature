Feature: Check we can receive Incidents

  Background:
    Given there is a Service server
    And I have a Service payload
    And I have added my Service to the server
    And I have an Incident payload
    And I have added my Incident to the server

  Scenario: Check if I can receive an Incident
    Given I have my Service identifier
    And I have my Incident identifier
    When I send a GET request to the /services/serviceId/incidents/incidentId endpoint
    Then I receive a payload containing the Incident

  Scenario: Check a non-existent Incident
    Given I have an invalid Incident identifier
    When I send a GET request to the /services/serviceId/incidents/incidentId endpoint
    Then I receive a 404 status code

  Scenario: Check multiple Incidents
    Given I have an Incident payload
    And  I have added my Incident to the server
    When I send a GET request to the /services/serviceId/incidents endpoint
    Then I receive a payload containing all Incidents for my Service
    And the payload contains multiple Incident
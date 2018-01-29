Feature: Check we can receive Incidents

  Background:
    Given there is a Service server
    And I have a Service payload
    And I have added my Service to the server
    And I have an Incident payload
    And I have added my Incident to the server
    And I have my Service identifier
    And I have my Incident identifier
    And I have an IncidentUpdate payload
    And I have added my IncidentUpdate to the Server
    And I have an IncidentUpdate payload
    And I have added my IncidentUpdate to the Server

  Scenario: Check if I can receive an Incident
    Given I have my Service identifier
    And I have my Incident identifier
    When I send a GET request to the /services/serviceId/incidents/incidentId endpoint
    Then I receive a payload containing the Incident

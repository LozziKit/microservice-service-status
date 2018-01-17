Feature: Delete a Incident

  Background:
    Given there is a Service server
    And I have added my Service to the server
    And I have my Service identifier
    And I have added an Incident to my Service

  Scenario: Delete my Service
    Given I have my Service identifier
    And I have my Incident identifier
    When I send a DELETE to the /services/serviceId/incidents/incidentId endpoint
    Then I receive a 204 status code
    When I send a GET request to the /services/serviceId/incidents/incidentId endpoint
    Then I receive a 404 status code

  Scenario: Delete a not existing Service
    Given I have an invalid Incident identifier
    When I send a DELETE to the /service/serviceId/incidents/incidentId endpoint
    Then I receive a 404 status code
Feature: Create an Incident

  Background:
    Given there is a Service server
    And I have added my Service to the server
    And I have my Service identifier

  Scenario: Create an Incident
    Given I have an Incident payload
    When I send a POST request to the /services/{serviceId]/incidents endpoint
    Then I receive a 201 status code
    And I have my Incident identifier

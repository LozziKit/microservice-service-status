Feature: Check we can receive Services

  Background:
    Given there is a Service server
    And I have added my Service to the server

  Scenario: Check if I can receive a Service
    Given I have my Service identifier
    When I send a GET request to the /service/id endpoint
    Then I receive a payload containing the Service

  Scenario: Check multiple Services
    When I send a GET request to the /services endpoint
    Then I receive a payload containing all Services

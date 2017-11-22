Feature: Check we can receive Services

  Background:
    Given there is a Service server for reception
    And I have added my Service to the server for reception

  Scenario: Check if I can receive a Service
    Given I have a Service identifier for reception
    When I send a GET request to the /service/id endpoint
    Then I receive a payload containing the Service

  Scenario: Check multiple Services
    When I send a GET request to the /services endpoint
    Then I receive a payload containing all Services

Feature: Create a Service

  Background:
    Given there is a Service server

  Scenario: Create a Service
    Given I have a Service payload
    When I POST it to the /services endpoint
    Then I receive a 201 status code
    And I receive the identifier of my Service

  Scenario: Create a Service with a wrong payload
    Given I have a wrong Service payload
    When I POST it to the /services endpoint
    Then I receive a 405 error code status code

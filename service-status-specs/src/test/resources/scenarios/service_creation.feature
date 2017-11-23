Feature: Create a Service

  Background:
    Given there is a Service server

  Scenario: Create a Service
    Given I have a Service payload
    When I have added my Service to the server
    Then I receive a 201 status code
    And I have my Service identifier

  Scenario: Create a Service with a wrong payload
    Given I have a Service payload
    And URL is null
    When I have added my Service to the server
    Then I receive a 422 status code

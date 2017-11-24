Feature: Create a Service

  Background:
    Given there is a Service server

  Scenario: Create a Service
    Given I have a Service payload
    When I have added my Service to the server
    Then I receive a 201 status code
    And I have my Service identifier

  Scenario: Create a Service with a null URL field
    Given I have a Service payload
    And the Service URL is null
    When I have added my Service to the server
    Then I receive a 422 status code

  Scenario: Create a Service with a null Name field
    Given I have a Service payload
    And the Service Name is null
    When I have added my Service to the server
    Then I receive a 422 status code

  Scenario: Create a Service with a null Description field
    Given I have a Service payload
    And the Service Description is null
    When I have added my Service to the server
    Then I receive a 201 status code

  Scenario: Create a Service with a null port field
    Given I have a Service payload
    And the Service port is null
    When I have added my Service to the server
    Then I receive a 422 status code

  Scenario: Create a Service with a negative port
    Given I have a Service payload
    And the Service port is negative
    When I have added my Service to the server
    Then I receive a 422 status code

  Scenario: Create a Service with a port of zero
    Given I have a Service payload
    And the Service port is zero
    When I have added my Service to the server
    Then I receive a 422 status code

  Scenario: Create a Service with a too big port
    Given I have a Service payload
    And the Service port is to big
    When I have added my Service to the server
    Then I receive a 422 status code


  Scenario: Create a Service with a null Interval field
    Given I have a Service payload
    And the Service interval is null
    When I have added my Service to the server
    Then I receive a 422 status code

  Scenario: Create a Service with a too small interval
    Given I have a Service payload
    And the Service interval is to small
    When I have added my Service to the server
    Then I receive a 422 status code

  Scenario: Create a Service with a negative interval
    Given I have a Service payload
    And the Service interval is negative
    When I have added my Service to the server
    Then I receive a 422 status code


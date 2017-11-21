Feature: Delete a Service

	Background:
		Given there is a Service server for deletion
        And I have added my Service to the server
        And I have my Service identifier
	
	Scenario: Delete my Service
		Given I have my Service identifier for deletion
		When I send a DELETE to the /service/{id} endpoint
		Then I receive a 200 status code
		And when I GET /service/{id}
		Then I receive a 404 response code

  Scenario: Delete a not existing Service
    Given I have a not existing Service identifier
    When I send a DELETE to the /service/{id} endpoint
    Then I receive a 404 error code

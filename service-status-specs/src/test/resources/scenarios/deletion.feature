Feature: Delete a Service

	Background:
		Given there is a Service server
		And the Service server contains my Service
	
	Scenario: Delete my Service
		Given I have my Service identifier
		When I send a DELETE to the /service/{id} endpoint
		Then I receive a 200 status code
		And when I GET /service/{id}
		Then I receive a 404 response code

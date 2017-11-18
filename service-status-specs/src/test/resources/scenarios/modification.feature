Feature: Modify a Service

	Background:
		Given there is a Service server
		And the Service server contains my Service
	
	Scenario: Modify my Service
		Given I have my Service identifier
		And I have a Service payload
		When I send a PUT request to the /service endpoint
		Then I receive a 200 status code
		
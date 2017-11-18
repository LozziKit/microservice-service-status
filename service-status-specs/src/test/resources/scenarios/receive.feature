Feature: Check Services
	Background:
		Given there is a Service server

	Scenario: Check if the Service is up and running
		Given I have a Service identifier
		When I GET /service/{id}
		Then I receive a payload containing the Service
		
	Scenario: Check multiple Services
		When I GET /services
		Then I receive a payload containing all Services
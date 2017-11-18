Feature: Modify a Service
  s
	Background:
		Given there is a Service server
		And I have added my Service to the server
		And I have my Service identifier
	
	Scenario: Modify my Service
		Given I have my Service identifier
		And I have a Service payload
		When I send a PUT request to the /service endpoint
		Then I receive a 200 status code
		
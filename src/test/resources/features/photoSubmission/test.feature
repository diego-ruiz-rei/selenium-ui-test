@runTest
Feature: Test Results
  As a user, I want to verify app is up
  
  @positive
  Scenario: App is up
    When the user is on the home page 
    Then the system displays content
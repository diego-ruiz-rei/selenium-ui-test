@accountspublic @photoSubmisssion
Feature: Allow User to provide ANumber and Upload Photo
  As a user, I want to upload a Photo, so that it can be validated against the biometric trusted photo
  
  Background: Login from Home page
   Given the user accesses the Photo Submission page
  
  @photoSubmissionPage
  Scenario: System provides to options to upload photo
  Then the system displays options add ANumber and Upload Photo
  
  @negative @invalidaNumber @wip
  Scenario: User providing invalid aNumber displays Error Message
    When the user provides "Invalid aNumber Details" data for photo submission 
    Then the system displays error message for "Invalid aNumber Details"

  @negative @invalidPhoto @wip
  Scenario: User providing invalid photo displays Error Message
    When the user provides "Invalid Photo Details" data for photo submission
    Then the system displays error message for "Invalid Photo Details"
 
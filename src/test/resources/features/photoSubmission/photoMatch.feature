@accountspublic @photoMatch @wip
Feature: Provide Photo Match Results
  As a user, I want to get results on whether photo matched or not, so that it can be validate the uploaded Photo
  
  Background: Login from Home page
   Given the user accesses the Photo Submission page
  
  @positive @validPhoto 
  Scenario: System displays Photo Match Results for matching photo
    When the user provides "Matching Photo Details" data for photo submission 
    Then the system displays match results page with Match Success

  @negative @invalidPhoto
  Scenario: System displays Photo Match Results for photo does not match
    When the user provides "Mismatching Photo Details" data for photo submission
    Then the system displays match results page with Match Failure
 
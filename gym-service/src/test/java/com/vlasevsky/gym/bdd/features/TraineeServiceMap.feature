Feature: Trainee Service Map Component Tests
  This feature tests the TraineeServiceMap functionalities.
  Scenario: Delete a trainee by username
    Given a trainee exists with username "john.doe"
    When the trainee with username "john.doe" is deleted
    Then the trainee with username "john.doe" should not exist in the system
  Scenario: Change active status of a trainee
    Given a trainee exists with username "john.doe"
    When I change the active status of trainee "john.doe" to "false"
    Then the active status of trainee "john.doe" should be "false"

  Scenario: Get trainee trainings
    Given a trainee exists with username "john.doe"
    When I fetch the trainings of trainee "john.doe" with trainer "trainer1" from "2023-01-01T00:00" to "2023-12-31T23:59"
    Then I should get a list of trainings

  Scenario: Update trainee details
    Given a trainee exists with username "john.doe"
    When I update trainee "john.doe" with first name "John", last name "Doe", address "123 Street", and date of birth "1990-01-01"
    Then the details of trainee "john.doe" should be updated

  Scenario: Find trainee by username
    Given a trainee exists with username "john.doe"
    When I search for trainee with username "john.doe"
    Then the trainee profile should be returned

  Scenario: Update trainers for trainee
    Given a trainee exists with username "john.doe"
    When I update trainers for trainee "john.doe" with "trainer1" and "trainer2"
    Then the trainers of trainee "john.doe" should be updated
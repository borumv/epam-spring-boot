Feature: Trainee Service Map Component Tests
  This feature tests the TraineeServiceMap functionalities.
  Scenario: Delete a trainee by username
    Given a trainee exists with username "john.doe"
    When the trainee with username "john.doe" is deleted
    Then the trainee with username "john.doe" should not exist in the system
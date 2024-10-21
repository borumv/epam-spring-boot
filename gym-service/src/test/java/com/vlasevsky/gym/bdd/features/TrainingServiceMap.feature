Feature: Training creation functionality
  This feature tests the functionality of creating trainings in the TrainingServiceMap service.

  Scenario: Successfully create a training
    Given a training create request with trainee "john_doe", trainer "trainer1", and training type "YOGA"
    When the training is created
    Then the training should be saved

  Scenario: Fail to create training due to missing trainee
    Given a non-existent trainee with username "missing_trainee"
    And a training create request with trainee "missing_trainee", trainer "trainer1", and training type "YOGA"
    When the training with non-existing trainee is created
    Then an error should be thrown for missing trainee

  Scenario: Fail to create training due to missing trainer
    Given a non-existent trainer with username "missing_trainer"
    And a training create request with trainee "john_doe", trainer "missing_trainer", and training type "YOGA"
    When the training with non-existing trainer is created
    Then an error should be thrown for missing trainer


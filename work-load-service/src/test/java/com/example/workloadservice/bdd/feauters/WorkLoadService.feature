Feature: Trainer Workload Management
  This feature tests the workload update and retrieval functionality.

  Scenario: Successfully update trainer workload
    Given a trainer workload request with username "trainer1" and training duration 60
    When the workload is updated
    Then the workload should be saved

  Scenario: Successfully retrieve yearly workload
    Given a workload summary for trainer "trainer1" for year 2023
    When I retrieve the yearly workload for trainer "trainer1" for year 2023
    Then the yearly workload should be returned


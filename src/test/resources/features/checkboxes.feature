@ui @checkboxes
Feature: Checkboxes
  As a user
  I want to check and uncheck checkboxes
  So that I can control their state

  Scenario: Check and uncheck boxes
    Given I open the home page
    When I navigate to "Checkboxes" page
    And I set checkbox 1 to "checked"
    And I set checkbox 2 to "unchecked"
    Then checkbox 1 should be "checked"
    And checkbox 2 should be "unchecked"

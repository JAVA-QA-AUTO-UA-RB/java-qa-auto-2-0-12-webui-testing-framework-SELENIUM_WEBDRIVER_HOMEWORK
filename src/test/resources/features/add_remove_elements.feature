@ui @addremove
Feature: Add/Remove Elements
  As a user
  I want to add and remove elements
  So that the list reacts correctly

  Scenario: Add new element
    Given I open the home page
    When I navigate to "Add/Remove Elements" page
    And I click "Add Element" button
    Then I should see 1 delete button

  Scenario: Remove element
    Given I open the home page
    When I navigate to "Add/Remove Elements" page
    And I click "Add Element" button
    And I click delete button
    Then I should see 0 delete buttons

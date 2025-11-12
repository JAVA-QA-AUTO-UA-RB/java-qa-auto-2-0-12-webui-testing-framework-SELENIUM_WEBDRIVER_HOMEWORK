Feature: Dropdown selection
  To verify dropdown behavior
  Users should be able to select an option

  Scenario: User selects option from dropdown
    Given I open the home page
    When I navigate to "Dropdown" page
    And I select option "Option 2" from dropdown
    Then dropdown should show option "Option 2"

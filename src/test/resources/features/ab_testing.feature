@ui @ab
Feature: A/B Testing page
  As a visitor
  I want to open A/B Testing page
  So that I can verify page header

  Scenario: Verify A/B Testing page has specific text
    Given I open the home page
    When I navigate to "A/B Testing" page
    Then I should see text that contains "A/B Test"

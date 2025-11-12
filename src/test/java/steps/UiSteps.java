package steps;

import hooks.WebHooks;
import io.cucumber.java.en.*;
import org.testng.Assert;
import pages.AddRemovePage;
import pages.AbTestingPage;
import pages.CheckboxesPage;
import pages.MainPage;
import hooks.SharedDriver;

public class UiSteps {

    private MainPage main;
    private AbTestingPage abPage;
    private AddRemovePage addRemovePage;
    private CheckboxesPage checkboxesPage;

    @Given("I open the home page")
    public void iOpenHomePage() {
        main = new MainPage(SharedDriver.getDriver());
        main.open();
    }

    @When("I navigate to {string} page")
    public void iNavigateToPage(String pageName) {
        switch (pageName) {
            case "A/B Testing":
                main.clickAbTesting();
                abPage = new AbTestingPage(SharedDriver.getDriver());
                break;

            case "Add/Remove Elements":
                main.clickAddRemove();
                addRemovePage = new AddRemovePage(SharedDriver.getDriver());
                break;

            case "Checkboxes":
                main.clickCheckboxes();
                checkboxesPage = new CheckboxesPage(SharedDriver.getDriver());
                break;

            default:
                throw new RuntimeException("Unknown page: " + pageName);
        }
    }

    @Then("I should see text that contains {string}")
    public void iShouldSeeText(String expected) {
        Assert.assertTrue(
                abPage.getHeaderText().contains(expected),
                "Expected text not found on page"
        );
    }

    @When("I click {string} button")
    public void iClickButton(String button) {
        switch (button) {
            case "Add Element":
                addRemovePage.clickAddElement();
                break;
            case "Delete":
                addRemovePage.clickDeleteButton();
                break;
            default:
                throw new RuntimeException("Unknown button: " + button);
        }
    }

    @Then("I should see {int} delete button")
    public void iShouldSeeDeleteButtonCount(int count) {
        Assert.assertEquals(
                addRemovePage.getDeleteButtonsCount(),
                count,
                "Delete button count mismatch"
        );
    }

    // ✅ ДОДАНО: крок для "I click delete button"
    @When("I click delete button")
    public void iClickDeleteButton() {
        addRemovePage.clickDeleteButton();
    }

    // ✅ ДОДАНО: крок для "I should see X delete buttons"
    @Then("I should see {int} delete buttons")
    public void iShouldSeeDeleteButtons(int count) {
        Assert.assertEquals(
                addRemovePage.getDeleteButtonsCount(),
                count,
                "Delete button count mismatch"
        );
    }

    @When("I set checkbox {int} to {string}")
    public void iSetCheckbox(int index, String state) {
        boolean shouldCheck = state.equalsIgnoreCase("checked");
        boolean currentState = checkboxesPage.isChecked(index - 1);

        if (currentState != shouldCheck) {
            checkboxesPage.clickCheckbox(index - 1);
        }
    }

    @Then("checkbox {int} should be {string}")
    public void checkboxShouldBe(int index, String state) {
        boolean shouldCheck = state.equalsIgnoreCase("checked");

        Assert.assertEquals(
                checkboxesPage.isChecked(index - 1),
                shouldCheck,
                "Checkbox state mismatch"
        );
    }
}


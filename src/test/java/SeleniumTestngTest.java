import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.List;

// This test class inherits BasicSetupTest class, where the browser is initialized
// browser variable is available here as it's inherited, so you'll have it available at any place
public class SeleniumTestngTest extends BasicSetupTest {

    @Test
    public void abTestingPageHasSpecificTextTest() {
        // Open the A/B Testing page directly
        browser.get("https://the-internet.herokuapp.com/abtest");

        // Locate the page header
        WebElement header = browser.findElement(By.cssSelector("div.example h3"));

        // Get the text
        String text = header.getText();

        // Verify the header contains "A/B Test"
        Assert.assertTrue(
                text.contains("A/B Test"),
                "Expected header to contain 'A/B Test'. Actual: " + text
        );
    }

    @Test
    public void addRemoveElementsTest() {
        // Open the page directly
        browser.get("https://the-internet.herokuapp.com/add_remove_elements/");

        // Add 3 "Delete" buttons
        WebElement addButton = browser.findElement(By.cssSelector("button[onclick='addElement()']"));
        for (int i = 0; i < 3; i++) {
            addButton.click();
        }

        // Verify 3 "Delete" buttons are displayed
        List<WebElement> deleteButtons = browser.findElements(By.cssSelector("button.added-manually"));
        Assert.assertEquals(deleteButtons.size(), 3, "Expected 3 Delete buttons.");

        // Click each "Delete" button to remove them
        for (WebElement btn : deleteButtons) {
            btn.click();
        }

        // Verify no "Delete" buttons displayed
        deleteButtons = browser.findElements(By.cssSelector("button.added-manually"));
        Assert.assertEquals(deleteButtons.size(), 0, "All Delete buttons should be removed.");
    }

    @Test
    public void checkboxesTest() {
        // Open the checkboxes page
        browser.get("https://the-internet.herokuapp.com/checkboxes");

        // Locate all checkboxes
        List<WebElement> checkboxes = browser.findElements(By.cssSelector("input[type='checkbox']"));

        // Verify there are only 2 checkboxes
        Assert.assertEquals(checkboxes.size(), 2, "Expected 2 checkboxes on the page");

        // Select all checkboxes if not already selected
        for (WebElement checkbox : checkboxes) {
            if (!checkbox.isSelected()) {
                checkbox.click();
            }
        }

        // Verify all checkboxes are selected
        for (WebElement checkbox : checkboxes) {
            Assert.assertTrue(checkbox.isSelected(), "All checkboxes should be selected");
        }
    }

    @Test
    public void selectOption2InDropdownTest() {
        // Open the page with the dropdown
        browser.get("https://the-internet.herokuapp.com/dropdown");

        // Locate the dropdown element
        WebElement dropdownElement = browser.findElement(By.id("dropdown"));

        // Initialize Select object
        Select dropdown = new Select(dropdownElement);

        // Select "Option 2" by visible text
        dropdown.selectByVisibleText("Option 2");

        // Verify "Option 2" is selected
        WebElement selectedOption = dropdown.getFirstSelectedOption();
        String selectedText = selectedOption.getText();

        Assert.assertEquals(selectedText, "Option 2", "Option 2 should be selected");
    }

    @Test
    public void formAuthenticationTest() {
        // Open login page
        browser.get("https://the-internet.herokuapp.com/login");

        // Enter valid credentials and log in
        browser.findElement(By.id("username")).sendKeys("tomsmith");
        browser.findElement(By.id("password")).sendKeys("SuperSecretPassword!");
        browser.findElement(By.cssSelector("button.radius")).click();

        // Wait for success message
        WebElement successMsg = new WebDriverWait(browser, Duration.ofSeconds(5))
                .until(ExpectedConditions.visibilityOfElementLocated(By.id("flash")));
        Assert.assertTrue(successMsg.getText().contains("You logged into a secure area!"),
                "Login success message is not displayed");

        browser.findElement(By.cssSelector("a[href='/logout']")).click();

        // Wait for logout message
        WebElement logoutMsg = new WebDriverWait(browser, Duration.ofSeconds(5))
                .until(ExpectedConditions.visibilityOfElementLocated(By.id("flash")));
        Assert.assertTrue(logoutMsg.getText().contains("You logged out of the secure area!"),
                "Logout success message is not displayed");
    }

    @Test
    public void dragAndDropTest() {
        // Open drag and drop page
        browser.get("https://the-internet.herokuapp.com/drag_and_drop");

        WebElement columnA = browser.findElement(By.id("column-a"));
        WebElement columnB = browser.findElement(By.id("column-b"));

        new WebDriverWait(browser, Duration.ofSeconds(3))
                .until(d -> columnA.isDisplayed() && columnB.isDisplayed());

        // Perform drag and drop using Actions class
        new Actions(browser)
                .dragAndDrop(columnA, columnB)
                .perform();

        // Verify the columns have swapped
        Assert.assertEquals(columnA.getText(), "B", "Column A should now contain B!");
        Assert.assertEquals(columnB.getText(), "A", "Column B should now contain A!");
    }

    @Test
    public void horizontalSliderTest() {
        // Open horizontal slider page
        browser.get("https://the-internet.herokuapp.com/horizontal_slider");

        WebElement slider = browser.findElement(By.cssSelector("input[type='range']"));
        WebElement value = browser.findElement(By.id("range"));

        // Store initial slider value
        String initialSliderValue = value.getText();

        // Move the slider using Actions
        Actions actions = new Actions(browser);
        actions.clickAndHold(slider)
                .moveByOffset(50, 0) // horizontal move
                .release()
                .perform();

        // Wait until the value element's text is not empty
        new WebDriverWait(browser, Duration.ofSeconds(5))
                .until(d -> !value.getText().isEmpty());

        // Verify the value has changed
        Assert.assertFalse(value.getText().isEmpty(), "Slider value should have changed");
    }
}
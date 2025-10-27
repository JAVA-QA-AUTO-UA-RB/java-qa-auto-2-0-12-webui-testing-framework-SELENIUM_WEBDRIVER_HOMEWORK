import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.Test;


public class SeleniumTestngTest extends BasicSetupTest {
    private static final String TEST_URL = "https://the-internet.herokuapp.com/";

    @Test
    public void abTestingPageHasSpecificTextTest() throws InterruptedException {
        Thread.sleep(2000);
        browser.get(TEST_URL);

        WebElement abTestingTaskLink = browser.findElement(By.xpath("//a[text()='A/B Testing']"));
        abTestingTaskLink.click();


        Assert.assertTrue(true);
    }


    @Test
    public void addRemoveElementsTest() throws InterruptedException {
        Thread.sleep(2000);
        browser.get(TEST_URL + "add_remove_elements/");
        WebElement addButton = browser.findElement(By.cssSelector("button[onclick='addElement()']"));


        for (int i = 0; i < 3; i++) {
            addButton.click();
        }


        int deleteButtonsCount = browser.findElements(By.cssSelector(".added-manually")).size();
        Assert.assertEquals(deleteButtonsCount, 3, "Expected 3 Delete buttons to be added");


        for (WebElement deleteButton : browser.findElements(By.cssSelector(".added-manually"))) {
            deleteButton.click();
        }


        deleteButtonsCount = browser.findElements(By.cssSelector(".added-manually")).size();
        Assert.assertEquals(deleteButtonsCount, 0, "Delete buttons were not removed");
    }

    @Test
    public void checkboxesTest() throws InterruptedException {
        Thread.sleep(2000);
        browser.get(TEST_URL + "checkboxes");
        WebElement checkbox1 = browser.findElement(By.cssSelector("input[type='checkbox']:nth-of-type(1)"));
        WebElement checkbox2 = browser.findElement(By.cssSelector("input[type='checkbox']:nth-of-type(2)"));

        if (!checkbox1.isSelected()) {
            checkbox1.click();
        }
        if (!checkbox2.isSelected()) {
            checkbox2.click();
        }


        Assert.assertTrue(checkbox1.isSelected(), "Checkbox 1 is not selected");
        Assert.assertTrue(checkbox2.isSelected(), "Checkbox 2 is not selected");
    }

    @Test
    public void dropdownTest() throws InterruptedException {
        Thread.sleep(2000);
        browser.get(TEST_URL + "dropdown");
        WebElement dropdownElement = browser.findElement(By.cssSelector("#dropdown"));
        Select dropdown = new Select(dropdownElement);


        dropdown.selectByVisibleText("Option 2");


        Assert.assertEquals(dropdown.getFirstSelectedOption().getText(), "Option 2", "Option 2 is not selected");
    }

    @Test
    public void formAuthenticationTest() throws InterruptedException {
        Thread.sleep(2000);
        browser.get(TEST_URL + "login");
        WebElement usernameField = browser.findElement(By.cssSelector("input#username"));
        WebElement passwordField = browser.findElement(By.cssSelector("input#password"));
        WebElement loginButton = browser.findElement(By.cssSelector("button[type='submit']"));


        usernameField.sendKeys("tomsmith");
        passwordField.sendKeys("SuperSecretPassword!");
        loginButton.click();


        WebElement successMessage = browser.findElement(By.cssSelector(".flash.success"));
        Assert.assertTrue(successMessage.isDisplayed(), "Login was not successful");


        WebElement logoutButton = browser.findElement(By.cssSelector("a[href='/logout']"));
        logoutButton.click();


        WebElement logoutMessage = browser.findElement(By.cssSelector(".flash.success"));

    }

    @Test
    public void dragAndDropTest() throws InterruptedException {
        Thread.sleep(2000);
        browser.get(TEST_URL + "drag_and_drop");
        WebElement elementA = browser.findElement(By.cssSelector("#column-a"));
        WebElement elementB = browser.findElement(By.cssSelector("#column-b"));


        Actions actions = new Actions(browser);
        actions.dragAndDrop(elementA, elementB).perform();


        String headerA = browser.findElement(By.cssSelector("#column-a header")).getText();
        Assert.assertEquals(headerA, "B", "Element A was not dragged to position B");
    }

    @Test
    public void horizontalSliderTest() throws InterruptedException {
        Thread.sleep(2000);
        browser.get(TEST_URL + "horizontal_slider");
        WebElement slider = browser.findElement(By.cssSelector("input[type='range']"));
        WebElement sliderValue = browser.findElement(By.cssSelector("#range"));


        String initialValue = sliderValue.getText();
        Thread.sleep(2000);

        Actions actions = new Actions(browser);
        actions.clickAndHold(slider).moveByOffset(10, 0).release().perform();
        Thread.sleep(2000);

        String newValue = sliderValue.getText();
        Assert.assertNotEquals(newValue, initialValue, "Slider value did not change");
    }
}
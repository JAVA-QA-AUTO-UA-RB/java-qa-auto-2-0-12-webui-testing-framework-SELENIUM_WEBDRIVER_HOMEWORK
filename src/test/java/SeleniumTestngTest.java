import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;


public class SeleniumTestngTest extends BasicSetupTest {

    @Test
    public void abTestingPageHasSpecificTextTest() {
        browser.get("https://the-internet.herokuapp.com/");
        browser.manage().window().maximize();
        WebDriverWait wait = new WebDriverWait(browser, Duration.ofSeconds(5));
        WebElement abTestingTaskLink = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("A/B Testing")));
        abTestingTaskLink.click();
        WebElement header = browser.findElement(By.cssSelector("h3"));
        String text = header.getText();
        Assert.assertTrue(text.contains("A/B Test "),
                "На сторінці немає тексту 'A/B Test '");
    }

    @Test
    public void addRemoveElementsTest() {
        browser.get("https://the-internet.herokuapp.com/add_remove_elements/");
        WebElement addButton = browser.findElement(By.cssSelector("button[onclick='addElement()']"));

        for (int i = 0; i < 3; i++) {
            addButton.click();
        }

        List<WebElement> deleteButtons = browser.findElements(By.cssSelector("button.added-manually"));
        Assert.assertEquals(deleteButtons.size(), 3, "Повинно бути 3 кнопки Delete");

        Wait<WebDriver> wait = new WebDriverWait(browser, Duration.ofSeconds(2));

        while (!browser.findElements(By.cssSelector("button.added-manually")).isEmpty()) {
            int before = browser.findElements(By.cssSelector("button.added-manually")).size();
            WebElement firstDelete = browser.findElement(By.cssSelector("button.added-manually"));
            firstDelete.click();
            wait.until(d ->
                    d.findElements(By.cssSelector("button.added-manually")).size() < before);
        }

        Assert.assertTrue(browser.findElements(By.cssSelector("button.added-manually")).isEmpty(),
                "Кнопки Delete повинні видалитись");
    }

    @Test
    public void checkboxesTest() {
        browser.get("https://the-internet.herokuapp.com/checkboxes");
        var checkboxes = browser.findElements(By.cssSelector("input[type='checkbox']"));
        for (WebElement box : checkboxes) {
            if (!box.isSelected()) box.click();
            Assert.assertTrue(box.isSelected(), "Чекбокс повинен бути обраний");
        }
    }

    @Test
    public void dropdownTest() {
        browser.get("https://the-internet.herokuapp.com/dropdown");
        WebElement dropdown = browser.findElement(By.id("dropdown"));
        dropdown.findElement(By.cssSelector("option[value='2']")).click();
        WebElement selected = browser.findElement(By.cssSelector("option[value='2']:checked"));
        Assert.assertNotNull(selected, "Option 2 повинна бути обрана");
    }

    @Test
    public void formAuthenticationTest() {
        browser.get("https://the-internet.herokuapp.com/login");
        browser.findElement(By.id("username")).sendKeys("tomsmith");
        browser.findElement(By.id("password")).sendKeys("SuperSecretPassword!");
        browser.findElement(By.cssSelector("button.radius")).click();
        WebElement successMsg = browser.findElement(By.id("flash"));
        Assert.assertTrue(successMsg.getText().contains("You logged into a secure area!"));

        browser.findElement(By.cssSelector("a[href='/logout']")).click();

        WebElement logoutMsg = browser.findElement(By.id("flash"));
        Assert.assertTrue(logoutMsg.getText().contains("You logged out of the secure area!"));
    }

    @Test
    public void dragandDropTest() {
        browser.get("https://the-internet.herokuapp.com/drag_and_drop");
        WebElement columnA = browser.findElement(By.id("column-a"));
        WebElement columnB = browser.findElement(By.id("column-b"));

        new WebDriverWait(browser, Duration.ofSeconds(3))
                .until(d -> columnA.isDisplayed() && columnB.isDisplayed());
        Actions actions = new Actions(browser);
        actions.dragAndDrop(columnA, columnB).perform();
        String textA = browser.findElement(By.id("column-a")).getText();
        Assert.assertEquals(textA, "B", "Елементи не помінялись місцями!");

    }

    @Test
    public void horizontalSliderTest() {
        browser.get("https://the-internet.herokuapp.com/horizontal_slider");
        WebElement slider = browser.findElement(By.cssSelector("input[type='range']"));
        WebElement value = browser.findElement(By.id("range"));


        slider.sendKeys(org.openqa.selenium.Keys.ARROW_RIGHT);
        Assert.assertNotEquals(value.getText(), "2", "Значення слайдера повинно змінитися");


    }


}
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.Keys;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.JavascriptExecutor;
import java.time.Duration;




// This test class inherits BasicSetupTest class, where the browser is initialized
// browser variable is available here as it's inherited, so you'll have it available at any place
public class SeleniumTestngTest extends BasicSetupTest {

    private WebDriverWait shortWait() {
        return new WebDriverWait(browser, Duration.ofSeconds(5));
    }


    @Test
    public void abTestingPageHasSpecificTextTest() {
        browser.get("https://the-internet.herokuapp.com/");
        shortWait().until(ExpectedConditions.elementToBeClickable(By.linkText("A/B Testing"))).click();

        String header = shortWait().until(ExpectedConditions.presenceOfElementLocated(By.tagName("h3"))).getText().trim();

        Assert.assertTrue(
                header.equals("A/B Test Control") || header.equals("A/B Test Variation 1"),
                "Unexpected header text: " + header
        );
    }

    @Test
    public void addRemoveElementsTest() {
        browser.get("https://the-internet.herokuapp.com/");

        shortWait().until(ExpectedConditions.elementToBeClickable(By.linkText("Add/Remove Elements"))).click();

        WebElement addButton = shortWait().until(ExpectedConditions.elementToBeClickable(By.xpath("//button[text()='Add Element']")));
        addButton.click();

        WebElement deleteBtn = shortWait().until(ExpectedConditions.elementToBeClickable(By.xpath("//button[text()='Delete']")));
        Assert.assertTrue(deleteBtn.isDisplayed());

        deleteBtn.click();

        Assert.assertTrue(browser.findElements(By.xpath("//button[text()='Delete']")).isEmpty(),
                "Delete button must disappear");
    }

    @Test
    public void checkboxesTest() {
        browser.get("https://the-internet.herokuapp.com/");
        shortWait().until(ExpectedConditions.elementToBeClickable(By.linkText("Checkboxes"))).click();

        WebElement cb1 = shortWait().until(ExpectedConditions.elementToBeClickable(By.xpath("//form[@id='checkboxes']/input[1]")));
        WebElement cb2 = browser.findElement(By.xpath("//form[@id='checkboxes']/input[2]"));

        cb1.click();
        Assert.assertTrue(cb1.isSelected());

        cb2.click();
        Assert.assertFalse(cb2.isSelected());
    }

    @Test
    public void dropdownSelectionTest() {
        browser.get("https://the-internet.herokuapp.com/");
        shortWait().until(ExpectedConditions.elementToBeClickable(By.linkText("Dropdown"))).click();

        WebElement dropdown = shortWait().until(ExpectedConditions.elementToBeClickable(By.id("dropdown")));
        dropdown.click();

        dropdown.findElement(By.cssSelector("option[value='2']")).click();

        String value = dropdown.findElement(By.cssSelector("option:checked")).getText().trim();
        Assert.assertEquals(value, "Option 2");
    }

    @Test
    public void formAuthenticationTest() {
        browser.get("https://the-internet.herokuapp.com/");
        shortWait().until(ExpectedConditions.elementToBeClickable(By.linkText("Form Authentication"))).click();

        shortWait().until(ExpectedConditions.elementToBeClickable(By.id("username"))).sendKeys("tomsmith");
        browser.findElement(By.id("password")).sendKeys("SuperSecretPassword!");
        browser.findElement(By.cssSelector("button[type='submit']")).click();

        String loginMessage = shortWait().until(ExpectedConditions.visibilityOfElementLocated(By.id("flash"))).getText();
        Assert.assertTrue(loginMessage.contains("You logged into a secure area!"));

        shortWait().until(ExpectedConditions.elementToBeClickable(By.cssSelector("a[href='/logout']"))).click();

        String logoutMessage = shortWait().until(ExpectedConditions.visibilityOfElementLocated(By.id("flash"))).getText();
        Assert.assertTrue(logoutMessage.contains("You logged out of the secure area!"));
    }

    @Test
    public void dragAndDropTest() {
        browser.get("https://the-internet.herokuapp.com/");
        shortWait().until(ExpectedConditions.elementToBeClickable(By.linkText("Drag and Drop"))).click();

        WebElement colA = shortWait().until(ExpectedConditions.presenceOfElementLocated(By.id("column-a")));
        WebElement colB = browser.findElement(By.id("column-b"));

        String beforeA = colA.getText();
        String beforeB = colB.getText();

        String jsScript = """
            function createEvent(type){var e=document.createEvent("CustomEvent");
            e.initCustomEvent(type,true,true,null);e.dataTransfer={data:{},
            setData:function(k,v){this.data[k]=v;}, getData:function(k){return this.data[k];}};
            return e;}
            function fireEvent(elem,event){elem.dispatchEvent(event);}
            var src=arguments[0]; var tgt=arguments[1];
            fireEvent(src,createEvent('dragstart'));
            fireEvent(tgt,createEvent('drop'));
            fireEvent(src,createEvent('dragend'));
        """;

        ((JavascriptExecutor) browser).executeScript(jsScript, colA, colB);

        Assert.assertNotEquals(colA.getText(), beforeA);
        Assert.assertNotEquals(colB.getText(), beforeB);
    }

    @Test
    public void horizontalSliderTest() throws InterruptedException {
        browser.get("https://the-internet.herokuapp.com/horizontal_slider");

        WebDriverWait wait = new WebDriverWait(browser, Duration.ofSeconds(5));

        WebElement slider = wait.until(
                ExpectedConditions.presenceOfElementLocated(
                        By.cssSelector("input[type='range']")
                )
        );

        JavascriptExecutor js = (JavascriptExecutor) browser;

        js.executeScript(
                "arguments[0].value = 2.5; " +
                        "arguments[0].dispatchEvent(new Event('input')); " +
                        "arguments[0].dispatchEvent(new Event('change'));",
                slider
        );

        Thread.sleep(300);

        String currentValue = browser.findElement(By.id("range")).getText().trim();

        Assert.assertEquals(currentValue, "2.5", "Slider value must be '2.5'");
    }


    // Write the rest of TEST METHODS according to the task here, each method checking one scenario described in README.md file
    // In the end you should have a set of test methods each of them describing some specific scenario

}

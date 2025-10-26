import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

// This test class inherits BasicSetupTest class, where the browser is initialized
// browser variable is available here as it's inherited, so you'll have it available at any place
public class SeleniumTestngTest extends BasicSetupTest {

    @Test
    public void abTestingPageHasSpecificTextTest() {
        browser.get("https://the-internet.herokuapp.com/");
        // Write your code here (just an example provided)
        WebElement abTestingTaskLink = browser.findElement(By.linkText("A/B Testing"));
        abTestingTaskLink.click();


        // Write your assertions in the after the steps of scenario are executed to validate results
        Assert.assertTrue(true);
    }

    // Write the rest of TEST METHODS according to the task here, each method checking one scenario described in README.md file
    // In the end you should have a set of test methods each of them describing some specific scenario

}

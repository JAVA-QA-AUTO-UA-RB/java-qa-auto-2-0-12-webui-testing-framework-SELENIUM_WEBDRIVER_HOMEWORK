package hooks;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

public class WebHooks {

    @Before
    public void setUp() {
        WebDriver driver = SharedDriver.getDriver();
    }

    @After
    public void tearDown(Scenario scenario) {
        try {
            WebDriver driver = SharedDriver.getDriver();
            if (scenario.isFailed() && driver instanceof TakesScreenshot) {
                final byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
                scenario.attach(screenshot, "image/png", scenario.getName().replaceAll("[^a-zA-Z0-9\\-]", "_"));
            }
        } catch (Exception ignored) {
        } finally {
            SharedDriver.quitDriver();
        }
    }
}




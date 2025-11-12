package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class AbTestingPage {

    private final WebDriver driver;

    private By header = By.tagName("h3");

    public AbTestingPage(WebDriver driver) {
        this.driver = driver;
    }

    public String getHeaderText() {
        return driver.findElement(header).getText();
    }
}

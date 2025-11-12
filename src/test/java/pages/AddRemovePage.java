package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class AddRemovePage {

    private final WebDriver driver;

    private By addButton = By.xpath("//button[text()='Add Element']");
    private By deleteButtons = By.xpath("//button[text()='Delete']");

    public AddRemovePage(WebDriver driver) {
        this.driver = driver;
    }

    public void clickAddElement() {
        driver.findElement(addButton).click();
    }

    public int getDeleteButtonsCount() {
        return driver.findElements(deleteButtons).size();
    }

    public void clickDeleteButton() {
        driver.findElement(deleteButtons).click();
    }
}


package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class CheckboxesPage {

    private final WebDriver driver;

    private final By checkboxes = By.cssSelector("#checkboxes input[type='checkbox']");

    public CheckboxesPage(WebDriver driver) {
        this.driver = driver;
    }

    public List<WebElement> getCheckboxes() {
        return driver.findElements(checkboxes);
    }

    public boolean isChecked(int index) {
        return getCheckboxes().get(index).isSelected();
    }

    public void clickCheckbox(int index) {
        getCheckboxes().get(index).click();
    }
}


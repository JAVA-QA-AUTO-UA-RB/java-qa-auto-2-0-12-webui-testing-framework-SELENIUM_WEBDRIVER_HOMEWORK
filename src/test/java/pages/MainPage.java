package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class MainPage {

    private final WebDriver driver;
    private final By dropdownLink = By.linkText("Dropdown");

    public MainPage(WebDriver driver) {
        this.driver = driver;
    }

    private By abTestingLink = By.linkText("A/B Testing");
    private By addRemoveLink = By.linkText("Add/Remove Elements");
    private By checkboxesLink = By.linkText("Checkboxes");


    public void open() {
        driver.get("https://the-internet.herokuapp.com/");
    }

    public void clickAbTesting() {
        driver.findElement(abTestingLink).click();
    }

    public void clickAddRemove() {
        driver.findElement(addRemoveLink).click();
    }

    public void clickCheckboxes() {
        driver.findElement(checkboxesLink).click();
    }
    public void clickDropdown() {
        driver.findElement(dropdownLink).click();
    }
}


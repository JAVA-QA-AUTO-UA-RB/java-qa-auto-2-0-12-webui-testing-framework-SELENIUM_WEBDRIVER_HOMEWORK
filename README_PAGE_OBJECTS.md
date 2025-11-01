# 🧭 Додаткове завдання: Рефакторинг із Page Objects

Це завдання виконується ПІСЛЯ завершення основного завдання з файлу `README.md`.
Мета — відрефакторити наявні тести, застосувавши підхід Page Object, структурувати проєкт і підготувати його до масштабування та паралельного запуску.

---

## 🪵 Перед стартом: гілка та Pull Request

- Створіть нову гілку від `main` і виконуйте всі зміни в ній. Наприклад: `feature/page-objects-refactor`.
- Після завершення роботи створіть Pull Request у напрямку `main`.
- Додайте посилання на Pull Request коментарем до виконаної домашньої роботи в Google Classroom, щоб викладач міг його переглянути.

---

## 🎯 Що потрібно зробити

1) Переписати тести, застосувавши патерн Page Object.
- Створити окремі класи-сторінки для кожної сторінки застосунку The Internet App, з якою працюють тести.
- Використовувати анотації `@FindBy` для оголошення веб-елементів.
- Інкапсулювати логіку взаємодії з елементами усередині Page Object класів (методи дій + прості перевірки стану сторінки).

2) Рознести сценарії за окремими тест-класами.
- Один тестовий клас — для однієї сторінки/групи пов’язаних сценаріїв.
- Тест-класи мають використовувати відповідні Page Object-и.

3) Додати (або оновити) `testng.xml` для керування запуском.
- Описати набір тестів, підключити всі нові тест-класи.
- Налаштувати паралельний запуск на рівні класів: `parallel="classes"` із розумним `thread-count`.

4) Винести спільну логіку в `BaseTest`.
- Ініціалізація/закриття браузера.
- Налаштування неявних та/або явних очікувань (implicit/explicit waits).
- Опціонально: базові хелпери/утиліти для навігації, створення WebDriverWait тощо.

5) Використати WebDriverManager (bonigarcia) для ініціалізації браузера.
- Жодного ручного завантаження/вказування шляхів до драйверів.
- Приклад: `WebDriverManager.chromedriver().setup();` перед створенням `new ChromeDriver(...)`.

---

## 🗂️ Рекомендована структура проєкту

- src
    - main
        - java
            - (за потреби — спільні утиліти, якщо виокремлюєте)
    - test
        - java
            - base
                - BaseTest.java
            - pages
                - HomePage.java
                - AbTestingPage.java
                - AddRemoveElementsPage.java
                - CheckboxesPage.java
                - DropdownPage.java
                - LoginPage.java
                - SecureAreaPage.java
                - DragAndDropPage.java
                - HorizontalSliderPage.java
            - tests
                - AbTestingTest.java
                - AddRemoveElementsTest.java
                - CheckboxesTest.java
                - DropdownTest.java
                - AuthenticationTest.java
                - DragAndDropTest.java
                - HorizontalSliderTest.java
- testng.xml (у корені репозиторію або в папці test/resources)

Назви — орієнтовні. Можете адаптувати під свій стиль, головне — логічність та читабельність.

---

## 🧱 Приклад Page Object із @FindBy

```java
package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class AbTestingPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    @FindBy(css = "#content h3")
    private WebElement header;

    public AbTestingPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
    }

    public AbTestingPage open() {
        driver.get("https://the-internet.herokuapp.com/abtest");
        return this;
    }

    public String getHeaderText() {
        wait.until(ExpectedConditions.visibilityOf(header));
        return header.getText();
    }
}
```

---

## 🧱 Приклад BaseTest

```java
package base;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import java.time.Duration;

public class BaseTest {
    protected WebDriver driver;
    protected WebDriverWait wait;

    @BeforeClass
    public void setUp() {
        // Менеджмент драйверів без ручного встановлення
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        // за необхідності: options.addArguments("--headless=new");
        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @AfterClass(alwaysRun = true)
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
```

---

## 🧪 Приклад тесту, що використовує Page Object

```java
package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.AbTestingPage;

public class AbTestingTest extends BaseTest {

    @Test
    public void headerShouldContainAbTestText() {
        String text = new AbTestingPage(driver)
                .open()
                .getHeaderText();
        Assert.assertTrue(text.contains("A/B Test"), "Очікували, що заголовок міститиме 'A/B Test'");
    }
}
```

---

## 📄 Приклад testng.xml із паралельним запуском класів

```xml
<!DOCTYPE suite SYSTEM "https://testng.org/testng-1.0.dtd" >
<suite name="UI Suite" verbose="1" parallel="classes" thread-count="3">
    <test name="The Internet App">
        <classes>
            <class name="tests.AbTestingTest"/>
            <class name="tests.AddRemoveElementsTest"/>
            <class name="tests.CheckboxesTest"/>
            <class name="tests.DropdownTest"/>
            <class name="tests.AuthenticationTest"/>
            <class name="tests.DragAndDropTest"/>
            <class name="tests.HorizontalSliderTest"/>
        </classes>
    </test>
</suite>
```

Примітки:
- Для коректного паралельного запуску слідкуйте, щоб між тестами не було прихованих залежностей/спільного стану.
- Якщо використовуєте паралельність, краще, щоб `BaseTest` створював окремий інстанс драйвера на клас або на метод (за потреби можна перейти на `@BeforeMethod`).

---

## ✅ Критерії приймання

- Проєкт відрефакторено на Page Object-и:
    - Всі локатори оголошені через `@FindBy` усередині класів сторінок.
    - Логіка взаємодії з елементами інкапсульована в методах Page Object-ів.
- Тести розбиті на окремі тест-класи відповідно до сторінок/функціоналу.
- Додано/налаштовано `testng.xml`:
    - Усі нові тест-класи підключені.
    - Налаштовано паралельне виконання на рівні класів (`parallel="classes"`).
- Є `BaseTest` із ініціалізацією та завершенням WebDriver, а також налаштованими `implicit` та/або `explicit` wait-ами.
- Для ініціалізації браузера використано WebDriverManager (bonigarcia) — без ручного встановлення драйверів або прописування шляхів.
- Всі тести проходять локально та через Maven.

---

## ▶️ Як запускати

- Запуск через Maven (використає testng.xml у корені):

```bash
mvn clean test -Dsurefire.suiteXmlFiles=testng.xml
```

- Або додайте конфігурацію TestNG у IDE та оберіть `testng.xml`.

---

## 💡 Поради

- Обирайте надійні локатори (уникайте крихких ієрархічних XPath, віддавайте перевагу id, data-* атрибутам, стабільним класам).
- Тримайте Page Object-и «тонкими»: без assert-ів усередині (окрім найпростіших перевірок стану). Основні перевірки — у тестах.
- Для очікувань віддавайте перевагу `WebDriverWait` із чіткими умовами (`ExpectedConditions`).
- Не дублюйте код: спільне — у `BaseTest` або утилітарних класах.
- Іменуйте тести зрозуміло: що перевіряєте та який очікуваний результат.

---

## 🏁 Результат

Створіть Pull Request, який містить:
- Нову структуру з Page Object-ами та тест-класами.
- Налаштований `testng.xml` із паралельним виконанням класів.
- Оновлені/переписані тести, що успішно проходять.

Після створення PR додайте посилання на нього коментарем до виконаної домашньої роботи в Google Classroom, щоб викладач міг його легко знайти.

Успіхів! 🚀
# Домашнє завдання: Рефакторинг проєкту під Cucumber + TestNG

Цей репозиторій — шаблон для виконання ДЗ з UI‑автотестів на Selenium WebDriver. На попередніх етапах ви працювали з TestNG і «ручними» тестами. Тепер ваше завдання — рефакторити проєкт під BDD‑підхід із використанням Cucumber, додати необхідну структуру, `feature`‑файли, степ‑дефінішени, хуки, раннери тощо.

Мета: навчитися
- організовувати BDD‑тести у форматі Given/When/Then;
- підтримувати зрозумілу структуру тестового фреймворку;
- налаштовувати Cucumber + TestNG у Maven‑проєкті;
- відокремлювати бізнес‑кроки (step definitions) від реалізації дій (Page Object / сервісні класи);
- піднімати та гасити браузер коректно на рівні сценарію/фічі (Hooks).

---

## ⚠️ ВАЖЛИВО: Перед початком роботи створіть нову гілку!

**ПЕРЕД ТИМ, ЯК РОЗПОЧАТИ ВИКОНАННЯ ЗАВДАННЯ:**

1. Переконайтеся, що ви знаходитесь у гілці `main`:
   ```bash
   git checkout main
   git pull origin main
   ```

2. Створіть нову гілку для виконання завдання:
   ```bash
   git checkout -b feature/cucumber_refactoring
   ```

3. Виконуйте всі зміни у цій гілці.

4. **Після завершення роботи:**
   - Закомітьте всі зміни
   - Запуште гілку у свій репозиторій: `git push origin feature/cucumber_refactoring`
   - Створіть Pull Request у своєму репозиторії з гілки `feature/cucumber_refactoring` в напрямку `main`
   - **Додайте посилання на Pull Request у коментар до домашки в Google Classroom**

---

## 1. Вимоги до інструментів
- Java 17+
- Maven
- TestNG
- Selenium WebDriver 4.x
- Cucumber (Java + TestNG)

Репозиторій уже містить базовий каркас із TestNG. Ваше завдання — додати Cucumber, зберігши або делікатно адаптувавши наявну логіку.

---

## 2. Налаштування Maven (pom.xml)
Додайте залежності Cucumber (актуальні версії підберіть у Maven Central; нижче – орієнтовний приклад):

```xml
<dependencies>
    <!-- Selenium + TestNG уже можуть бути додані у шаблоні -->

    <!-- Cucumber: Java API -->
    <dependency>
        <groupId>io.cucumber</groupId>
        <artifactId>cucumber-java</artifactId>
        <version>7.18.1</version>
        <scope>test</scope>
    </dependency>

    <!-- Cucumber: TestNG integration -->
    <dependency>
        <groupId>io.cucumber</groupId>
        <artifactId>cucumber-testng</artifactId>
        <version>7.18.1</version>
        <scope>test</scope>
    </dependency>

    <!-- Опційно: ін`єкція залежностей для спільного стану між кроками -->
    <dependency>
        <groupId>io.cucumber</groupId>
        <artifactId>cucumber-picocontainer</artifactId>
        <version>7.18.1</version>
        <scope>test</scope>
    </dependency>
</dependencies>
```

Плагін Surefire (або Failsafe) налаштуйте так, щоб знаходив і виконував Cucumber‑раннери. Приклад для Surefire:

```xml
<build>
  <plugins>
    <plugin>
      <groupId>org.apache.maven.plugins</groupId>
      <artifactId>maven-surefire-plugin</artifactId>
      <version>3.2.5</version>
      <configuration>
        <includes>
          <include>**/*Runner.java</include>
        </includes>
        <systemPropertyVariables>
          <cucumber.filter.tags>@ui</cucumber.filter.tags>
        </systemPropertyVariables>
      </configuration>
    </plugin>
  </plugins>
</build>
```

Якщо ви запускаєте через TestNG‑suite (`testng.xml`), переконайтеся, що раннери Cucumber підтягуються у цей suite (див. розділ про раннер нижче).

---

## 3. Рекомендована структура проєкту

```
src
├─ main
│  └─ java
│     └─ org.example ... (продуктивний код за потреби)
└─ test
   ├─ java
   │  ├─ hooks
   │  │  └─ WebHooks.java                 (Before/After для WebDriver)
   │  ├─ pages
   │  │  └─ ... Page Object класи         (HomePage, AbTestingPage, тощо)
   │  ├─ steps
   │  │  └─ UiSteps.java                  (Given/When/Then кроки)
   │  └─ runners
   │     └─ CucumberTestRunner.java       (або кілька під різні набори)
   └─ resources
      └─ features
         ├─ ab_testing.feature
         └─ ... інші .feature файли
```

Примітки:
- Існуючі `BasicSetupTest` та інші TestNG‑класи можна або зберегти для порівняння, або інкапсулювати їх логіку в Hooks/Page Object.
- Логіку ініціалізації WebDriver з `BasicSetupTest` перенесіть у хуки (`@Before`, `@After`).

---

## 4. Приклад .feature файлу
`src/test/resources/features/ab_testing.feature`

```gherkin
@ui @ab
Feature: A/B Testing page
  As a visitor of the Internet Herokuapp site
  I want to open A/B Testing page
  So that I can verify specific text is present

  Scenario: Verify A/B Testing page has specific text
    Given I open the home page
    When I navigate to "A/B Testing" page
    Then I should see text that contains "A/B Test Control"
```

Пишіть сценарії українською або англійською — головне, щоб кроки були однозначними. Додайте ще кілька сценаріїв відповідно до вимог із основного `README.md` (напр., навігація, валідації, негативні кейси).

---

## 5. Хуки (Hooks) для WebDriver
`src/test/java/hooks/WebHooks.java`

```java
package hooks;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class WebHooks {
    public static WebDriver driver;

    @Before
    public void setUp() {
        // TODO: зчитування опцій із системних властивостей (headless, window size тощо)
        driver = new ChromeDriver();
        driver.manage().window().maximize();
    }

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
```

За потреби винесіть фабрику браузера в окремий утилітарний клас. Якщо вже є готові налаштування у `BasicSetupTest`, пере використайте їх у хуках.

---

## 6. Page Object приклад
`src/test/java/pages/HomePage.java`

```java
package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class HomePage {
    private final WebDriver driver;
    private final By abTestingLink = By.linkText("A/B Testing");

    public HomePage(WebDriver driver) {
        this.driver = driver;
    }

    public void open() {
        driver.get("https://the-internet.herokuapp.com/");
    }

    public void goToAbTesting() {
        driver.findElement(abTestingLink).click();
    }
}
```

`src/test/java/pages/AbTestingPage.java`

```java
package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class AbTestingPage {
    private final WebDriver driver;
    private final By pageHeader = By.tagName("h3");

    public AbTestingPage(WebDriver driver) {
        this.driver = driver;
    }

    public String headerText() {
        return driver.findElement(pageHeader).getText();
    }
}
```

---

## 7. Step Definitions (кроки)
`src/test/java/steps/UiSteps.java`

```java
package steps;

import hooks.WebHooks;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;
import pages.AbTestingPage;
import pages.HomePage;

public class UiSteps {
    private HomePage home;
    private AbTestingPage ab;

    @Given("I open the home page")
    public void iOpenHomePage() {
        home = new HomePage(WebHooks.driver);
        home.open();
    }

    @When("I navigate to \"A/B Testing\" page")
    public void iNavigateToAbTesting() {
        home.goToAbTesting();
        ab = new AbTestingPage(WebHooks.driver);
    }

    @Then("I should see text that contains \"{string}\"")
    public void iShouldSeeText(String expected) {
        Assert.assertTrue(ab.headerText().contains(expected),
                "Очікуваний текст відсутній у заголовку сторінки");
    }
}
```

Мову кроків (`Given/When/Then`) можна змінити на українську (через `io.cucumber.java.uk.*`) або залишити англійські анотації — на ваш розсуд. Для стабільності групи зазвичай залишають англійські.

---

## 8. Раннер Cucumber + TestNG
`src/test/java/runners/CucumberTestRunner.java`

```java
package runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
        features = "src/test/resources/features",
        glue = {"steps", "hooks"},
        plugin = {
                "pretty",
                "html:target/cucumber-report.html",
                "json:target/cucumber.json"
        },
        tags = "@ui",
        monochrome = true
)
public class CucumberTestRunner extends AbstractTestNGCucumberTests {
}
```

Запуск:
- Через Maven: `mvn clean test` (Surefire знайде `*Runner.java`).
- Через TestNG suite (`testng.xml`): додайте клас раннера у suite:

```xml
<!DOCTYPE suite SYSTEM "https://testng.org/testng-1.0.dtd" >
<suite name="UI Suite">
  <test name="Cucumber">
    <classes>
      <class name="runners.CucumberTestRunner"/>
    </classes>
  </test>
</suite>
```

---

## 9. Що саме потрібно зробити у цьому ДЗ
1) Додати у `pom.xml` залежності Cucumber (+ за потреби Surefire/Failsafe конфіг).  
2) Додати рекомендовану структуру тек для `features`, `steps`, `hooks`, `pages`, `runners`.  
3) Перенести ініціалізацію WebDriver із `BasicSetupTest` у Cucumber‑хуки.  
4) Міграція сценаріїв із README.md у BDD‑формат (`.feature` файли) — не менше 4‑6 сценаріїв.  
5) Реалізувати step definitions для всіх сценаріїв, використовуючи Page Object.  
6) Налаштувати Cucumber‑раннер під TestNG, теги, репорти (`pretty`, `html`, `json`).  
7) Забезпечити можливість запуску тестів:
   - локально через IDE (раннер),
   - через Maven (`mvn test`),
   - опційно через `testng.xml`.
8) Додати базові негативні перевірки, коректні асерти та стабільні локатори.  
9) Оформити короткий `README`‑розділ про те, як запускати Cucumber‑тести (можете доповнити цей файл секцією «Запуск» для вашого середовища).

---

## 10. Критерії оцінювання
- Структура проєкту зрозуміла та логічна (features/steps/hooks/pages/runners).  
- Мінімум 4‑6 якісних сценаріїв у `.feature` файлах із осмисленими кроками.  
- Step definitions не «клікають» напряму, а використовують Page Object.  
- Ініціалізація/завершення WebDriver виконуються через хуки і не течуть між сценаріями.  
- Тести стабільно запускаються локально та через Maven.  
- Локатори надійні, асерти інформативні.  
- Репорти Cucumber генеруються (html/json).  
- Охайний код‑стайл, мінімум дублікацій, зрозумілі імена.

---

## 11. Поради та застереження
- Не змішуйте бізнес‑кроки та технічні деталі у step definitions — тримайте драйвер/локатори у Page Object.  
- Використовуйте теги (`@ui`, `@smoke`, `@regression`) для групування сценаріїв.  
- Робіть маленькі кроки (Given/When/Then), щоб їх можна було перевикористовувати.  
- Уникайте `Thread.sleep`; віддавайте перевагу WebDriverWait.  
- Для headless‑запуску підхоплюйте параметр із `-Dheadless=true`.  
- Зробіть утиліту для створення драйвера (Chrome/Firefox) за системною змінною `-Dbrowser=chrome|firefox`.

---

## 12. Що здати
1. **Pull Request у вашому репозиторії** з гілки `feature/cucumber_refactoring` в напрямку `main` із:
   - зміненим `pom.xml` (додані залежності Cucumber),
   - новими папками/класами (hooks/pages/steps/runners),
   - `.feature` файлами у `src/test/resources/features`,
   - оновленими інструкціями по запуску (цей файл).
2. **Посилання на Pull Request у коментар до домашки в Google Classroom**.

---

## 13. Як запускати (швидкий старт)
- IDE: запустіть `CucumberTestRunner`.  
- Maven: `mvn clean test -Dcucumber.filter.tags=@ui`  
- TestNG suite: запустіть `testng.xml` з доданим класом раннера.

Додаткові прапорці:
```
-Dbrowser=chrome -Dheadless=true -Dcucumber.filter.tags=@smoke
```

Успіхів! Якщо виникнуть питання — готуйте мінімальні приклади та логи запуску, щоб швидше їх розв'язати.
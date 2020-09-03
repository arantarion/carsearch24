import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.junit.Assert.assertEquals;

/**
 * @author Henry Weckermann
 * Hausarbeit im Rahmen von Software Engineering 2 bei Prof. Dr. Sasha Alda
 */

public class LoginTest {

    private static WebDriver driver;

    public LoginTest() {
    }

    @Before
    public void setUp() {
        System.setProperty("webdriver.gecko.driver", "Drivers\\geckodriver.exe");
        driver = new FirefoxDriver();
    }

    @After
    public void tearDown() {
        driver.quit();
    }

    @Test
    public void login() {

        WebDriverWait wait = new WebDriverWait(driver, 5);

        //open website. Server has to be started beforehand
        driver.get("http://localhost:8080/!login");

        //maximize the window for looks
        driver.manage().window().maximize();

        //make sure that the page is rendered
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("gwt-uid-3")));

        //find and select the email field
        driver.findElement(By.id("gwt-uid-3")).click();

        //input email of salesman account
        driver.findElement(By.id("gwt-uid-3")).sendKeys("admin@carsearch24.de");

        //input password
        driver.findElement(By.id("gwt-uid-5")).sendKeys("Test1234");

        //press "login" button
        driver.findElement(By.cssSelector(".v-slot:nth-child(5) > .v-button")).click();

        //make sure the new site is loaded
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"ROOT-2521314\"]/div/div[2]/div[1]/div/div[5]/div/span[1]")));

        //find user page button and extract the text out of it.
        WebElement element = driver.findElement(By.xpath("//*[@id=\"ROOT-2521314\"]/div/div[2]/div[1]/div/div[5]/div/span[1]"));
        String buttonName = element.getText().substring(1); //there is a weird char in the button text so it is left out

        // Tests if the user name and role is correct by checking the button for the personal page: <user first name>'s Autos
        assertEquals("Henry's Autos", buttonName);

        // Press logout button
        driver.findElement(By.cssSelector(".v-menubar-menuitem:nth-child(2)")).click();

        // Select email field
        driver.findElement(By.id("gwt-uid-3")).click();

        //input the email of a user account
        driver.findElement(By.id("gwt-uid-3")).sendKeys("henry.weckermann@gmx.de");

        //input password
        driver.findElement(By.id("gwt-uid-5")).sendKeys("Test1234");

        //confirm by enter key this time
        driver.findElement(By.id("gwt-uid-5")).sendKeys(Keys.ENTER);

        //make sure the page is loaded correctly
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"ROOT-2521314\"]/div/div[2]/div[1]/div/div[5]/div/span[1]")));

        //finds the user page button and reads its text
        WebElement element2 = driver.findElement(By.xpath("//*[@id=\"ROOT-2521314\"]/div/div[2]/div[1]/div/div[5]/div/span[1]"));
        String buttonName2 = element2.getText().substring(1); //there is a weird char in the button text so it is left out

        //Button text is: <user first name>'s Reservierungen
        assertEquals("Anna's Reservierungen", buttonName2);
    }
}

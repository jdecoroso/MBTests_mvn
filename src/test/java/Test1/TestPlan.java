package Test1;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.testng.Assert.assertEquals;


public class TestPlan {

    public static WebDriver driver= new ChromeDriver();
    @BeforeSuite
    public void browser() {

    System.setProperty("webdriver.chrome.driver",Utils.CHROME_DRIVER);

    }


    @Test(testName = "Open Mercedes-Benz uk website")
            public void openSite(){
                driver.manage().window().maximize();
                driver.get(Utils.MERC_UK_URL);
                CheckCookiesAndProceed(driver);

    }

    @Test(testName = "Validate basic content such as title", dependsOnMethods = {"openSite"})
    public void ValidateBasicContent(){
        String title = driver.getTitle();
        System.out.println(title);
        assertEquals(title,"Mercedes-Benz Passenger Cars");
    }

    @Test(testName = "Validate if Models / Hatchbacks exist", dependsOnMethods = {"openSite", "ValidateBasicContent"})
    public void ValidateModels(){
        //Validate the number of menu options for body types
        List<WebElement> body_types = driver.findElements(By.cssSelector("[class=\"vmos_2Lgix vmos_1dbJ5\"] wb-subnavigation-item"));
        System.out.println(body_types.size());
    }

    private void CheckCookiesAndProceed(WebDriver driver) {
//Get the shadowRoot of the element you want to intrude in on,
//and then use that as your root selector.
        WebElement host = driver.findElement(By.cssSelector("[settings-id=\"fph8XBqir\"]"));
        JavascriptExecutor js = (JavascriptExecutor)driver;
        WebElement shadowRoot = (WebElement)(js.executeScript("return arguments[0].shadowRoot", host));
        shadowRoot.findElement(By.cssSelector("[data-test=\"handle-accept-all-button\"]")).click();
    }

    @AfterSuite
    public static void cleanup(){

        driver.manage().deleteAllCookies();
        driver.close();
    }
}
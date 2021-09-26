package Test1;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.util.List;

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

    @Test(testName = "Validate if Models / Hatchbacks exist and Select Body Type", dependsOnMethods = {"openSite", "ValidateBasicContent"})
    public void ValidateBodiesAndSelect(){
        //Models selection bar is located under an iframe, therefore we must change to that frame before continuing
        WebElement iframe = driver.findElement(By.id("vmos-cont"));
        driver.switchTo().frame(iframe);
        //Find the menu items
        List<WebElement> body_types = driver.findElements(By.cssSelector("[class=\"vmos_1nZ_z\"]"));

        //Validate the menu size
        System.out.println("Total number of body types: " + body_types.size());
        assertEquals(body_types.size(), Utils.BODY_TYPES.size());

        //validate that each individual body type category has the correct text
        for (int i=0;i < Utils.BODY_TYPES.size(); i++) {
            System.out.println("Checking body type button " + Utils.BODY_TYPES.get(i) + " in position " + (i+1));
            assertEquals(Utils.BODY_TYPES.get(i), body_types.get(i).getText());

        }

        //Select the desired body type
        driver.findElements(By.cssSelector("span.vmos_1nZ_z")).get(Utils.BODY_TYPES.indexOf(Utils.BODY)).click();
        System.out.println("Selected "+ Utils.BODY);

    }

    @Test(testName = "Hover over the correct car and Click Build", dependsOnMethods = {"openSite", "ValidateBasicContent", "ValidateBodiesAndSelect"})
    public void BuildCar(){

        //Hover over the desired model and click in "Build your car"
        Actions action = new Actions(driver);
        WebElement element = driver.findElement(By.xpath(Utils.A_CLASS_XPATH));
        //OUModels Element to scroll into view
        WebElement our_models = driver.findElement(By.xpath("//h1[contains(text(),\"Our models\")]"));
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView();", our_models);


        action.moveToElement(element).perform();
        //Click on Build Your Car for the A-Class --> Xpath can be used for the generic "Build button" since only one be visible because of the "hover" behaviour

        WebElement buildButton = driver.findElement(By.xpath(Utils.BUILD_BUTTON_XPATH));
        action.moveToElement(buildButton).perform();
        action.click().build().perform();


        System.out.println("Navigating to Build the car");



    }
    @Test(testName = "Filter by fuel", dependsOnMethods = {"openSite", "ValidateBasicContent", "ValidateBodiesAndSelect", "BuildCar"})
    public void FilterByFuel(){

        //waiting until Mercedes Images are clickable
        WebDriverWait wait = new WebDriverWait(driver, 30);
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"owcc-cont\"]/div/owcc/cc-app-root/div/cc-app-container/div/div[2]/div/div[2]/div/div[2]/cc-motorization/cc-motorization-filters-section/cc-motorization-filters/form/fieldset[1]/div[2]/div[1]/wb-checkbox-control/label/wb-icon")));

        //scroll to the fuel fields
        WebElement fuel_type = driver.findElement(By.xpath(Utils.FUEL_TABLE_XPATH));
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView();", fuel_type);


        //Check what kinds of fuel exist and compare with expected
        List<WebElement> fuel_types = driver.findElements(By.xpath(Utils.FUEL_LABELS_XPATH));
        assertEquals(fuel_types.size(), Utils.FUEL_TYPES.size());
        System.out.println("Total number of fuel types: " + fuel_types.size());

        for (int i=0;i < Utils.FUEL_TYPES.size(); i++) {
            System.out.println("Checking fuel type label for " + Utils.FUEL_TYPES.get(i) + " in position " + (i+1));
            assertEquals(Utils.FUEL_TYPES.get(i), fuel_types.get(i).getText());

        }
        //select the desired fuel

        for (int i=0;i < Utils.FUEL_TYPES.size(); i++) {
            if(fuel_types.get(i).getText().equals(Utils.DESIRED_FUEL)){
                System.out.println("Found the desired fuel in position" + (i+1));
                fuel_types.get(i).click();
            }

        }
        System.out.println("Done");
        //check how many options there are and take screenshot

        //if there are more than 2 options, scroll to the side and take more screenshots
    }



    private void CheckCookiesAndProceed(WebDriver driver) {

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
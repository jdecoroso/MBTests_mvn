package Test1;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.FileUtils;

import static java.lang.Integer.parseInt;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;


public class Test1 {

    private static WebDriver driver = null;


    @BeforeSuite
    public void browser() {

//    System.setProperty("webdriver.chrome.driver",Utils.CHROME_DRIVER);
        if (Utils.BROWSER.equalsIgnoreCase("chrome")) {
            //initializing and starting the chrome browser
            WebDriverManager.chromedriver().setup();
            driver = new ChromeDriver();

        } else if (Utils.BROWSER.equalsIgnoreCase("firefox")) {
            //initializing and starting the firefox browser
            WebDriverManager.firefoxdriver().setup();
            driver = new FirefoxDriver();


        }
    }


    @Test(testName = "Open Mercedes-Benz uk website")
    public void openSite(){
        driver.manage().window().maximize();
        driver.get(Utils.MERC_URL);
        WebDriverWait wait0 = new WebDriverWait(driver, 30);
        wait0.until(ExpectedConditions.elementToBeClickable(By.xpath(Utils.HOME_PAGE_READY_XPATH)));
        CheckCookiesAndProceed(driver);

    }

    @Test(testName = "Validate basic content such as title", dependsOnMethods = {"openSite"})
    public void ValidateBasicContent(){
        String title = driver.getTitle();
        System.out.println(title);
        assertEquals(title,"Mercedes-Benz Passenger Cars");
        //A lot of more things can be validated here
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
    public void FilterByFuel() throws IOException {

        //waiting until Mercedes Images are clickable
        WebDriverWait wait = new WebDriverWait(driver, 30);
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"owcc-cont\"]/div/owcc/cc-app-root/div/cc-app-container/div/div[2]/div/div[2]/div/div[2]/cc-motorization/cc-motorization-filters-section/cc-motorization-filters/form/fieldset[1]/div[2]/div[1]/wb-checkbox-control/label/wb-icon")));

        //scroll to the fuel fields
        WebElement fuel_type = driver.findElement(By.xpath(Utils.FUEL_TABLE_XPATH));
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView();", fuel_type);

        //waiting until scroll is effective by checking that the right arrow is clickable
        WebDriverWait wait2 = new WebDriverWait(driver, 30);
        wait2.until(ExpectedConditions.elementToBeClickable(By.xpath(Utils.MODELS_RIGHT_SLIDER_XPATH)));


        //Check what kinds of fuel exist and compare with expected
        List<WebElement> fuel_types = driver.findElements(By.xpath(Utils.FUEL_LABELS_XPATH));
        assertEquals(fuel_types.size(), Utils.FUEL_TYPES.size());
        System.out.println("Total number of fuel types: " + fuel_types.size());

        for (int i=0;i < Utils.FUEL_TYPES.size(); i++) {
            System.out.println("Checking fuel type label for " + Utils.FUEL_TYPES.get(i) + " in position " + (i+1));
            assertEquals(fuel_types.get(i).getText(),Utils.FUEL_TYPES.get(i));

        }
        //select the desired fuel
        //go through the fuel list and choose the desired one

        for (int i=0;i < Utils.FUEL_TYPES.size(); i++) {
            if(fuel_types.get(i).getText().equals(Utils.DESIRED_FUEL)){
                System.out.println("Found the desired fuel in position " + (i+1));
                fuel_types.get(i).click();
            }

        }
        System.out.println("Proceeding to validate prices");

    }

    @Test(testName = "Validate Prices and get screenshots", dependsOnMethods = {"openSite", "ValidateBasicContent", "ValidateBodiesAndSelect", "BuildCar", "FilterByFuel"})
    public void ValidatePrices() throws IOException {

        //Set the timestamp for test starting for file name generation
        String filename = String.valueOf(System.currentTimeMillis())+"_iter_";

        //options checker in (a-b / total) format. Must be stripped by each one of the elements in order to know how many prices there are and screenshots to take
        String options_text=driver.findElement(By.cssSelector(Utils.MODELS_PAGINATION_CSS_SELECTOR)).getText();

        String[] splitted_text = options_text.split("\\s+");
        int a = parseInt(splitted_text[0]);
        int b = parseInt(splitted_text[2]);
        int total = parseInt(splitted_text[4]);

        //get all the prices and convert them to integer in order to be sortable
        int[] prices = new int[total];
        //prices are only visible when car is visible on screen as well, so it is necessary to scroll to the right to see the prices
        for (int i=a-1; i<(total-b); i++){
            //check first price visible
            List<WebElement> pricelist = driver.findElements(By.cssSelector(".cc-motorization-header__price"));

            prices[i] = Integer.parseInt(pricelist.get(i).getText().replace(",","").replace("£", ""));
            System.out.println("Price for option "+i+" is " + prices[i]);
            //Take the screenshot
            System.out.println("Saving the screenshot for iteration "+i+" of "+(total - b - 1));
            this.takeSnapShot(driver, Utils.FOLDER+filename+String.valueOf(i)+".png");

            //scroll 1 to the right. Max number of scrolls will be "i", that ensure that the last (b-a) items will be visible
            driver.findElement(By.xpath(Utils.MODELS_RIGHT_SLIDER_XPATH)).click();

            //if we are already in the last visible page: i = (total-b), do j=i --> j<total
            if(i == (total - b - 1)){
                for(int j = i + 1; j < total; j++) {
                    prices[j] = Integer.parseInt(pricelist.get(j).getText().replace(",","").replace("£", ""));
                    System.out.println("Price for option "+j+" is " + prices[j]);

                }
            }

        }
        //Find lower and higher prices and check that are between the values
        int maxprice = findMax(prices);
        int minprice = findMin(prices);


        assertTrue(minprice > Utils.MIN_PRICE);
        assertTrue(maxprice < Utils.MAX_PRICE);

        //Write values to file
        writeToFile("Max Price found is £"+maxprice,Utils.FOLDER+filename+"priceRange.txt");
        writeToFile("Min Price found is £"+minprice,Utils.FOLDER+filename+"priceRange.txt");
    }




    private void CheckCookiesAndProceed(WebDriver driver) {

        WebElement host = driver.findElement(By.cssSelector(Utils.COOKIES_HELPER));
        JavascriptExecutor js = (JavascriptExecutor)driver;
        WebElement shadowRoot = (WebElement)(js.executeScript("return arguments[0].shadowRoot", host));
        shadowRoot.findElement(By.cssSelector(Utils.COOKIES_ACCEPT_BUTTON)).click();
    }

    public static void takeSnapShot(WebDriver webdriver,String fileWithPath) throws IOException {
        //Convert web driver object to TakeScreenshot
        TakesScreenshot scrShot =((TakesScreenshot)webdriver);
        //Call getScreenshotAs method to create image file
        File SrcFile=scrShot.getScreenshotAs(OutputType.FILE);
        //Move image file to new destination
        File DestFile=new File(fileWithPath);
        //Copy file at destination
        FileUtils.copyFile(SrcFile, DestFile);
    }

    private int findMax(int[] numbers){
        int max = numbers[0];
        for (int i=1;i< numbers.length; i++){
            if (numbers[i] > max) {
                max = numbers[i];
            }
        }
        return max;
    }

    private int findMin(int[] numbers) {
        int min = numbers[0];
        for (int i=1;i< numbers.length; i++){
            if (numbers[i] < min) {
                min = numbers[i];
            }
        }
        return min;
    }
    private void writeToFile(String str, String fileName) throws IOException {
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true));
            writer.append(' ');
            writer.append(str);
            writer.close();
    }

    @AfterSuite
    public static void cleanup(){

        driver.manage().deleteAllCookies();
        driver.close();
    }
}
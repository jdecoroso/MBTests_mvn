package Test1;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class Utils {
    final static String MERC_UK_URL = "https://www.mercedes-benz.co.uk";
    final static String CHROME_DRIVER ="src\\chromedriver.exe";
    final static String BODY = "Hatchbacks";
//    Web Page items
    final static List<String> BODY_TYPES = Arrays.asList("Hatchbacks", "Saloons", "Estates", "SUVs", "Coupés", "Cabriolets", "Roadsters", "MPVs", "See All");
    final static String MODEL_CSS = "vmos_jQyeG";
    final static String MODEL_CSS_SELECTOR = "a."+MODEL_CSS;
    final static String A_CLASS_TEXT = "A-Class";
    final static String A_CLASS_XPATH = "//div/div/a/div/div/span/div/span[contains(text(), "+A_CLASS_TEXT+")][@class=\"vmos_1PW4e\"]";
    //To Be improved
    public static String BUILD_BUTTON_XPATH = "//wb-popover[1]/ul/li[1]/a";
    public static String FUEL_TYPE_CSS_SELECTOR = ".cc-motorization-filters--view-column";
    public static final String FUEL_TABLE_XPATH = "//fieldset[1]" ;
    public static String FUEL_LABELS_XPATH = "//fieldset[1]/div[2]/div/wb-checkbox-control/label";
    public static List<String> FUEL_TYPES = Arrays.asList("Petrol", "Diesel", "Hybrid");
    public static String DESIRED_FUEL = "Diesel";
}

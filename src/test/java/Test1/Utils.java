package Test1;

import java.util.Arrays;
import java.util.List;

public class Utils {

    //These are good candidates to be set as properties
    final static String MERC_URL = "https://www.mercedes-benz.co.uk";
    final static String BODY = "Hatchbacks";
    final static String MODEL_TEXT = "A-Class";
    final static int MAX_PRICE = 60000;
    final static int MIN_PRICE = 15000;
    public static String DESIRED_FUEL = "Diesel";




    public static String FOLDER = "c:\\snapshots\\";

    //    Web Page items
    final static List<String> BODY_TYPES = Arrays.asList("Hatchbacks", "Saloons", "Estates", "SUVs", "Coupés", "Cabriolets", "Roadsters", "MPVs", "See All");
    final static String MODEL_CSS = "vmos_jQyeG";
    public static final String MAIN_PAGE_TEXT_VALIDATOR = "Mercedes-Benz Passenger Cars";

    final static String A_CLASS_XPATH = "//div/div/a/div/div/span/div/span[contains(text(), "+ MODEL_TEXT +")][@class=\"vmos_1PW4e\"]";
    //To Be improved
    public static String BUILD_BUTTON_XPATH = "//wb-popover[1]/ul/li[1]/a";
    public static final String FUEL_TABLE_XPATH = "//fieldset[1]" ;
    public static String FUEL_LABELS_XPATH = "//fieldset[1]/div[2]/div/wb-checkbox-control/label";
    public static List<String> FUEL_TYPES = Arrays.asList("Diesel", "Petrol", "Hybrid");
    public static String MODELS_PAGINATION_CSS_SELECTOR = ".cc-motorization-comparsion-status__pagination";
    public static String MODELS_RIGHT_SLIDER_XPATH = "//cc-motorization-comparison/div/div/cc-slider/div/cc-slider-ui-container/cc-slider-buttons/div/button[2]/div";
    public static String HOME_PAGE_READY_XPATH = "//div[@class=\"nextstepnavigation__base\"]";
    public static String COOKIES_HELPER ="[settings-id=\"fph8XBqir\"]";
    public static String COOKIES_ACCEPT_BUTTON = "[data-test=\"handle-accept-all-button\"]";

}

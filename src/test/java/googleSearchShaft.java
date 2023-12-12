import com.shaft.driver.SHAFT;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class googleSearchShaft {

    SHAFT.GUI.WebDriver ShaftDriver;
    SHAFT.TestData.JSON testData;

    private final By googleSearchBar_textarea = By.xpath("//textarea[@jsname='yZiJbe']");
    private final By firstSearchResult_h3 = By.xpath("(//h3[@class='LC20lb MBeuO DKV0Md'])[1]");

    @BeforeClass
    public void beforeClass() {
        testData = new SHAFT.TestData.JSON("src/test/resources/TestData/TestData.json");
    }

    @BeforeMethod
    public void beforeMethod() {
        ShaftDriver = new SHAFT.GUI.WebDriver();

        ShaftDriver.browser().navigateToURL("https://www.google.com/");
    }

    @Test
    public void testSearchGiza() {
        ShaftDriver.element()
                .type(googleSearchBar_textarea, testData.getTestData("searchDataGiza"))
                .keyPress(googleSearchBar_textarea, Keys.ENTER);

        ShaftDriver.element().assertThat(firstSearchResult_h3)
                .text().isEqualTo(testData.getTestData("expectedFirstSearchGiza"))
                .perform();
    }

    @Test
    public void testSearchSelenium() {
        ShaftDriver.element()
                .type(googleSearchBar_textarea, testData.getTestData("searchDataSelenium"))
                .keyPress(googleSearchBar_textarea, Keys.ENTER);

        ShaftDriver.element().assertThat(firstSearchResult_h3)
                .text().isEqualTo(testData.getTestData("expectedFirstSearchSelenium"))
                .perform();
    }

}
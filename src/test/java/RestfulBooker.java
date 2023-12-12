import RestfulBooker.objects.APISBooking;
import RestfulBooker.objects.APIs;
import com.shaft.driver.SHAFT;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class RestfulBooker {

    private SHAFT.API api;

    private APISBooking apisBooking;




    @Test()
    public void createBookingTest(){
        APISBooking.createBooking("Heba", "Abdelaziz", "Coffee");
        APISBooking.validateThatTheBookingIsCreated("Heba","Abdelaziz","Coffee");

    }

    @Test(dependsOnMethods = {"createBookingTest"})
    public void deleteBookingTest(){
        APISBooking.deleteBooking("Heba","Abdelaziz");
        APISBooking.validateThatTheBookingDeleted();
    }

    @Test(dependsOnMethods = {"createBookingTest"})
    public void testGetBookingID() {
        String ids = APISBooking.getBookingId("Heba", "Abdelaziz");
        SHAFT.Report.log(ids);
    }

    @BeforeClass
    public void beforeClass(){
        api = new SHAFT.API(APIs.baseUrl);
        APIs.login(api, "admin", "password123");
        apisBooking = new APISBooking(api);
    }

}
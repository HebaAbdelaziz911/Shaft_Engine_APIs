package RestfulBooker.objects;

import com.shaft.driver.SHAFT;
import io.restassured.http.ContentType;

public class APISBooking {
    public static String booking_serviceName = "/booking";
    private static SHAFT.API api;

    public APISBooking(SHAFT.API api) {
        this.api = api;}
    private static String BookingID;
 


    public static void createBooking(String firstname, String lastname, String additionalneeds){
        String CreateNewBooking = """
                    {
                    "firstname" : "{FIRST_NAME}",
                    "lastname" : "{LAST_NAME}",
                    "totalprice" : 111,
                    "depositpaid" : true,
                    "bookingdates" : {
                        "checkin" : "2022-01-01",
                        "checkout" : "2023-01-01"
                    },
                    "additionalneeds" : "{ADDITIONAL_NEED}"
                    }
                    """
                .replace("{FIRST_NAME}", firstname)
                .replace("{LAST_NAME}", lastname)
                .replace("{ADDITIONAL_NEED}", additionalneeds);
        api.post(booking_serviceName)
                .setContentType(ContentType.JSON)
                .setRequestBody(CreateNewBooking)
                .setTargetStatusCode(APIs.SUCCESS)
                .perform();

        api.verifyThatResponse().extractedJsonValue("booking.lastname").isEqualTo("Abdelaziz").perform();
        BookingID = api.getResponseJSONValue("bookingid");
        api.assertThatResponse().body().contains("\"bookingid\"").perform();


    }

    public static String getBookingId(String firstName, String lastName) {
        api.get(booking_serviceName)
                .setUrlArguments("firstname=" + firstName + "&lastname=" + lastName)
                .setTargetStatusCode(APIs.SUCCESS)
                .perform();
        return api.getResponseJSONValue("$[0].bookingid");
    }


    public static void deleteBooking(String firstname, String lastname){

        BookingID  = getBookingId(firstname , lastname);
        api.delete(booking_serviceName +"/"+ BookingID)
                .addHeader("Cookie","token=" + APIs.MyToken)
                .setTargetStatusCode(APIs.SUCCESS_DELETE)
                .perform();
        api.assertThatResponse().body().contains("Created").perform();
    }


/////////////////////// validations \\\\\\\\\\\\\\\\\\\\
    public static void validateThatTheBookingIsCreated(String expectedFirstName, String expectedLastName, String expectedAddNeeds){

        api.verifyThatResponse().extractedJsonValue("booking.firstname").isEqualTo(expectedFirstName).perform();
        api.verifyThatResponse().extractedJsonValue("booking.lastname").isEqualTo(expectedLastName).perform();
        api.verifyThatResponse().extractedJsonValue("booking.additionalneeds").isEqualTo(expectedAddNeeds).perform();
        api.verifyThatResponse().body().contains("\"bookingid\":").perform();
    }


    public static void validateThatTheBookingDeleted() {
        api.assertThatResponse().body().isEqualTo("Created").perform();


    }
}

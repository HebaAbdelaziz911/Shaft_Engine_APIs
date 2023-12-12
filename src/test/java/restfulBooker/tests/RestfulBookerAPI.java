package restfulBooker.tests;

import com.shaft.driver.SHAFT;
import io.restassured.http.ContentType;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

    public class RestfulBookerAPI {

        private SHAFT.API api;
        String MyToken;
        String BookingID;

        /// Services name
        private final String authentication_serviceName = "/auth";
        private final String booking_serviceName = "/booking";
        private final int success_statusCode = 200;
        private final int success_DeletedstatusCode = 201;


        @Test()
        public void createBookingTest(){
            createBooking("Heba", "Abdelaziz", "Coffee");
            validateThatTheBookingIsCreated("Heba","Abdelaziz","Coffee");

        }

        @Test(dependsOnMethods = {"createBookingTest"})
        public void deleteBookingTest(){
            deleteBooking("Heba","Abdelaziz");
            //validateThatTheBookingDeleted();
        }

        @Test(dependsOnMethods = {"createBookingTest"})
        public void testGetBookingID() {
            String ids = getBookingId("Heba", "Abdelaziz");
            SHAFT.Report.log(ids);
        }

        @BeforeClass
        public void beforeClass(){
            api = new SHAFT.API("https://restful-booker.herokuapp.com");
            login("admin", "password123");
        }

        ////////////////////// Config \\\\\\\\\\\\\\\\\\\\\\\

        public void login(String username, String password){
            String tokenBody = """
                {
                    "username" : "{USERNAME}",
                    "password" : "{PASSWORD}"
                }
                """
                    .replace("{USERNAME}", username)
                    .replace("{PASSWORD}", password);

            api.post(authentication_serviceName)
                    .setContentType(ContentType.JSON)
                    .setRequestBody(tokenBody)
                    .setTargetStatusCode(success_statusCode)
                    .perform();



            api.assertThatResponse().body().contains("\"token\":").perform();
            MyToken = api.getResponseJSONValue("token");
            api.addHeader("Cookie","token=" + MyToken);
        }

        public void createBooking(String firstname, String lastname, String additionalneeds){
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
                    .setTargetStatusCode(success_statusCode)
                    .perform();

            api.verifyThatResponse().extractedJsonValue("booking.lastname").isEqualTo("Abdelaziz").perform();
            BookingID = api.getResponseJSONValue("bookingid");
            api.assertThatResponse().body().contains("\"bookingid\"").perform();


        }

        public String getBookingId(String firstName, String lastName) {
            api.get(booking_serviceName)
                    .setUrlArguments("firstname=" + firstName + "&lastname=" + lastName)
                    .perform();
            return api.getResponseJSONValue("$[0].bookingid");
        }

        public void validateThatTheBookingIsCreated(String expectedFirstName, String expectedLastName, String expectedAddNeeds){

            api.verifyThatResponse().extractedJsonValue("booking.firstname").isEqualTo(expectedFirstName).perform();
            api.verifyThatResponse().extractedJsonValue("booking.lastname").isEqualTo(expectedLastName).perform();
            api.verifyThatResponse().extractedJsonValue("booking.additionalneeds").isEqualTo(expectedAddNeeds).perform();
            api.verifyThatResponse().body().contains("\"bookingid\":").perform();
        }


        public void deleteBooking(String firstname, String lastname){

            BookingID  = getBookingId(firstname , lastname);
            api.delete(booking_serviceName +"/"+ BookingID)
                    .addHeader("Cookie","token=" + MyToken)
                    .setTargetStatusCode(success_DeletedstatusCode)
                    .perform();

            api.assertThatResponse().body().contains("Created").perform();

        }

        private void validateThatTheBookingDeleted() {
            api.assertThatResponse().body().isEqualTo("Created").perform();


        }


    }
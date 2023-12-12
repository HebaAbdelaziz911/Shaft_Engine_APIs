package RestfulBooker.objects;

import com.shaft.driver.SHAFT;
import io.restassured.http.ContentType;

public class APIs {

//    SHAFT.API api;
//
public static  String MyToken;

    public static String baseUrl =System.getProperty("baseUrl");

    public static String authentication_serviceName = "/auth";

    //////// status code \\\\\\\\\\\\\
    public static int SUCCESS = 200;
    public static int SUCCESS_DELETE = 201;

    public static void login(SHAFT.API api, String username, String password){
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
                .setTargetStatusCode(SUCCESS)
                .perform();



        api.assertThatResponse().body().contains("\"token\":").perform();
        MyToken = api.getResponseJSONValue("token");
        api.addHeader("Cookie","token=" + MyToken);
    }

}

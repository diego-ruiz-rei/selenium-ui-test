package gov.uscis.accountspublic.helpers;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import net.serenitybdd.core.SerenitySystemProperties;
import net.thucydides.core.ThucydidesSystemProperty;
import org.junit.Assert;

import static io.restassured.RestAssured.given;

/**
 * Common rest requests
 */
public class RestRequests {

    private RestRequests () {}

    private static String getApplicantBearerToken () {
        ValidatableResponse response = given().auth()
                .basic("automation.applicant@gmail.com", "AUTOmation123$")
                .when()
                .get("https://cis-ap-agency-ui-cis-ap-dev.apps.ocp.reicicd1001.net/auth/realms/accounts-public/account")
                .then();
        System.out.println(response);
        if ( response.extract().statusCode() == 200 )
            return response.contentType(ContentType.HTML).extract().response().cookie("KC_RESTART");
        return null;
    }

    public static void deleteI485 () {
        String bearerToken = getApplicantBearerToken();
        if ( bearerToken != null ) {
            Response response = given().auth().oauth2(bearerToken)
                    .when().log().all()
                    .delete("api/v1/benefit/i485/deleteI485")
                    .then().log().all()
                    .statusCode(204).extract().response();
            Assert.assertTrue("Response of 'delete I485' request was not empty: " + response.asString(), response.asString().isEmpty());
        }
    }
}

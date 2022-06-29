package org.acme;

import javax.ws.rs.core.MediaType;

import io.quarkus.test.junit.QuarkusTest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class TaskResourceTest {

	@Test
	public void testCreationAndDisposal() {
		// If we haven't restarted quarkus, the value of the counter might not be 0
		Long initialTotal = getTotal();

		// Start a new task and then dispose of it
		given().when()
				.get( "/task/start" )
				.then()
				.statusCode( 200 )
				// Check that the id of the task is correct
				.body( containsString( "Started task " + ( initialTotal + 1 ) ) );

		Long current = getTotal();

		// it should have decreased the counter to the original value while disposing of the bean
		Assertions.assertEquals( initialTotal, current );
	}

	/**
	 * @return the current total from the rest API
	 */
	private Long getTotal() {
		String response1 = given()
				.when().get( "/task/total" )
				.then().statusCode( 200 )
				.contentType( MediaType.TEXT_PLAIN )
				.extract().asString();

		return Long.valueOf( response1 );
	}

}
package pl.s.h.interview.controller;

import org.junit.jupiter.api.Test;
import pl.s.h.interview.BaseIT;
import pl.s.h.interview.api.OccupancyPlanBuildRequest;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;

class OccupancyPlanControllerIT extends BaseIT {

    private static final String OCCUPANCY_PLAN_BUILD_URI = "/occupancy/plan/build";

    @Test
    void shouldCallBuildEndpoint() {
        final OccupancyPlanBuildRequest request = new OccupancyPlanBuildRequest();

        given()
            .contentType(JSON)
            .body(request)
        .when()
            .post(OCCUPANCY_PLAN_BUILD_URI)
        .then()
            .statusCode(200);
    }
}

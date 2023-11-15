package pl.s.h.interview.controller;

import org.junit.jupiter.api.Test;
import pl.s.h.interview.BaseIT;
import pl.s.h.interview.api.ErrorResponse;
import pl.s.h.interview.api.OccupancyPlanBuildRequest;
import pl.s.h.interview.api.RoomAvailability;
import pl.s.h.interview.api.RoomType;
import pl.s.h.interview.api.ServiceError;

import java.time.LocalDate;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static io.restassured.http.ContentType.TEXT;
import static org.assertj.core.api.Assertions.assertThat;

class OccupancyPlanControllerIT extends BaseIT {

    private static final String OCCUPANCY_PLAN_BUILD_URI = "/api/occupancy/plan/build";

    @Test
    void shouldCallBuildEndpoint() {
        final OccupancyPlanBuildRequest request = buildOccupancyPlanBuildRequest();

        given()
            .contentType(JSON)
            .body(request)
        .when()
            .post(OCCUPANCY_PLAN_BUILD_URI)
        .then()
            .statusCode(200);
    }

    @Test
    void shouldReturnValidationErrorResponseWhenRequestContentTypeIsNotSupported() {

        final ErrorResponse response = given()
                .contentType(TEXT)
                .when()
                .post(OCCUPANCY_PLAN_BUILD_URI)
                .then()
                .statusCode(415)
                .extract()
                .as(ErrorResponse.class);

        assertThat(response).isNotNull();
        assertThat(response.errors()).hasSize(1);
        final ServiceError serviceError = response.errors().get(0);
        assertThat(serviceError.code()).isEqualTo("80001");
        assertThat(serviceError.message()).isEqualTo("Content-Type 'text/plain;charset=ISO-8859-1' is not supported.");
    }

    private static OccupancyPlanBuildRequest buildOccupancyPlanBuildRequest() {
        return OccupancyPlanBuildRequest.builder()
                .planDate(LocalDate.now())
                .roomAvailability(RoomAvailability.builder()
                        .type(RoomType.ECONOMY)
                        .availableRooms(1)
                        .build())
                .guestPriceProposalPerNight(0)
                .build();
    }
}

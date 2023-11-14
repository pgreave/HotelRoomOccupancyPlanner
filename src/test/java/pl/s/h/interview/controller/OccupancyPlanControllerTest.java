package pl.s.h.interview.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pl.s.h.interview.api.OccupancyPlanBuildRequest;
import pl.s.h.interview.api.OccupancyPlanBuildResponse;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class OccupancyPlanControllerTest {

    private OccupancyPlanController occupancyPlanController;

    @BeforeEach
    void setup() {
        occupancyPlanController = new OccupancyPlanController();
    }

    @Test
    void shouldRespondWithSuccess() {
        // given
        OccupancyPlanBuildRequest request = new OccupancyPlanBuildRequest();

        // when
        final ResponseEntity<OccupancyPlanBuildResponse> result = occupancyPlanController.buildOccupancyPlan(request);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}

package pl.s.h.interview.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pl.s.h.interview.api.OccupancyPlanBuildRequest;
import pl.s.h.interview.api.OccupancyPlanBuildResponse;
import pl.s.h.interview.service.OccupancyPlanService;

import java.time.LocalDate;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OccupancyPlanControllerTest {

    @Mock
    private OccupancyPlanService occupancyPlanService;
    private OccupancyPlanController occupancyPlanController;

    @BeforeEach
    void setup() {
        occupancyPlanController = new OccupancyPlanController(occupancyPlanService);
    }

    @Test
    void shouldCallServiceAndRespondWithSuccess() {
        // given
        OccupancyPlanBuildRequest request = new OccupancyPlanBuildRequest(LocalDate.now(), Collections.emptyList(), Collections.emptyList());

        when(occupancyPlanService.buildOccupancyPlan(eq(request))).thenReturn(new OccupancyPlanBuildResponse(Collections.emptyList()));

        // when
        final ResponseEntity<OccupancyPlanBuildResponse> result = occupancyPlanController.buildOccupancyPlan(request);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);

        verify(occupancyPlanService).buildOccupancyPlan(eq(request));
    }
}

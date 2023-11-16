package pl.s.h.interview.web.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pl.s.h.interview.api.OccupancyPlanBuildRequest;
import pl.s.h.interview.api.OccupancyPlanBuildResponse;
import pl.s.h.interview.service.planning.OccupancyPlanService;
import pl.s.h.interview.web.validator.OccupancyPlanBuildRequestValidator;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OccupancyPlanControllerTest {

    @Mock
    private OccupancyPlanService occupancyPlanService;
    @Mock
    private OccupancyPlanBuildRequestValidator occupancyPlanBuildRequestValidator;
    private OccupancyPlanController occupancyPlanController;

    @BeforeEach
    void setup() {
        occupancyPlanController = new OccupancyPlanController(occupancyPlanService, occupancyPlanBuildRequestValidator);
    }

    @Test
    void shouldCallServiceAndRespondWithSuccess() {
        // given
        OccupancyPlanBuildRequest request = new OccupancyPlanBuildRequest(LocalDate.now(), Collections.emptyList(), Collections.emptyList());

        when(occupancyPlanService.buildPlan(eq(request))).thenReturn(buildEmptyResponse());

        // when
        final ResponseEntity<OccupancyPlanBuildResponse> result = occupancyPlanController.buildOccupancyPlan(request);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);

        verify(occupancyPlanService).buildPlan(eq(request));
    }

    private static OccupancyPlanBuildResponse buildEmptyResponse() {
        return new OccupancyPlanBuildResponse(UUID.randomUUID().toString(), Collections.emptyList(), LocalDateTime.now());
    }
}

package pl.s.h.interview.service.planning;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import pl.s.h.interview.api.OccupancyPlanBuildRequest;
import pl.s.h.interview.api.OccupancyPlanBuildResponse;
import pl.s.h.interview.api.RoomAvailability;
import pl.s.h.interview.service.common.PlanIdGenerator;
import pl.s.h.interview.service.planning.strategy.RoomOccupancyPlanStrategy;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class OccupancyPlanServiceTest {

    @Mock
    private PlanIdGenerator planIdGenerator;

    @Mock
    private RoomOccupancyPlanStrategy occupancyPlanStrategy;

    private OccupancyPlanService sut;

    @BeforeEach
    void setup() {
        planIdGenerator = mock(PlanIdGenerator.class);
        occupancyPlanStrategy = mock(RoomOccupancyPlanStrategy.class);
        sut = new OccupancyPlanService(planIdGenerator, occupancyPlanStrategy);
    }

    @Test
    void shouldCallStrategy() {
        // given
        final List<RoomAvailability> roomAvailabilities = List.of();
        final List<BigDecimal> prices = List.of();
        OccupancyPlanBuildRequest request = OccupancyPlanBuildRequest.builder()
                .roomAvailabilities(roomAvailabilities)
                .guestPriceProposalsPerNight(prices)
                .build();

        // when
        final OccupancyPlanBuildResponse result = sut.buildPlan(request);

        // then
        assertThat(result).isNotNull();

        verify(occupancyPlanStrategy).buildPlan(eq(roomAvailabilities), eq(prices));
    }

    @Test
    void shouldGeneratePlanId() {
        // given
        OccupancyPlanBuildRequest request = OccupancyPlanBuildRequest.builder().build();
        when(planIdGenerator.get()).thenReturn("mockedPlanId");

        // when
        final OccupancyPlanBuildResponse result = sut.buildPlan(request);

        // then
        assertThat(result).isNotNull();
        assertThat(result.buildPlanId()).isEqualTo("mockedPlanId");

        verify(planIdGenerator).get();
    }

    @Test
    void shouldReturnCurrentPlanGenerationDateTime() {
        // given
        final LocalDateTime timeBeforeExecution = LocalDateTime.now();
        OccupancyPlanBuildRequest request = OccupancyPlanBuildRequest.builder().build();

        // when
        final OccupancyPlanBuildResponse result = sut.buildPlan(request);

        // then
        final LocalDateTime timeAfterExecution = LocalDateTime.now();
        assertThat(result).isNotNull();
        assertThat(result.planGenerationDate()).isBetween(timeBeforeExecution, timeAfterExecution);
    }
}

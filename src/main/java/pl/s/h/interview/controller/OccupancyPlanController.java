package pl.s.h.interview.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.s.h.interview.api.OccupancyPlanBuildRequest;
import pl.s.h.interview.api.OccupancyPlanBuildResponse;
import pl.s.h.interview.service.OccupancyPlanService;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/occupancy/plan")
public class OccupancyPlanController {

    private final OccupancyPlanService occupancyPlanService;

    @PostMapping(value = "/build", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OccupancyPlanBuildResponse> buildOccupancyPlan(@Valid @RequestBody OccupancyPlanBuildRequest request) {
        return ResponseEntity.ok(occupancyPlanService.buildOccupancyPlan(request));
    }
}

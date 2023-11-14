package pl.s.h.interview.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.s.h.interview.api.OccupancyPlanBuildRequest;
import pl.s.h.interview.api.OccupancyPlanBuildResponse;

@RestController
@RequestMapping(value = "/occupancy/plan")
public class OccupancyPlanController {

    @PostMapping(value = "/build", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OccupancyPlanBuildResponse> buildOccupancyPlan(@RequestBody OccupancyPlanBuildRequest request) {
        return ResponseEntity.ok(new OccupancyPlanBuildResponse());
    }
}

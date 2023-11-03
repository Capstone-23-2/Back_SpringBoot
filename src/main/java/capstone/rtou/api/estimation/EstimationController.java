package capstone.rtou.api.estimation;

import capstone.rtou.api.estimation.dto.EstimationResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("api")
public class EstimationController {

    private final EstimationService estimationService;

    public EstimationController(EstimationService estimationService) {
        this.estimationService = estimationService;
    }

    @GetMapping("/estimation")
    ResponseEntity<EstimationResponse> getEstimation(@RequestParam String userId, @RequestParam String conversationId) {

        EstimationResponse estimationResponse = estimationService.getEstimation(userId, conversationId);

        return ResponseEntity.ok().body(estimationResponse);
    }
}

package capstone.rtou.api.estimation;

import capstone.rtou.api.estimation.dto.EstimationResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "발음 평가 API", description = "사용자의 발음을 평가한 결과들을 가져옴.")
@RestController
@Slf4j
@RequestMapping("api")
public class EstimationController {

    private final EstimationService estimationService;

    public EstimationController(EstimationService estimationService) {
        this.estimationService = estimationService;
    }

    @GetMapping("/estimation")
    ResponseEntity<EstimationResponse> getEstimation(@RequestParam String userId) {

        EstimationResponse estimationResponse = estimationService.getEstimation(userId);

        return ResponseEntity.ok().body(estimationResponse);
    }
}

package capstone.rtou.api.estimation;

import capstone.rtou.api.estimation.dto.EstimationResponse;
import capstone.rtou.api.estimation.dto.EstimationScoreResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "발음 평가 API", description = "사용자의 발음을 평가한 결과들을 가져옴.")
@RestController
@Slf4j
@RequestMapping("api/estimation")
public class EstimationController {

    private final EstimationService estimationService;

    public EstimationController(EstimationService estimationService) {
        this.estimationService = estimationService;
    }

    @Operation(summary = "현재 음성 결과", description = "사용자가 본인의 음성의 정확도, 능숙도, 완성도, 발음에 대한 점수와 틀린 부분을 알 수 있다.")
    @ApiResponse(responseCode = "200", description = "status가 false인 경우 사용자의 음성 평가 결과가 없음, true인 경우 사용자의 음성 평가 결과 응답.", content = @Content(schema = @Schema(implementation = EstimationScoreResponse.class)))
    @GetMapping("/{userId}/{estimationId}")
    ResponseEntity<EstimationScoreResponse> getEstimation(@Validated @PathVariable String userId, @Validated @PathVariable String estimationId) {

        EstimationScoreResponse estimationScoreResponse = estimationService.getEstimation(userId, estimationId);

        return ResponseEntity.ok().body(estimationScoreResponse);
    }

    @Operation(summary = "사용자별 음성 평가 기록", description = "사용자가 본인의 음성 평가에 대한 전체 기록을 조회할 수 있음.")
    @ApiResponse(responseCode = "200", description = "status가 true이면 사용자의 전체 평가 기록 조회 가능, status가 false이면 음성 평가에 대한 기록이 없음.", content = @Content(schema = @Schema(implementation = EstimationResponse.class)))
    @GetMapping("/{userId}/estimations")
    ResponseEntity<EstimationResponse> getAllEstimationById(@PathVariable String userId) {
        EstimationResponse estimationResponse = estimationService.getAllEstimationById(userId);

        return ResponseEntity.ok().body(estimationResponse);
    }
}

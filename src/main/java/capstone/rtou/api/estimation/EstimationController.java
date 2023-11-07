package capstone.rtou.api.estimation;

import capstone.rtou.api.conversation.dto.ConversationResponse;
import capstone.rtou.api.estimation.dto.EstimationResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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

    @Operation(summary = "음성 평가 API", description = "사용자가 본인의 음성의 정확도, 능숙도, 완성도, 발음에 대한 점수와 틀린 부분을 알 수 있다.")
    @ApiResponse(responseCode = "200", description = "status가 false인 경우 사용자의 음성 평가 결과가 없음, true인 경우 사용자의 음성 평가 결과 응답.", content = @Content(schema = @Schema(implementation = ConversationResponse.class)))
    @GetMapping("/estimation")
    ResponseEntity<EstimationResponse> getEstimation(@RequestParam String userId) {

        EstimationResponse estimationResponse = estimationService.getEstimation(userId);

        return ResponseEntity.ok().body(estimationResponse);
    }
}

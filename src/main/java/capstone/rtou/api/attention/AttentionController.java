package capstone.rtou.api.attention;

import capstone.rtou.api.attention.dto.AttentionRequestDto;
import capstone.rtou.api.attention.dto.AttentionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "출석 API", description = "출석 체크, 전체 출석 조회 API")
@RestController
@Slf4j
@RequestMapping("api/attention")
public class AttentionController {

    private final AttentionService attentionService;

    public AttentionController(AttentionService attentionService) {
        this.attentionService = attentionService;
    }

    @Operation(summary = "출석 체크", description = "사용자가 출석 등록")
    @ApiResponse(responseCode = "200", description = "status가 true인 경우 출석 체크 완료, " +
            "false인 경우, 잘못된 사용자 접근 또는 이미 출석 등록", content = @Content(schema = @Schema(implementation = AttentionResponse.class)))
    @PostMapping("/attend")
    ResponseEntity<AttentionResponse> attention(@RequestBody @Validated AttentionRequestDto attentionRequest) {

        AttentionResponse attentionResponse = attentionService.attention(attentionRequest);

        return ResponseEntity.ok().body(attentionResponse);
    }

    @Operation(summary = "전체 출석 기록 조회", description = "사용자가 전체 출석 기록을 조회")
    @ApiResponse(responseCode = "200", description = "status가 true인 경우, 전체 출석 기록 데이터 조회, " +
            "false인 경우, 한 건 이상의 출석 기록이 존재X", content = @Content(schema = @Schema(implementation = AttentionResponse.class)))
    @GetMapping("/attendances")
    ResponseEntity<AttentionResponse> getAttentionList(@RequestParam @Validated String userId) {

        AttentionResponse attentionResponse = attentionService.findAttentionById(userId);

        return ResponseEntity.ok().body(attentionResponse);
    }
}

package capstone.rtou.api.attention;

import capstone.rtou.api.attention.dto.AttentionRequestDto;
import capstone.rtou.api.attention.dto.AttentionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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

    @PostMapping("/attend")
    ResponseEntity<AttentionResponse> attention(@RequestBody @Validated AttentionRequestDto attentionRequest) {

        AttentionResponse attentionResponse = attentionService.attention(attentionRequest);

        return ResponseEntity.ok().body(attentionResponse);
    }

    @GetMapping("/attendances")
    ResponseEntity<AttentionResponse> getAttentionList(@RequestParam @Validated String userId) {

        AttentionResponse attentionResponse = attentionService.findAttentionById(userId);

        return ResponseEntity.ok().body(attentionResponse);
    }
}

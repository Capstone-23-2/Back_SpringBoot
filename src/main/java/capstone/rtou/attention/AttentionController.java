package capstone.rtou.attention;

import capstone.rtou.attention.dto.AttentionRequestDto;
import capstone.rtou.attention.dto.AttentionResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("api/attention")
public class AttentionController {

    private final AttentionService attentionService;

    public AttentionController(AttentionService attentionService) {
        this.attentionService = attentionService;
    }

    @PostMapping("attend")
    ResponseEntity<AttentionResponse> attention(@RequestBody @Validated AttentionRequestDto attentionRequest) {

        attentionService.attention(attentionRequest);

        return ResponseEntity.ok().body(new AttentionResponse());
    }
}

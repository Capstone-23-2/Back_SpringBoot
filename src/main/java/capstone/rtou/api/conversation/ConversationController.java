package capstone.rtou.api.conversation;

import capstone.rtou.api.conversation.dto.ConversationResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@Tag(name = "사용자와 AR 간 대화 API", description = "대화 시작, 다음 대화 및 발음 평가")
@RestController
@RequestMapping("api/conversation")
@Slf4j
public class ConversationController {

    private final ConversationService conversationService;

    public ConversationController(ConversationService conversationService) {
        this.conversationService = conversationService;
    }

    @Operation(summary = "대화 시작 API", description = "AR과 사용자 사이의 대화 시작.")
    @GetMapping(value = "/start", produces = MediaType.MULTIPART_FORM_DATA_VALUE )
    @ApiResponse(responseCode = "200", description = "status가 false인 경우 잘못된 접근 또는 사용할 캐릭터의 정보가 없음, true인 경우 처음 대화 음성 생성.", content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE))
    public ResponseEntity<ByteArrayResource> startConversation(@Validated @RequestParam String userId, @Validated @RequestParam String characterName) throws IOException {

        ByteArrayResource conversationResponse = conversationService.startConversation(userId, characterName);

        return ResponseEntity.ok().body(conversationResponse);
    }

    @Operation(summary = "다음 대화 API", description = "사용자의 답을 듣고 다음 대화를 함.")
    @ApiResponse(responseCode = "200", description = "status가 false인 경우 음성이 생성되지 않음, true인 경우 다음 대화 음성이 생성됨.", content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE))
    @PostMapping(value = "/audio/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ByteArrayResource> receiveAudio(@PathVariable(name = "userId") String userId,
                                                             @Validated @Parameter(description = "multipart/form-data 형식의 오디오 파일을 input으로 받음. key값은 audioFile"
                                                                     , content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE)) @RequestPart MultipartFile audioFile) throws IOException, ExecutionException, InterruptedException, TimeoutException {

        ByteArrayResource conversationResponse = conversationService.getNextAudio(userId, audioFile);

        return ResponseEntity.ok().body(conversationResponse);
    }
}

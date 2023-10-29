package capstone.rtou.conversation;

import capstone.rtou.conversation.dto.ConversationRequestDto;
import capstone.rtou.conversation.dto.ConversationResponse;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("api/conversation")
@Slf4j
public class ConversationController {

    private final ConversationService conversationService;

    public ConversationController(ConversationService conversationService) {
        this.conversationService = conversationService;
    }

    @GetMapping("/start")
    public ResponseEntity startConversation(@Validated @NotNull(message = "사용자 아이디는 필수입니다.") @RequestParam String userId, @Validated @NotNull(message = "캐릭터 이름은 필수입니다.") @RequestParam String characterName) throws IOException {

        String startConversation = conversationService.startConversation(userId, characterName);

        return ResponseEntity.ok().body(new ConversationResponse(startConversation, "대화 시작", HttpStatus.OK.value()));
    }

    @PostMapping("/audio")
    public ResponseEntity receiveAudio(@Validated @RequestBody ConversationRequestDto conversationRequestDto, @Validated @NotNull(message = "파일은 필수입니다.") @RequestPart MultipartFile audioFile ) throws IOException {

        String responseAudio = conversationService.getNextAudio(conversationRequestDto.getUserId(), audioFile);

        return ResponseEntity.ok().body(new ConversationResponse(responseAudio, "사용자 음성 저장 완료", HttpStatus.OK.value()));
    }
}

package capstone.rtou.conversation;

import capstone.rtou.conversation.dto.ConversationRequestDto;
import capstone.rtou.conversation.dto.ConversationResponse;
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
    public ResponseEntity<ConversationResponse> startConversation(@Validated @RequestParam String userId, @Validated @RequestParam String characterName) throws IOException {

        String startConversation = conversationService.startConversation(userId, characterName);

        return ResponseEntity.ok().body(new ConversationResponse(startConversation, "대화 시작", HttpStatus.OK.value()));
    }

    @PostMapping("/audio")
    public ResponseEntity<ConversationResponse> receiveAudio(@Validated @RequestBody ConversationRequestDto conversationRequestDto, @Validated @RequestPart MultipartFile audioFile ) throws IOException {

        String responseAudio = conversationService.getNextAudio(conversationRequestDto.getUserId(), audioFile);

        return ResponseEntity.ok().body(new ConversationResponse(responseAudio, "사용자 음성 저장 완료", HttpStatus.OK.value()));
    }
}

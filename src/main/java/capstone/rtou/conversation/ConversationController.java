package capstone.rtou.conversation;

import capstone.rtou.conversation.dto.ConversationRequestDto;
import capstone.rtou.conversation.dto.ConversationResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
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

    @GetMapping(value = "/start")
    public ResponseEntity<ConversationResponseDto> startConversation(@Validated @RequestParam String userId, @RequestParam String characterName, BindingResult bindingResult) throws IOException {

        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(new ConversationResponseDto(null, bindingResult.getObjectName(), bindingResult.getFieldError().toString(), HttpStatus.BAD_REQUEST.value()));
        }

        String startConversation = conversationService.startConversation(userId, characterName);

        return ResponseEntity.ok().body(new ConversationResponseDto(startConversation, "대화 시작", HttpStatus.OK.value()));
    }

    @PostMapping(value = "/audio")
    public ResponseEntity<ConversationResponseDto> receiveAudio(@Validated @RequestBody ConversationRequestDto conversationRequestDto, @Validated @RequestPart MultipartFile audioFile, BindingResult bindingResult) throws IOException, InterruptedException {

        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(new ConversationResponseDto(null, bindingResult.getObjectName(), bindingResult.getFieldError().toString(), HttpStatus.BAD_REQUEST.value()));
        }

        String responseAudio = conversationService.getNextAudio(conversationRequestDto.getUserId(), audioFile);

        return ResponseEntity.ok().body(new ConversationResponseDto(responseAudio, "사용자 음성 저장 완료", HttpStatus.OK.value()));
    }
}

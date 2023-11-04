package capstone.rtou.api.conversation;

import capstone.rtou.api.conversation.dto.ConversationResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

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

        ConversationResponse conversationResponse = conversationService.startConversation(userId, characterName);

        return ResponseEntity.ok().body(conversationResponse);
    }

    @PostMapping("/audio/{userId}")
    public ResponseEntity<ConversationResponse> receiveAudio(@PathVariable(name = "userId") String userId, @Validated @RequestPart MultipartFile audioFile) throws IOException, ExecutionException, InterruptedException, TimeoutException {

         ConversationResponse conversationResponse = conversationService.getNextAudio(userId, audioFile);

        return ResponseEntity.ok().body(conversationResponse);
    }
}

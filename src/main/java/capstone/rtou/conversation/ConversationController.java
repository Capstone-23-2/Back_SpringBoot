package capstone.rtou.conversation;

import capstone.rtou.conversation.dto.RequestDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("api/conversation")
@Slf4j
public class ConversationController {

    @Autowired
    private final ConversationService conversationService;

    public ConversationController(ConversationService conversationService) {
        this.conversationService = conversationService;
    }

    @GetMapping(value = "/start", produces = "audio/*")
    public ResponseEntity startConversation(@RequestParam String userId, @RequestParam String characterName) throws IOException {

        byte[] startConversation = conversationService.startConversation(userId, characterName);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.parseMediaType("audio/wav"));
        httpHeaders.setContentLength(startConversation.length);

        return ResponseEntity.ok().headers(httpHeaders).body(startConversation);
    }

    @PostMapping(value = "/audio", produces = "audio/*")
    public ResponseEntity receiveAudio(@RequestPart MultipartFile conversation) throws IOException {

        byte[] responseAudio = conversationService.getNextAudio(conversation);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.parseMediaType("audio/wav"));
        httpHeaders.setContentLength(responseAudio.length);

        return ResponseEntity.ok().headers(httpHeaders).body(responseAudio);
    }

    @GetMapping(value = "/end")
    public ResponseEntity endConversation() {

        conversationService.endConversation();

        return ResponseEntity.ok().body(null);
    }
}

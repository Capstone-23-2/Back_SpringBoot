package capstone.rtou.conversation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ConversationResponse {

    String url;
    String message;
    String error;
    int status;

    public ConversationResponse(String url, String message, int status) {
        this.url = url;
        this.message = message;
        this.status = status;
    }
}

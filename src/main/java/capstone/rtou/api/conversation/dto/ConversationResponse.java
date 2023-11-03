package capstone.rtou.api.conversation.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class ConversationResponse {

    boolean status;
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    String message;
    String url;
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    String conversationId;

    public ConversationResponse(boolean status, String message) {
        this.status = status;
        this.message = message;
    }

    public ConversationResponse(boolean status, String message, String url) {
        this.status = status;
        this.message = message;
        this.url = url;
    }
}

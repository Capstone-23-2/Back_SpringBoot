package capstone.rtou.api.conversation.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
public class ConversationResponse {

    boolean status;
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    String message;
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    String url;

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

package capstone.rtou.api.conversation.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
public class ConversationResponse {

    int status;
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    String message;
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    String url;

    public ConversationResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public ConversationResponse(int status, String message, String url) {
        this.status = status;
        this.message = message;
        this.url = url;
    }
}

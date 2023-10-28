package capstone.rtou.conversation.dto;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ConversationResponseDto {

    String url;
    String message;
    String error;
    int status;

    public ConversationResponseDto(String url, String message, int status) {
        this.url = url;
        this.message = message;
        this.status = status;
    }
}

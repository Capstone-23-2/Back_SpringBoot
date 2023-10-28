package capstone.rtou.conversation.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ConversationRequestDto {

    @NotNull
    String userId;
}

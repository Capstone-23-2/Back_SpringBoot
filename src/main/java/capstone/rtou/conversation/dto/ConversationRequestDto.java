package capstone.rtou.conversation.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class ConversationRequestDto {

    @NotBlank(message = "사용자 아이디는 필수입니다.")
    String userId;
}

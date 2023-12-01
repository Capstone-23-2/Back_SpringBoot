package capstone.rtou.api.conversation.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;


@Getter
public class ConversationResponse implements Serializable {

    @Schema(name = "status", description = "정상적으로 연결된 경우 true, 아닌 경우 false")
    boolean status;
    @Schema(name = "message", description = "정상적으로 출석이 되거나 전체 출결 결과가 조회됨. 또는 오류가 발생한 이유")
    String message;
    @Schema(name = "conversationId", description = "현재 대화 아이디")
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    String conversationId;
    @Schema(name = "url", description = "3D model이 말할 음성 파일 주소")
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

    public ConversationResponse(boolean status, String message, String conversationId, String url) {
        this.status = status;
        this.message = message;
        this.conversationId = conversationId;
        this.url = url;
    }
}

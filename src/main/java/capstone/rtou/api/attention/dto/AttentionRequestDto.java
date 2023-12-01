package capstone.rtou.api.attention.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class AttentionRequestDto {

    @Schema(name = "userId", description = "사용자 아이디")
    @NotBlank
    String userId;

    public AttentionRequestDto() {
    }
}

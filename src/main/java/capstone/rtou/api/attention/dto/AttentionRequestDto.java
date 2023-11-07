package capstone.rtou.api.attention.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class AttentionRequestDto {

    @NotBlank(message = "UserId cannot be null")
    @Schema(description = "사용자 아이디")
    private String userId;

    @NotBlank(message = "Date cannot be null")
            @Schema(description = "출석한 날짜. YYYYMMDD 형식.", example = "20231106")
    private String date;
}

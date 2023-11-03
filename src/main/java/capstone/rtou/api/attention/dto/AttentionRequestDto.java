package capstone.rtou.api.attention.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class AttentionRequestDto {

    @NotBlank
    String userId;
    @NotBlank
    String date;
}

package capstone.rtou.attention.dto;

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
    LocalDate date;
}

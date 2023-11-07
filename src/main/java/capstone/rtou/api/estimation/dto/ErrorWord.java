package capstone.rtou.api.estimation.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorWord {

    @Schema(description = "틀린 단어")
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    String errorWord;
    @Schema(description = "단어가 틀린 이유", example = "Omission, Insertion, Mispronunciation")
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    String errorType;
}

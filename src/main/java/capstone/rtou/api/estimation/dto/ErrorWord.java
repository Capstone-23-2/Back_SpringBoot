package capstone.rtou.api.estimation.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@Getter
@AllArgsConstructor
public class ErrorWord implements Serializable {

    @Schema(name = "errorWord", description = "문장에서 오류가 발생한 단어")
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    String errorWord;

    @Schema(name = "errorType", description = "해당 단어에서 오류가 발생한 이유")
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    String errorType;
}

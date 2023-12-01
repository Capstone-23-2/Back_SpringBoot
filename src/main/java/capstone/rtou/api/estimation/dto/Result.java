package capstone.rtou.api.estimation.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;
import java.util.List;

@Getter
@AllArgsConstructor
public class Result implements Serializable {

    @Schema(name = "modelSentence", description = "모델이 말한 문장")
    String modelSentence;

    @Schema(name = "userSentence", description = "사용자가 말한 문장")
    String userSentence;

    @Schema(name = "errorWords", description = "사용자가 말한 문장에서 오류가 발생한 단어들")
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    List<ErrorWord> errorWords;
}

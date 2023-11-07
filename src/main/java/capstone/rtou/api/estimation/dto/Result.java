package capstone.rtou.api.estimation.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class Result {

    @Schema(description = "사용자가 말한 문장")
    String sentence;
    @Schema(description = "문장에서 틀린 단어들")
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    List<ErrorWord> errorWords;
}

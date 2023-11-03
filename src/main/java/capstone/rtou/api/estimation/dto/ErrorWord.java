package capstone.rtou.api.estimation.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorWord {
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    String errorWord;
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    String errorType;
}

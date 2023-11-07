package capstone.rtou.api.estimation.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class Result {
    String sentence;
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    List<ErrorWord> errorWords;
}

package capstone.rtou.api.estimation.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class EstimationResponse {

    boolean status;
    String message;
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    List<Result> results;
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    double avgAccuracy;
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    double avgFluency;
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    double avgCompleteness;
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    double avgPron;

    public EstimationResponse(boolean status, String message) {
        this.status = status;
        this.message = message;
    }
}

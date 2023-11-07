package capstone.rtou.api.estimation.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class EstimationResponse {

    @Schema(description = "상태")
    boolean status;
    @Schema(description = "상태 메시지")
    String message;
    @Schema(description = "모든 문장 중에서 틀린 문장의 결과")
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    List<Result> results;
    @Schema(description = "모든 음성의 정확도 평균 점수")
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    double avgAccuracy;
    @Schema(description = "모든 음성의 능숙도 평균 점수")
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    double avgFluency;
    @Schema(description = "모든 음성의 완성도 평균 점수")
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    double avgCompleteness;
    @Schema(description = "모든 음성의 발음 평균 점수")
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    double avgPron;

    public EstimationResponse(boolean status, String message) {
        this.status = status;
        this.message = message;
    }
}

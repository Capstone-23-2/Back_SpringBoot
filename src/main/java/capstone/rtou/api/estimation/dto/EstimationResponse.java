package capstone.rtou.api.estimation.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.io.Serializable;
import java.util.List;

@Getter
public class EstimationResponse implements Serializable {

    @Schema(name = "status", description = "정상적으로 연결된 경우 true, 아닌 경우 false")
    private boolean status;

    @Schema(name = "message", description = "정상적으로 출석이 되거나 전체 출결 결과가 조회됨. 또는 오류가 발생한 이유")
    private String message;

    @Schema(name = "date", description = "대화가 끝난 시각")
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private String date;

    @Schema(name = "estimationList", description = "사용자가 전체 평가 리스트를 호출할 때만 있음.", example = "{estimationId, 2023-11-28 11:45:00")
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private List<UserEstimation> estimationList;


    public EstimationResponse(boolean status, String message) {
        this.status = status;
        this.message = message;
    }

    public EstimationResponse(boolean status, String message, String date) {
        this.status = status;
        this.message = message;
        this.date = date;
    }

    public EstimationResponse(boolean status, String message, List<UserEstimation> estimationList) {
        this.status = status;
        this.message = message;
        this.estimationList = estimationList;
    }
}

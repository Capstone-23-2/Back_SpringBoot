package capstone.rtou.api.estimation.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.io.Serializable;
import java.util.List;

@Getter
public class EstimationScoreResponse implements Serializable {

    @Schema(name = "status", description = "정상적으로 연결된 경우 true, 아닌 경우 false")
    private boolean status;

    @Schema(name = "message", description = "정상적으로 출석이 되거나 전체 출결 결과가 조회됨. 또는 오류가 발생한 이유")
    private String message;

    @Schema(name = "results", description = "사용자가 말한 모든 문장과 문장에 대한 오류 단어 및 오류 이유")
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private List<Result> results;

    @Schema(name = "avgAccuracy", description = "사용자가 말한 모든 문장 평가에 대한 정확성 평균 점수")
    private double avgAccuracy;

    @Schema(name = "avgFluency", description = "사용자가 말한 모든 문장 평가에 대한 유창성 평균 점수")
    private double avgFluency;

    @Schema(name = "avgCompleteness", description = "사용자가 말한 모든 문장 평가에 대한 완전성 평균 점수")
    private double avgCompleteness;

    @Schema(name = "avgProsody", description = "사용자가 말한 모든 문장 평가에 대한 운율 평균 점수")
    private double avgProsody;

    @Schema(name = "avgPron", description = "사용자가 말한 모든 문장에 대한 정확성, 유창성, 완전성, 운윤 점수를 기반한 발음 평균 점수")
    private double avgPron;

    public EstimationScoreResponse(boolean status, String message) {
        this.status = status;
        this.message = message;
    }

    public EstimationScoreResponse(boolean status, String message, List<Result> results, double avgAccuracy, double avgFluency, double avgCompleteness, double avgProsody, double avgPron) {
        this.status = status;
        this.message = message;
        this.results = results;
        this.avgAccuracy = avgAccuracy;
        this.avgFluency = avgFluency;
        this.avgCompleteness = avgCompleteness;
        this.avgProsody = avgProsody;
        this.avgPron = avgPron;
    }
}

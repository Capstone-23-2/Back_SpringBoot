package capstone.rtou.api.estimation;

import capstone.rtou.api.estimation.dto.ErrorWord;
import capstone.rtou.api.estimation.dto.Result;
import capstone.rtou.api.estimation.repository.ErrorWordRepository;
import capstone.rtou.api.estimation.repository.EstimationRepository;
import capstone.rtou.api.estimation.dto.EstimationResponse;
import capstone.rtou.domain.estimation.ErrorWords;
import capstone.rtou.domain.estimation.EstimationResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class EstimationService {

    private final EstimationRepository estimationRepository;
    private final ErrorWordRepository errorWordRepository;

    public EstimationService(EstimationRepository estimationRepository, ErrorWordRepository errorWordRepository) {
        this.estimationRepository = estimationRepository;
        this.errorWordRepository = errorWordRepository;
    }

    public EstimationResponse getEstimation(String userId, String conversationId) {

        List<EstimationResult> results = estimationRepository.findAllByUserIdAndConversationId(userId, conversationId);

        if (!results.isEmpty()) {
            List<Result> estimationResults = new ArrayList<>();

            double sumAccuracy = 0.0;
            double sumFluency = 0.0;
            double sumCompleteness = 0.0;
            double sumPron = 0.0;

            for (EstimationResult i : results) {
                sumAccuracy += i.getAccuracyScore();
                sumFluency += i.getFluencyScore();
                sumCompleteness += i.getCompletenessScore();
                sumPron += i.getPronunciationScore();
            }
            double avgAccuracy = sumAccuracy / results.size();
            double avgFluency = sumFluency / results.size();
            double avgCompleteness = sumCompleteness / results.size();
            double avgPron = sumPron / results.size();

            List<EstimationResult> resultList = estimationRepository.findByMultipleScores(avgAccuracy, avgFluency, avgCompleteness, avgPron);
            for (EstimationResult i : resultList) {
                List<ErrorWords> words = errorWordRepository.findAllByUserIdAndConversationIdAndSentence(userId, conversationId, i.getSentence());
                List<ErrorWord> errorWords = new ArrayList<>();
                for (ErrorWords error : words) {
                    errorWords.add(new ErrorWord(error.getErrorWord(), error.getErrorType()));
                }
                estimationResults.add(new Result(i.getSentence(), errorWords));
            }

            return new EstimationResponse(true, userId + "의 평가 결과", estimationResults, avgAccuracy, avgFluency, avgCompleteness, avgPron);
        } else {
            return new EstimationResponse(false, userId + "의 평가 결과X");
        }
    }
}

package capstone.rtou.api.estimation;

import capstone.rtou.api.conversation.repository.ConversationCharacterRepository;
import capstone.rtou.api.conversation.repository.ConversationsRepository;
import capstone.rtou.api.estimation.dto.*;
import capstone.rtou.api.estimation.repository.ErrorWordRepository;
import capstone.rtou.api.estimation.repository.EstimationRepository;
import capstone.rtou.api.estimation.repository.EstimationScoreRepository;
import capstone.rtou.domain.estimation.ErrorWords;
import capstone.rtou.domain.estimation.EstimationScores;
import capstone.rtou.domain.estimation.Estimations;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class EstimationService {

    private final ConversationCharacterRepository conversationCharacterRepository;
    private final ConversationsRepository conversationsRepository;
    private final EstimationRepository estimationRepository;
    private final EstimationScoreRepository estimationScoreRepository;
    private final ErrorWordRepository errorWordRepository;

    public EstimationService(ConversationCharacterRepository conversationCharacterRepository, ConversationsRepository conversationsRepository, EstimationRepository estimationRepository, EstimationScoreRepository estimationScoreRepository, ErrorWordRepository errorWordRepository) {
        this.conversationCharacterRepository = conversationCharacterRepository;
        this.conversationsRepository = conversationsRepository;
        this.estimationRepository = estimationRepository;
        this.estimationScoreRepository = estimationScoreRepository;
        this.errorWordRepository = errorWordRepository;
    }

    @Transactional
    public EstimationScoreResponse getEstimation(String userId, String estimationId) {

        List<EstimationScores> results = estimationScoreRepository.findEstimationScoresByUserIdAndEstimationId(userId, estimationId);

        if (!results.isEmpty()) {
            List<Result> estimationResults = new ArrayList<>();

            double avgAccuracy = estimationScoreRepository.getAverageAccuracyByUserId(userId, estimationId);
            double avgFluency = estimationScoreRepository.getAverageFluencyByUserId(userId, estimationId);
            double avgCompleteness = estimationScoreRepository.getAverageCompletenessByUserId(userId, estimationId);
            double avgPron = estimationScoreRepository.getAveragePronByUserId(userId, estimationId);
            double avgProsody = estimationScoreRepository.getAverageProsodyByUserId(userId, estimationId);

            List<String> sentences = conversationsRepository.findAllById_UserIdAndId_ConversationIdOrderByDate(userId, estimationId);
            List<EstimationScores> resultList = estimationScoreRepository.findEstimationScoresByUserIdAndEstimationId(userId, estimationId);

            for (int i = 0; i < sentences.size(); i += 2) {
                for (EstimationScores result : resultList) {
                    List<ErrorWords> words = errorWordRepository.findAllByEstimationIdAndSentence(estimationId, result.getSentence());
                    List<ErrorWord> errorWords = new ArrayList<>();
                    for (ErrorWords error : words) {
                        errorWords.add(new ErrorWord(error.getErrorWord(), error.getErrorType()));
                    }
                    estimationResults.add(new Result(sentences.get(i), result.getSentence(), errorWords));
                }
            }

            return new EstimationScoreResponse(true, userId + "의 평가 결과", estimationResults, avgAccuracy, avgFluency, avgCompleteness, avgProsody, avgPron);
        } else {
            return new EstimationScoreResponse(false, userId + "의 평가 결과 X");
        }
    }

    public EstimationResponse getAllEstimationById(String userId) {

        List<Estimations> estimationsList = estimationRepository.findAllById_UserIdOrderByDate(userId);
        List<UserEstimation> estimations = new ArrayList<>();

        if (estimationsList.isEmpty()) {
            return new EstimationResponse(false, "평가 기록 없음");
        }

        for (Estimations i : estimationsList) {
            String date = i.getDate().format(DateTimeFormatter.ofPattern("yy/MM/dd(E) a hh:mm"));
            estimations.add(new UserEstimation(i.getId().getEstimationId(), i.getCharacterName(), date));
        }

        return new EstimationResponse(true, "전체 평가 기록", estimations);
    }
}

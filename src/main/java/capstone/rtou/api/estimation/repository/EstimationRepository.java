package capstone.rtou.api.estimation.repository;

import capstone.rtou.domain.estimation.EstimationResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EstimationRepository extends JpaRepository<EstimationResult, Long> {

    List<EstimationResult> findAllByUserIdAndConversationId(String userId, String conversationId);

    @Query("SELECT e FROM EstimationResult e " +
            "WHERE e.AccuracyScore <= :avgAccuracyScore " +
            "AND e.FluencyScore <= :avgFluencyScore " +
            "AND e.CompletenessScore <= :avgCompletenessScore " +
            "AND e.PronunciationScore <= :avgPronScore")
    List<EstimationResult> findByMultipleScores(
            @Param("avgAccuracyScore") double avgAccuracyScore,
            @Param("avgFluencyScore") double avgFluencyScore,
            @Param("avgCompletenessScore") double avgCompletenessScore,
            @Param("avgPronScore") double avgPronScore
    );

}

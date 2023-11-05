package capstone.rtou.api.estimation.repository;

import capstone.rtou.domain.estimation.EstimationResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EstimationRepository extends JpaRepository<EstimationResult, Long> {

    List<EstimationResult> findAllByUserId(String userId);

    void deleteAllByUserId(String userId);

    @Query("SELECT e FROM EstimationResult e " +
            "WHERE e.accuracy <= :avgAccuracyScore " +
            "AND e.fluency <= :avgFluencyScore " +
            "AND e.completeness <= :avgCompletenessScore " +
            "AND e.pronunciation <= :avgPronScore")
    List<EstimationResult> findByMultipleScores(
            @Param("avgAccuracyScore") double avgAccuracyScore,
            @Param("avgFluencyScore") double avgFluencyScore,
            @Param("avgCompletenessScore") double avgCompletenessScore,
            @Param("avgPronScore") double avgPronScore
    );

}

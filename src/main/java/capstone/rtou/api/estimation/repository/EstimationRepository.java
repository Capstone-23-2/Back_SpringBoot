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

    @Query("select AVG (e.accuracy) from EstimationResult e where e.userId = :userId")
    double getAverageAccuracyByUserId(@Param("userId") String userId);

    @Query("select AVG (e.fluency) from EstimationResult e where e.userId = :userId")
    double getAverageFluencyByUserId(@Param("userId") String userId);

    @Query("select AVG (e.completeness) from EstimationResult e where e.userId = :userId")
    double getAverageCompletenessByUserId(@Param("userId") String userId);

    @Query("select AVG (e.pronunciation) from EstimationResult e where e.userId = :userId")
    double getAveragePronByUserId(@Param("userId") String userId);

    @Query("SELECT e FROM EstimationResult e " +
            "WHERE e.userId = :userId AND e.accuracy < AVG (e.accuracy) AND e.fluency < AVG (e.fluency)" +
            "AND e.pronunciation < AVG (e.pronunciation) AND e.completeness <= AVG (e.completeness)")
    List<EstimationResult> findByMultipleScores(@Param("userId") String userId);

}

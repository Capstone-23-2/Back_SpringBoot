package capstone.rtou.api.estimation.repository;

import capstone.rtou.domain.estimation.EstimationScores;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EstimationScoreRepository extends JpaRepository<EstimationScores, Long> {

    List<EstimationScores> findEstimationScoresByUserIdAndEstimationId(String userId, String estimationId);

    @Query("select sum(e.accuracy)/count (e.estimationId) from EstimationScores e where e.userId = :userId and e.estimationId = :estimationId group by e.estimationId")
    double getAverageAccuracyByUserId(@Param("userId") String userId, @Param("estimationId") String estimationId);

    @Query("select sum(e.fluency)/count (e.estimationId) from EstimationScores e where e.userId = :userId and e.estimationId = :estimationId group by e.estimationId")
    double getAverageFluencyByUserId(@Param("userId") String userId, @Param("estimationId") String estimationId);

    @Query("select sum(e.completeness)/count (e.estimationId) from EstimationScores e where e.userId = :userId and e.estimationId = :estimationId group by e.estimationId")
    double getAverageCompletenessByUserId(@Param("userId") String userId, @Param("estimationId") String estimationId);

    @Query("select sum(e.pronunciation)/count (e.estimationId) from EstimationScores e where e.userId = :userId and e.estimationId = :estimationId group by e.estimationId")
    double getAveragePronByUserId(@Param("userId") String userId, @Param("estimationId") String estimationId);

    @Query("select sum(e.prosody)/count (e.estimationId) from EstimationScores e where e.userId = :userId and e.estimationId = :estimationId group by e.estimationId")
    double getAverageProsodyByUserId(@Param("userId") String userId, @Param("estimationId") String estimationId);
}

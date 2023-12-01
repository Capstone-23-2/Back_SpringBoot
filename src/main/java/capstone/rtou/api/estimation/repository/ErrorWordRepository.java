package capstone.rtou.api.estimation.repository;

import capstone.rtou.domain.estimation.ErrorWords;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ErrorWordRepository extends JpaRepository<ErrorWords, Long> {
    List<ErrorWords> findAllByEstimationIdAndSentence(String estimationId, String sentence);
}
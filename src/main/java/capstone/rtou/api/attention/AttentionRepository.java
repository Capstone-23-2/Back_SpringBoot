package capstone.rtou.api.attention;

import capstone.rtou.domain.attention.Attentions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface AttentionRepository extends JpaRepository<Attentions, String> {

    List<Attentions> findAllByUserId(String userId);

    boolean existsAttentionByUserIdAndDate(String userId, Date date);
}

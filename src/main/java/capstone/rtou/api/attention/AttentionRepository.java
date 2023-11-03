package capstone.rtou.api.attention;

import capstone.rtou.domain.attention.Attention;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AttentionRepository extends JpaRepository<Attention, String> {

    List<Attention> findAllByUserId(String userId);

    boolean existsAttentionByUserIdAndAttendDate(String userId, String attendDate);
}

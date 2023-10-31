package capstone.rtou.attention;

import capstone.rtou.domain.attention.Attention;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttentionRepository extends JpaRepository<Attention, String> {
}

package capstone.rtou.conversation.repository;

import capstone.rtou.domain.conversation.CharacterInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CharacterInfoRepository extends JpaRepository<CharacterInfo, String> {

}

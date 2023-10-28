package capstone.rtou.conversation;

import capstone.rtou.domain.conversation.CharacterInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface CharacterInfoRepository extends JpaRepository<CharacterInfo, String> {

}

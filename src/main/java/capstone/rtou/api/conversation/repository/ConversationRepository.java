package capstone.rtou.api.conversation.repository;

import capstone.rtou.domain.conversation.Conversations;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConversationRepository extends JpaRepository<Conversations, Long> {

    Conversations findByUserIdAndUserSentence(String userId, String sentence);
}

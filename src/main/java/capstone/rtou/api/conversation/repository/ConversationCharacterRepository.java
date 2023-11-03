package capstone.rtou.api.conversation.repository;

import capstone.rtou.domain.conversation.ConversationCharacter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ConversationCharacterRepository extends JpaRepository<ConversationCharacter, String> {

    ConversationCharacter findByConversationId(String conversationId);
}

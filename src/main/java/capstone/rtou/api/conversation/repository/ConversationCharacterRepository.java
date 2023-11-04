package capstone.rtou.api.conversation.repository;

import capstone.rtou.domain.conversation.ConversationCharacter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ConversationCharacterRepository extends JpaRepository<ConversationCharacter, String> {

    @Query("select c.characterName from ConversationCharacter c where c.userId = :userId")
    String  findByUserId(@Param(value = "userId") String userId);

    @Modifying
    @Query("update ConversationCharacter c set c.characterName = :characterName where c.userId = :userId")
    void updateByUserId(@Param("userId") String userId, @Param("characterName") String characterName);
}

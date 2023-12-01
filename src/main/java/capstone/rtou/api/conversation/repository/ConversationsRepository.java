package capstone.rtou.api.conversation.repository;

import capstone.rtou.domain.conversation.Conversations;
import capstone.rtou.domain.estimation.Estimations;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConversationsRepository extends JpaRepository<Conversations, String> {
    @Query("select c.id.sentence from Conversations c where c.id.userId = :userId and c.id.conversationId = :conversationId order by c.date desc ")
    List<String> findAllById_UserIdAndId_ConversationIdOrderByDate(@Param("userId") String userId, @Param("conversationId") String conversationId);
}

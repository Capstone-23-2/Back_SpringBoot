package capstone.rtou.domain.conversation;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "Conversations")
public class Conversation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "conversationid", nullable = false)
    String conversationId;

    @Column(name = "userid",nullable = false)
    String userId;

    @Column(name = "audiofilelink", nullable = false)
    String audioFileLink;

    @Column(name = "usersentence", nullable = false)
    String userSentence;

    public Conversation(String conversationId, String userId, String audioFileLink, String userSentence) {
        this.conversationId = conversationId;
        this.userId = userId;
        this.audioFileLink = audioFileLink;
        this.userSentence = userSentence;
    }
}

package capstone.rtou.domain.conversation;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "conversations", indexes = @Index(name = "idx_conversation", columnList = "userid"))
public class Conversation {

    @Id
    @Column(name = "id")
    Long id;
    @Column(name = "userid",nullable = false)
    String userId;
    @Column(name = "audiofilelink", nullable = false)
    String audioFileLink;
    @Column(name = "usersentence", nullable = false)
    String userSentence;

    public Conversation(String userId, String audioFileLink, String userSentence) {
        this.userId = userId;
        this.audioFileLink = audioFileLink;
        this.userSentence = userSentence;
    }
}

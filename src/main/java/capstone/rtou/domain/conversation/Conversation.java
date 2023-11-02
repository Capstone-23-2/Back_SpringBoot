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

    @Column(nullable = false)
    String userId;

    @Column(nullable = false)
    String audioFileLink;

    @Column(nullable = false)
    String userSentence;

    public Conversation(String userId, String audioFileLink, String userSentence) {
        this.userId = userId;
        this.audioFileLink = audioFileLink;
        this.userSentence = userSentence;
    }
}

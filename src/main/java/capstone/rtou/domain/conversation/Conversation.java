package capstone.rtou.domain.conversation;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Table(name = "conversations")
public class Conversation {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    String userId;

    String modelSentence;

    String userSentence;

    public Conversation(String userId, String modelSentence, String userSentence) {
        this.userId = userId;
        this.modelSentence = modelSentence;
        this.userSentence = userSentence;
    }

}

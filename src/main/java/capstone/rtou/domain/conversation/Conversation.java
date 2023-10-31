package capstone.rtou.domain.conversation;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "conversations")
public class Conversation {

    @Id @Column(nullable = false)
    String userId;

    @Getter @Column(nullable = false)
    String audioFileLink;

    @Getter @Column(nullable = false)
    String userSentence;
}

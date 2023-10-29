package capstone.rtou.domain.conversation;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Conversations")
public class Conversation {

    @Id @Column(nullable = false)
    String userId;

    @Column(nullable = false)
    String audioFileLink;

    @Column(nullable = false)
    String userSentence;
}

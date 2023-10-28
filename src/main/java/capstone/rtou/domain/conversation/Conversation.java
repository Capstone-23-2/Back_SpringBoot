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

    @Id
    String userId;

    @Getter
    String audioFileLink;

    @Getter
    String userSentence;
}

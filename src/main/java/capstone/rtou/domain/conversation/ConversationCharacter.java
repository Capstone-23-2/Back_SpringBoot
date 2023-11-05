package capstone.rtou.domain.conversation;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "conversationcharacter")
@AllArgsConstructor
@NoArgsConstructor
public class ConversationCharacter {

    @Id
    @Column(name = "userid")
    String userId;

    @Column(name = "charactername", nullable = false)
    String characterName;
}

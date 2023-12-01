package capstone.rtou.domain.conversation;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;

import java.io.Serializable;


@Embeddable
@Getter
public class ConversationsId implements Serializable {
    @Column(name = "conversationid")
    private String conversationId;

    @Column(name = "userid")
    private String userId;

    @Column(name = "sentence")
    private String sentence;

    public ConversationsId() {

    }

    public ConversationsId(String conversationId, String userId, String sentence) {
        this.conversationId = conversationId;
        this.userId = userId;
        this.sentence = sentence;
    }
}

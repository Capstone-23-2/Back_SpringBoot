package capstone.rtou.domain.conversation;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "conversations", indexes = @Index(name = "idx_conversations", columnList = "userid, conversationid"))
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
public class Conversations {

    @Getter
    @EmbeddedId
    private ConversationsId id;

    @Column(name = "date", updatable = false)
    @CreationTimestamp
    private LocalDateTime date;

    public Conversations(ConversationsId id) {
        this.id = id;
    }
}

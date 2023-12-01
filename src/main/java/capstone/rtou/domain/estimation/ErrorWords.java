package capstone.rtou.domain.estimation;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "errorwords", indexes = @Index(name = "idx_errors", columnList = "userid, sentence"))
public class ErrorWords {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "userid", nullable = false)
    String userId;

    @Column(name = "estimationid", nullable = false)
    String estimationId;

    @Column(name = "sentence", nullable = false)
    String sentence;

    @Column(name = "errorword", nullable = false)
    String errorWord;

    @Column(name = "errortype", nullable = false)
    String errorType;

    public ErrorWords(String estimationId, String sentence, String errorWord, String errorType) {
        this.estimationId = estimationId;
        this.sentence = sentence;
        this.errorWord = errorWord;
        this.errorType = errorType;
    }
}

package capstone.rtou.domain.estimation;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "Errorwords", indexes = @Index(name = "errorWordsUserIdIndex", columnList = "userid"))
public class ErrorWords {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(name = "userid", nullable = false)
    String userId;
    @Column(nullable = false)
    String sentence;
    @Column(name = "errorword", nullable = false)
    String errorWord;
    @Column(name = "errortype", nullable = false)
    String errorType;

    public ErrorWords(String userId, String sentence, String errorWord, String errorType) {
        this.userId = userId;
        this.sentence = sentence;
        this.errorWord = errorWord;
        this.errorType = errorType;
    }
}

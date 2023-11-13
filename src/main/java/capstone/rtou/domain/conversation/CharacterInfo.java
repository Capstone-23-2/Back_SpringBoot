package capstone.rtou.domain.conversation;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "characterinfo")
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CharacterInfo {

    @Id
    @Column(name = "name")
    String name; // 캐릭터 이름
    @Column(name = "voicename")
    String voiceName; // 음성명
    @Column(name = "pitch")
    double pitch; // 음성 높낮이
    @Column(name = "langcode")
    String langCode;
}

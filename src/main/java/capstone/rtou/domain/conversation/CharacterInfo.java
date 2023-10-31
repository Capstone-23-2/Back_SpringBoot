package capstone.rtou.domain.conversation;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

@Entity
@Table(name = "CharacterInfo")
public class CharacterInfo {

    @Id @Column
    String name; // 캐릭터 이름
    @Column
    String voice_name; // 음성명
    @Column
    double pitch; // 음성 높낮이
}

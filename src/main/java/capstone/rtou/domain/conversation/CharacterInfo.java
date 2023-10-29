package capstone.rtou.domain.conversation;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

@Getter
@Entity
@Table(name = "CharacterInfo")
public class CharacterInfo {

    @Id
    String name; // 캐릭터 이름

    String voice_name; // 음성명

    double pitch; // 음성 높낮이
}

package capstone.rtou.api.conversation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ModelRequest {

    int user_id;
    String sentence;
    String character_name;
}

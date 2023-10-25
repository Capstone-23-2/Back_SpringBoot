package capstone.rtou.conversation.dto;

import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class RequestDto {

    //Long roomId;

    MultipartFile audioFile;
}

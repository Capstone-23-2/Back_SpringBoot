package capstone.rtou.auth.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

// Registration Object
@Getter
public class AuthRequestDto {

    @NotBlank
    private String id;

}

package capstone.rtou.domain;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

// Registration Object
@Getter
public class UserRequestDto {

    @NotBlank
    @Size(min = 8, max = 15)
    private String id;
    @NotBlank
    @Size(min = 8, max = 20)
    private String password;
    @NotBlank
    private String name;
    private MultipartFile img;
    private Integer age;

    public byte[] getImg() throws IOException {
        if (img == null) {
            return new byte[0];
        }
        return img.getBytes();
    }
}

package capstone.rtou.auth;

import capstone.rtou.domain.AuthResponse;
import capstone.rtou.domain.User;
import capstone.rtou.domain.UserRequestDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/users")
@Slf4j
public class UserController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @JsonFormat
    @GetMapping("/duplicate")
    public ResponseEntity<AuthResponse> existId(@RequestParam String id) {
        log.info("아이디 중복 조회");
        if (!userRepository.findByUserId(id).isEmpty()) {
            return ResponseEntity.ok().body(new AuthResponse(HttpStatus.OK.value(), "'" + id + "'아이디는 이미 존재합니다.", null));
        }

        return ResponseEntity.ok().body(new AuthResponse(HttpStatus.OK.value(), "중복된 아이디가 없습니다.", null));
    }

    @Transactional
    @JsonFormat
    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> registerUser(@RequestBody @Validated UserRequestDto userRequestDto, BindingResult bindingResult) throws IOException {
        log.info("회원 등록 시작");

        if (bindingResult.hasErrors()) {
            log.info("회원 등록 오류={}", bindingResult);
            return ResponseEntity.badRequest().body(new AuthResponse(HttpStatus.BAD_REQUEST.value(), bindingResult.getFieldError().getDefaultMessage(), bindingResult.getFieldError().getField()));
        }

        String encodedPassword = passwordEncoder.encode(userRequestDto.getPassword());
        User user = new User();

        user.setUserId(userRequestDto.getId());
        user.setPassword(encodedPassword);
        user.setName(userRequestDto.getName());
        user.setAge(userRequestDto.getAge());
        user.setImg(userRequestDto.getImg());

        userRepository.save(user);

        log.info("User Info={} 저장 완료", user);


        return ResponseEntity.ok().body(new AuthResponse(HttpStatus.OK.value(),"회원 정보가 등록되었습니다.", null));
    }

//    @GetMapping("/signin")
//    public ResponseEntity<?> Login(@Validated @RequestParam String id, @Validated @RequestParam String password, BindingResult bindingResult) {
//        log.info("로그인 시작");
//
//        if (bindingResult.hasFieldErrors("id")) {
//
//        }
//
//        String encodedPassword = passwordEncoder.encode(password);
//
//
//    }
}

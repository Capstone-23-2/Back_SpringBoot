package capstone.rtou;

import capstone.rtou.api.conversation.repository.CharacterInfoRepository;
import capstone.rtou.domain.conversation.CharacterInfo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class RtouApplication {

	public static void main(String[] args) {
		SpringApplication.run(RtouApplication.class, args);
	}

	@SpringBootApplication
	public class YourApplication {

		public static void main(String[] args) {
			SpringApplication.run(YourApplication.class, args);
		}
	}

}

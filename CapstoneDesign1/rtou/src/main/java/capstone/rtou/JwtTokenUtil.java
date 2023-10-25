package capstone.rtou;

import org.springframework.beans.factory.annotation.Value;

import java.io.Serializable;

public class JwtTokenUtil implements Serializable {
    private static final long serialVersionUID = -2550185165626007488L;
    public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60;

    @Value("${spring.jwt.secret}")
    private String secret;


}

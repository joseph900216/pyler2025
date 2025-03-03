package pyler.config;

import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;

@Slf4j
@Component
public class JwtKeyProvider {

    @Value("${jwt.secret}")
    private String secretKey;

    public Key getSignKey() {
        byte[] keyByes = Base64.getDecoder().decode(secretKey);
        return Keys.hmacShaKeyFor(keyByes);
    }
}

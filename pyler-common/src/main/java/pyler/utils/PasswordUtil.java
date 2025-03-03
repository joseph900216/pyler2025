package pyler.utils;

import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordUtil {

    private final PasswordEncoder passwordEncoder;

    public PasswordUtil() {
        this.passwordEncoder = Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8();
    }

    /***
     * 패스워드 해싱(알고리즘: Argon2)
     * @param rawPassword
     * @return
     */
    public String hashPassWord(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    /***
     * 패스워드 암호화 검증
     * @param rawPassword
     * @param hashedPassword
     * @return
     */
    public boolean verifyPassword(String rawPassword, String hashedPassword) {
        return passwordEncoder.matches(rawPassword, hashedPassword);
    }
}

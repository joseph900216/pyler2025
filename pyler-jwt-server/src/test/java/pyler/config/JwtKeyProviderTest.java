package pyler.config;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.security.Key;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(properties = {
        "jwt.secret=pylerJwtSecretKeyForTestingPurposesOnly2025EeWFuZG9tU2VjcmV0S2V5"
})
class JwtKeyProviderTest {

    @Autowired
    private JwtKeyProvider jwtKeyProvider;

    @Test
    void getSignKey() {
        Key key = jwtKeyProvider.getSignKey();

        assertThat(key).isNotNull();
    }

}
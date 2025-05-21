package samsamoo.ai_mockly;

import com.auth0.jwt.algorithms.Algorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import samsamoo.ai_mockly.domain.member.domain.Member;
import samsamoo.ai_mockly.global.security.jwt.JwtTokenProviderImpl;
import samsamoo.ai_mockly.infrastructure.redis.RedisUtil;

import java.lang.reflect.Field;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JwtTokenProviderImplTest {

    @InjectMocks
    private JwtTokenProviderImpl jwtTokenProvider;

    @Mock
    private RedisUtil redisUtil;

    private Member mockMember;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        jwtTokenProvider = new JwtTokenProviderImpl(redisUtil); // 생성자 주입

        injectPrivateField(jwtTokenProvider, "secret", "test_secret_1234567890");
        injectPrivateField(jwtTokenProvider, "accessExpiration", 3600L);
        injectPrivateField(jwtTokenProvider, "refreshExpiration", 86400L);
        injectPrivateField(jwtTokenProvider, "accessHeader", "Authorization");
        injectPrivateField(jwtTokenProvider, "refreshHeader", "Refresh");

        // Member 생성 및 ID 수동 주입
        mockMember = Member.builder()
                .kakaoId("kakao_999")
                .nickname("MockUser")
                .build();

        injectPrivateField(mockMember, "id", 1L);
    }

    @Test
    void accessToken_생성_정상작동() {
        String token = jwtTokenProvider.createAccessToken(mockMember);
        assertNotNull(token);
        assertTrue(token.startsWith("eyJ"));  // JWT는 대개 eyJ로 시작
    }

    @Test
    void refreshToken_생성_정상작동() {
        String refreshToken = jwtTokenProvider.createRefreshToken();
        assertNotNull(refreshToken);
        assertTrue(refreshToken.startsWith("eyJ"));
    }

//    @Test
//    void accessToken에서_memberId_정상_추출() {
//        String token = jwtTokenProvider.createAccessToken(mockMember);
//        Optional<String> memberId = jwtTokenProvider.extractMemberId(token);
//        assertTrue(memberId.isPresent());
//        assertEquals("1", memberId.get());
//    }

    @Test
    void accessToken_유효성_정상_검증() {
        String token = jwtTokenProvider.createAccessToken(mockMember);
        boolean valid = jwtTokenProvider.isTokenValid(token);
        assertTrue(valid);
    }

    @Test
    void accessToken_블랙리스트_탐지() {
        String fakeToken = "someTokenValue";
        when(redisUtil.getData("BL_AT_" + fakeToken)).thenReturn("blacklisted");

        boolean result = jwtTokenProvider.isTokenInBlackList(fakeToken);
        assertTrue(result);
    }

    @Test
    void accessToken_블랙리스트_없을때() {
        String fakeToken = "someTokenValue";
        when(redisUtil.getData("BL_AT_" + fakeToken)).thenReturn(null);

        boolean result = jwtTokenProvider.isTokenInBlackList(fakeToken);
        assertFalse(result);
    }

    private void injectPrivateField(Object target, String fieldName, Object value) {
        try {
            Field field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (Exception e) {
            throw new RuntimeException("필드 주입 실패: " + fieldName, e);
        }
    }

}


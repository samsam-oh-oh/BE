package samsamohoh.ai_mockly.global.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import samsamohoh.ai_mockly.domain.user.domain.User;
import samsamohoh.ai_mockly.infrastructure.redis.RedisUtil;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Transactional
@Service
@RequiredArgsConstructor
@Setter(value = AccessLevel.PRIVATE)
@Slf4j
public class JwtTokenProviderImpl implements JwtTokenProvider {

    private final RedisUtil redisUtil;

    // token hash
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.access.expiration}")
    private Long accessExpirationSecond;

    @Value("${jwt.refresh.expiration}")
    private Long refreshExpirationSecond;

    @Value("${jwt.access.header}")
    private String accessHeader;

    @Value("${jwt.refresh.header}")
    private String refreshHeader;

    private static final String BL_AT_PREFIX = "BL_AT_";

    private static final String ACCESS_TOKEN_SUBJECT = "AccessToken";
    private static final String REFRESH_TOKEN_SUBJECT = "RefreshToken";

    private static final String USERID_CLAIM = "id";
    private static final String USERNAME_CLAIM = "nickname";
    private static final String USER_STATE_CLAIM = "state";
    private static final String BEARER = "Bearer ";

    // method
    @Override
    public String createAccessToken(User user) {
        return JWT.create()
                .withSubject(ACCESS_TOKEN_SUBJECT)
                .withExpiresAt(new Date(System.currentTimeMillis() + accessExpirationSecond * 1000))
                .withClaim(USERID_CLAIM, user.getId())
                .withClaim(USERNAME_CLAIM, user.getNickname())
                .withClaim(USER_STATE_CLAIM, user.getState().name())
                .sign(Algorithm.HMAC512(secret));
    }

    @Override
    public String createRefreshToken() {
        return JWT.create()
                .withSubject(REFRESH_TOKEN_SUBJECT)
                .withExpiresAt(new Date(System.currentTimeMillis() + refreshExpirationSecond * 1000))
                .sign(Algorithm.HMAC512(secret));
    }

    @Override
    public void sendAccessAndRefreshToken(HttpServletResponse response, String accessToken, String refreshToken) {
        response.setStatus(HttpServletResponse.SC_OK);

        setAccessTokenHeader(response, accessToken);
        setRefreshTokenHeader(response, refreshToken);

        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put(ACCESS_TOKEN_SUBJECT, accessToken);
        tokenMap.put(REFRESH_TOKEN_SUBJECT, refreshToken);
    }

    @Override
    public void sendAccessToken(HttpServletResponse response, String accessToken) {
        response.setStatus(HttpServletResponse.SC_OK);

        setAccessTokenHeader(response, accessToken);

        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put(ACCESS_TOKEN_SUBJECT, accessToken);
    }

    @Override
    public Optional<String> extractAccessToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(accessHeader)).filter(
                accessToken -> accessToken.startsWith(BEARER)
        ).map(accessToken -> accessToken.replace(BEARER, ""));
    }

    @Override
    public Optional<String> extractRefreshToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(refreshHeader)).filter(
                refreshToken -> refreshToken.startsWith(BEARER)
        ).map(refreshToken -> refreshToken.replace(BEARER, ""));
    }

    @Override
    public Optional<Long> extractUserId(String accessToken) {
        try{
            return Optional.ofNullable(
                    JWT.require(Algorithm.HMAC512(secret))
                            .build()
                            .verify(accessToken)
                            .getClaim(USERID_CLAIM)
                            .asLong());
        } catch (Exception e) {
            log.error("계정 아이디 추출 실패: {}", e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public Optional<String> extractNickname(String accessToken) {
        try {
            return Optional.ofNullable(
                    JWT.require(Algorithm.HMAC512(secret))
                            .build()
                            .verify(accessToken)
                            .getClaim(USERNAME_CLAIM)
                            .asString());
        } catch (Exception e) {
            log.error("닉네임 추출 실패: {}", e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public void setAccessTokenHeader(HttpServletResponse response, String accessToken) {
        response.setHeader(accessHeader, accessToken);
    }

    @Override
    public void setRefreshTokenHeader(HttpServletResponse response, String refreshToken) {
        response.setHeader(refreshHeader, refreshToken);
    }

    @Override
    public boolean isTokenValid(String token) {
        try {
            JWT.require(Algorithm.HMAC512(secret)).build().verify(token);
            return true;
        } catch (TokenExpiredException ex) {
            log.error("만료된 JWT 토큰입니다.");
            return false;
        } catch (Exception e) {
            log.error("유효하지 않은 JWT 토큰입니다: {}", e.getMessage());
            return false;
        }
    }

    @Override
    public boolean isTokenInBlackList(String accessToken) {
        // Redis에서 블랙리스트로 저장된 토큰 확인
        String blacklistToken = redisUtil.getData(BL_AT_PREFIX + accessToken);
        return blacklistToken != null;
    }
}

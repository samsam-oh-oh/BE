package samsamohoh.ai_mockly.global.security.jwt;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import samsamohoh.ai_mockly.domain.user.domain.User;

import java.util.Optional;

public interface JwtTokenProvider {

    String createAccessToken(User user);

    String createRefreshToken();

    void sendAccessAndRefreshToken(HttpServletResponse response, String accessToken, String refreshToken);

    void sendAccessToken(HttpServletResponse response, String accessToken);

    Optional<String> extractAccessToken(HttpServletRequest request);

    Optional<String> extractRefreshToken(HttpServletRequest request);

    Optional<Long> extractUserId(String accessToken);

    Optional<String> extractNickname(String accessToken);

    void setAccessTokenHeader(HttpServletResponse response, String accessToken);

    void setRefreshTokenHeader(HttpServletResponse response, String refreshToken);

    boolean isTokenValid(String token);

    boolean isTokenInBlackList(String accessToken);
}

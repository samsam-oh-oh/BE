package samsamoo.ai_mockly.global.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import samsamoo.ai_mockly.domain.member.domain.Member;
import samsamoo.ai_mockly.domain.member.domain.repository.MemberRepository;
import samsamoo.ai_mockly.global.security.jwt.JwtTokenProvider;
import samsamoo.ai_mockly.infrastructure.redis.RedisUtil;

import java.io.IOException;
import java.util.Collections;

@RequiredArgsConstructor
public class JwtAuthenticationProcessingFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;
    private final RedisUtil redisUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        checkAccessTokenAndAuthentication(request, response, filterChain);
    }

    private void checkAccessTokenAndAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        jwtTokenProvider.extractAccessToken(request)
                .filter(jwtTokenProvider::isTokenValid)
                .filter(accessToken -> !jwtTokenProvider.isTokenInBlackList(accessToken))
                .flatMap(jwtTokenProvider::extractMemberId)
                .map(Long::valueOf)
                .flatMap(memberRepository::findById)
                .ifPresent(this::saveAuthentication);
        filterChain.doFilter(request, response);
    }

    private void saveAuthentication(Member member) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(member, null, Collections.emptyList());
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }
}

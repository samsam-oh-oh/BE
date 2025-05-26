package samsamoo.ai_mockly.domain.auth.application;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import samsamoo.ai_mockly.domain.auth.dto.request.LoginReq;
import samsamoo.ai_mockly.domain.auth.dto.response.DuplicateCheckRes;
import samsamoo.ai_mockly.domain.auth.dto.response.LoginRes;
import samsamoo.ai_mockly.domain.member.domain.Member;
import samsamoo.ai_mockly.domain.member.domain.repository.MemberRepository;
import samsamoo.ai_mockly.domain.member.dto.response.MemberDTO;
import samsamoo.ai_mockly.domain.point.domain.Point;
import samsamoo.ai_mockly.domain.point.domain.State;
import samsamoo.ai_mockly.domain.point.domain.repository.PointRepository;
import samsamoo.ai_mockly.global.common.SuccessResponse;
import samsamoo.ai_mockly.global.security.jwt.JwtTokenProvider;
import samsamoo.ai_mockly.infrastructure.redis.RedisUtil;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class AuthService {

    @Value("${jwt.refresh.expiration}")
    private Long refreshExpiration;

    private static String RT_PREFIX = "RT_";
    private static String BL_AT_PREFIX = "BL_AT_";

    private final KakaoTokenValidator kakaoTokenValidator;
    private final MemberRepository memberRepository;
    private final PointRepository pointRepository;

    private final JwtTokenProvider jwtTokenProvider;
    private final RedisUtil redisUtil;

    @Transactional
    public SuccessResponse<LoginRes> signUpOrLogin(LoginReq loginReq) {
        String idToken = loginReq.getIdToken();
        MemberDTO memberDTO = kakaoTokenValidator.validateAndExtract(idToken);

        Member member = memberRepository.findByKakaoId(memberDTO.getKakaoId())
                .orElseGet(() -> registerNewMember(memberDTO));

        boolean isNewMember = member.getNickname().startsWith("|KAKAO");

        String accessToken = jwtTokenProvider.createAccessToken(member);
        String refreshToken = jwtTokenProvider.createRefreshToken();

        redisUtil.setDataExpire(RT_PREFIX + refreshToken, member.getNickname(), refreshExpiration);

        LoginRes loginRes = LoginRes.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .isNewMember(isNewMember)
                .build();

        return SuccessResponse.of(loginRes);
    }

    private Member registerNewMember(MemberDTO memberDTO) {
        Member member = Member.builder()
                .kakaoId(memberDTO.getKakaoId())
                .nickname("|KAKAO" + (int)(Math.random() * 9000 + 1000))
                .profileImage(null)
                .maxScore(0)
                .build();

        Point point = Point.builder()
                .member(member)
                .amount(0)
                .type("new join")
                .state(State.ACTIVE)
                .build();

        memberRepository.save(member);
        pointRepository.save(point);

        return member;
    }

    public SuccessResponse<DuplicateCheckRes> checkNicknameDuplicate(String nickname) {
        if(nickname == null || nickname.isBlank()) {
            throw new IllegalArgumentException("닉네임은 공백일 수 없습니다.");
        }

        DuplicateCheckRes nicknameDuplicateCheckRes = DuplicateCheckRes.builder()
                .availability(!memberRepository.existsByNickname(nickname))
                .build();

        return SuccessResponse.of(nicknameDuplicateCheckRes);
    }
}

package samsamoo.ai_mockly.domain.auth.application;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import samsamoo.ai_mockly.domain.auth.dto.request.LoginReq;
import samsamoo.ai_mockly.domain.auth.dto.request.LogoutReq;
import samsamoo.ai_mockly.domain.auth.dto.response.DuplicateCheckRes;
import samsamoo.ai_mockly.domain.auth.dto.response.LoginRes;
import samsamoo.ai_mockly.domain.feedback.domain.Feedback;
import samsamoo.ai_mockly.domain.feedback.domain.repository.FeedbackRepository;
import samsamoo.ai_mockly.domain.member.domain.Member;
import samsamoo.ai_mockly.domain.member.domain.repository.MemberRepository;
import samsamoo.ai_mockly.domain.member.dto.response.MemberDTO;
import samsamoo.ai_mockly.domain.point.domain.Point;
import samsamoo.ai_mockly.domain.point.domain.State;
import samsamoo.ai_mockly.domain.point.domain.repository.PointRepository;
import samsamoo.ai_mockly.domain.score.domain.Score;
import samsamoo.ai_mockly.domain.score.domain.repository.ScoreRepository;
import samsamoo.ai_mockly.global.common.Message;
import samsamoo.ai_mockly.global.common.SuccessResponse;
import samsamoo.ai_mockly.global.security.jwt.JwtTokenProvider;
import samsamoo.ai_mockly.infrastructure.redis.RedisUtil;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

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
    private final ScoreRepository scoreRepository;
    private final FeedbackRepository feedbackRepository;

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

    public SuccessResponse<Message> logout(Long memberId, LogoutReq logoutReq) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("해당 아이디를 갖는 유저가 없습니다."));
        String findNickname = redisUtil.getData(RT_PREFIX + logoutReq.getRefreshToken());
        if(!member.getNickname().equals(findNickname))
            throw new IllegalArgumentException("본인의 RefreshToken만 삭제 가능합니다.");
        redisUtil.deleteData(RT_PREFIX + logoutReq.getRefreshToken());

        // 남는 시간 초단위 계산
        DecodedJWT decodedJWT = JWT.decode(logoutReq.getAccessToken());
        Instant expiredAt = decodedJWT.getExpiresAt().toInstant();
        Instant now = Instant.now();
        long between = ChronoUnit.SECONDS.between(now, expiredAt);
        System.out.println("남은 시간 : " + between);

        // 남는 시간 만료만큼 AccessToken을 Blacklist에 포함
        redisUtil.setDataExpire(BL_AT_PREFIX + logoutReq.getAccessToken(), "black list token", between);

        Message message = Message.builder()
                .message("로그아웃이 완료되었습니다.")
                .build();

        return SuccessResponse.of(message);
    }

    @Transactional
    public SuccessResponse<Message> exit(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("해당 아이디를 갖는 유저가 없습니다."));

        List<Point> pointList = pointRepository.findAllByMember(member);
        List<Score> scoreList = scoreRepository.findAllByMember(member);
        List<Feedback> feedbackList = feedbackRepository.findAllByMember(member);
        pointRepository.deleteAll(pointList);
        scoreRepository.deleteAll(scoreList);
        feedbackRepository.deleteAll(feedbackList);

        memberRepository.delete(member);

        Message message = Message.builder()
                .message("회원 탈퇴가 완료되었습니다.")
                .build();

        return SuccessResponse.of(message);
    }
}

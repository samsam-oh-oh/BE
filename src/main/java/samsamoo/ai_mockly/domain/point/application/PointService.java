package samsamoo.ai_mockly.domain.point.application;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import samsamoo.ai_mockly.domain.member.domain.Member;
import samsamoo.ai_mockly.domain.member.domain.repository.MemberRepository;
import samsamoo.ai_mockly.domain.point.domain.Point;
import samsamoo.ai_mockly.domain.point.domain.State;
import samsamoo.ai_mockly.domain.point.domain.repository.PointRepository;
import samsamoo.ai_mockly.global.common.Message;
import samsamoo.ai_mockly.global.common.SuccessResponse;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class PointService {

    private final PointRepository pointRepository;
    private final MemberRepository memberRepository;

    public SuccessResponse<Integer> getCurrentAmount(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BadCredentialsException("해당 아이디를 갖는 유저가 없습니다."));

        Integer pointAmount = pointRepository.sumActiveAmountByMember(member);

        return SuccessResponse.of(pointAmount);
    }

    @Transactional
    public SuccessResponse<Message> addPoint(Long memberId, Integer amount, String reason) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BadCredentialsException("해당 아이디를 갖는 유저가 없습니다."));

        if(amount < 0) {
            throw new IllegalArgumentException("적립할 포인트는 음수가 될 수 없습니다.");
        }

        if(reason == null || reason.isBlank()) {
            reason = "기타";
        }

        Point point = Point.builder()
                .member(member)
                .amount(amount)
                .type(reason)
                .state(State.ACTIVE)
                .build();

        pointRepository.save(point);

        Message message = Message.builder()
                .message(amount + "점이 적립되었습니다.")
                .build();

        return SuccessResponse.of(message);
    }

    @Transactional
    public SuccessResponse<Message> deductPoint(Long memberId, Integer amount, String reason) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BadCredentialsException("해당 아이디를 갖는 유저가 없습니다."));

        if (amount < 0) {
            throw new IllegalArgumentException("차감할 포인트는 음수가 될 수 없습니다.");
        }

        if(reason == null || reason.isBlank()) {
            reason = "기타";
        }

        Point point = Point.builder()
                .member(member)
                .amount(-amount)
                .type(reason)
                .state(State.ACTIVE)
                .build();

        pointRepository.save(point);

        Message message = Message.builder()
                .message(amount + "점이 차감되었습니다.")
                .build();

        return SuccessResponse.of(message);
    }
}

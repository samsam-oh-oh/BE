package samsamoo.ai_mockly.domain.feedback.application;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import samsamoo.ai_mockly.domain.feedback.domain.Feedback;
import samsamoo.ai_mockly.domain.feedback.domain.repository.FeedbackRepository;
import samsamoo.ai_mockly.domain.feedback.dto.request.FeedbackSaveReq;
import samsamoo.ai_mockly.domain.member.domain.Member;
import samsamoo.ai_mockly.domain.member.domain.repository.MemberRepository;
import samsamoo.ai_mockly.global.common.Message;
import samsamoo.ai_mockly.global.common.SuccessResponse;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public SuccessResponse<Message> saveFeedback(Long memberId, FeedbackSaveReq feedbackSaveReq) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BadCredentialsException("해당 아이디를 갖는 유저가 없습니다."));

        Feedback feedback = Feedback.builder()
                .member(member)
                .content(feedbackSaveReq.getContent())
                .build();

        feedbackRepository.save(feedback);

        Message message = Message.builder()
                .message("면접 결과 리포트 저장이 완료되었습니다.")
                .build();
        return SuccessResponse.of(message);
    }
}

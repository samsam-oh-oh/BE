package samsamoo.ai_mockly.domain.feedback.application;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import samsamoo.ai_mockly.domain.feedback.domain.Feedback;
import samsamoo.ai_mockly.domain.feedback.domain.repository.FeedbackRepository;
import samsamoo.ai_mockly.domain.feedback.dto.request.FeedbackSaveReq;
import samsamoo.ai_mockly.domain.feedback.dto.response.FeedbackContentsRes;
import samsamoo.ai_mockly.domain.member.domain.Member;
import samsamoo.ai_mockly.domain.member.domain.repository.MemberRepository;
import samsamoo.ai_mockly.global.common.Message;
import samsamoo.ai_mockly.global.common.SuccessResponse;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public SuccessResponse<Message> saveFeedback(Long memberId, FeedbackSaveReq feedbackSaveReq) {
        if(feedbackSaveReq.getContent() == null || feedbackSaveReq.getContent().isBlank()) {
            throw new IllegalArgumentException("면접 결과를 입력해주세요.");
        }

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

    public SuccessResponse<List<FeedbackContentsRes>> getFeedbackContents(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BadCredentialsException("해당 아이디를 갖는 유저가 없습니다."));

        List<Feedback> feedbackList = feedbackRepository.findAllByMember(member);

        List<FeedbackContentsRes> feedbackContentList = feedbackList.stream()
                .map(feedback -> FeedbackContentsRes.builder()
                        .id(feedback.getId())
                        .content(feedback.getContent())
                        .build())
                .collect(Collectors.toList());

        return SuccessResponse.of(feedbackContentList);
    }
}

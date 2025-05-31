package samsamoo.ai_mockly.domain.feedback.application;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import samsamoo.ai_mockly.domain.feedback.domain.Feedback;
import samsamoo.ai_mockly.domain.feedback.domain.repository.FeedbackRepository;
import samsamoo.ai_mockly.domain.feedback.dto.request.FeedbackSaveReq;
import samsamoo.ai_mockly.domain.feedback.dto.response.FeedbackContentsRes;
import samsamoo.ai_mockly.domain.feedbackaccess.repository.FeedbackAccessRepository;
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
    private final FeedbackAccessRepository feedbackAccessRepository;

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

    public SuccessResponse<FeedbackContentsRes> getFeedbackContent(Long memberId, Long feedbackId) {
        Member viewer = memberRepository.findById(memberId)
                .orElseThrow(() -> new BadCredentialsException("해당 아이디를 갖는 유저가 없습니다."));

        Feedback feedback = feedbackRepository.findById(feedbackId)
                .orElseThrow(() -> new BadCredentialsException("해당 feedbackId를 갖는 feedback가 없습니다."));

        Member owner = feedback.getMember();

        // 본인이면 무조건 열람 허용
        if(!viewer.getId().equals(owner.getId())) {
            boolean hasAccess = feedbackAccessRepository.existsByViewerAndFeedback(viewer, feedback);
            if(!hasAccess) {
                throw new IllegalArgumentException("해당 feedback에 대한 접근 권한이 없습니다. 포인트로 해제하세요.");
            }
        }

        FeedbackContentsRes feedbackContentsRes = FeedbackContentsRes.builder()
                .content(feedback.getContent())
                .build();

        return SuccessResponse.of(feedbackContentsRes);
    }
}

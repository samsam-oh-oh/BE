package samsamoo.ai_mockly.domain.feedback.presentation;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import samsamoo.ai_mockly.domain.feedback.application.FeedbackService;
import samsamoo.ai_mockly.domain.feedback.dto.request.FeedbackSaveReq;
import samsamoo.ai_mockly.domain.feedback.dto.response.FeedbackContentsRes;
import samsamoo.ai_mockly.domain.member.domain.Member;
import samsamoo.ai_mockly.global.annotation.LoginMember;
import samsamoo.ai_mockly.global.common.Message;
import samsamoo.ai_mockly.global.common.SuccessResponse;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/feedbacks")
public class FeedbackController implements FeedbackApi {

    private final FeedbackService feedbackService;

    @Override
    @PostMapping("/save")
    public ResponseEntity<SuccessResponse<Message>> saveFeedback(@LoginMember Member member, @Valid @RequestBody FeedbackSaveReq feedbackSaveReq) {
        Long memberId = member.getId();
        return ResponseEntity.ok(feedbackService.saveFeedback(memberId, feedbackSaveReq));
    }

    @Override
    @GetMapping("/contents/all")
    public ResponseEntity<SuccessResponse<List<FeedbackContentsRes>>> getFeedbackContents(@LoginMember Member member) {
        Long memberId = member.getId();
        return ResponseEntity.ok(feedbackService.getFeedbackContents(memberId));
    }
}

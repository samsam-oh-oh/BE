package samsamoo.ai_mockly.domain.point.presentation;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import samsamoo.ai_mockly.domain.member.domain.Member;
import samsamoo.ai_mockly.domain.point.application.PointService;
import samsamoo.ai_mockly.domain.point.dto.request.PointAmountReq;
import samsamoo.ai_mockly.global.annotation.LoginMember;
import samsamoo.ai_mockly.global.common.Message;
import samsamoo.ai_mockly.global.common.SuccessResponse;

@RequiredArgsConstructor
@RestController
@RequestMapping("/points")
public class PointController implements PointApi {

    private final PointService pointService;

    @Override
    @GetMapping("/me")
    public ResponseEntity<SuccessResponse<Integer>> getCurrentAmount(@LoginMember Member member) {
        Long memberId = member.getId();
        return ResponseEntity.ok(pointService.getCurrentAmount(memberId));
    }

    @Override
    @PostMapping("/add")
    public ResponseEntity<SuccessResponse<Message>> addPoint(@LoginMember Member member, @Valid @RequestBody PointAmountReq pointAmountReq) {
        Long memberId = member.getId();
        Integer amount = pointAmountReq.getPointAmount();
        String reason = pointAmountReq.getReason();
        return ResponseEntity.ok(pointService.addPoint(memberId, amount, reason));
    }

    @Override
    @PostMapping("/deduct")
    public ResponseEntity<SuccessResponse<Message>> deductPoint(@LoginMember Member member, @Valid @RequestBody PointAmountReq pointAmountReq) {
        Long memberId = member.getId();
        Integer amount = pointAmountReq.getPointAmount();
        String reason = pointAmountReq.getReason();
        return ResponseEntity.ok(pointService.deductPoint(memberId, amount, reason));
    }
}

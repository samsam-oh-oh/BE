package samsamoo.ai_mockly.domain.point.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import samsamoo.ai_mockly.domain.member.domain.Member;
import samsamoo.ai_mockly.domain.point.application.PointService;
import samsamoo.ai_mockly.global.annotation.LoginMember;
import samsamoo.ai_mockly.global.common.SuccessResponse;

@RequiredArgsConstructor
@RestController
@RequestMapping("/points")
public class PointController {

    private final PointService pointService;

    @GetMapping("/me")
    public ResponseEntity<SuccessResponse<Integer>> getCurrentAmount(@LoginMember Member member) {
        Long memberId = member.getId();
        return ResponseEntity.ok(pointService.getCurrentAmount(memberId));
    }
}

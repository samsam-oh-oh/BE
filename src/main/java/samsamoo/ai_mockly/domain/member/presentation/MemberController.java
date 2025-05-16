package samsamoo.ai_mockly.domain.member.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import samsamoo.ai_mockly.domain.member.application.MemberService;
import samsamoo.ai_mockly.global.common.Message;
import samsamoo.ai_mockly.global.common.SuccessResponse;

@RequiredArgsConstructor
@RestController
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    @PatchMapping("/nickname")
    public ResponseEntity<SuccessResponse<Message>> updateNickname(@RequestParam(name = "id") Long memberId, @RequestParam(name = "nickname") String nickname) {
        return ResponseEntity.ok(memberService.updateNickname(memberId, nickname));
    }
}

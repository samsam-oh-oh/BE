package samsamoo.ai_mockly.domain.member.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import samsamoo.ai_mockly.domain.member.application.MemberService;
import samsamoo.ai_mockly.domain.member.dto.request.UpdateNicknameReq;
import samsamoo.ai_mockly.global.common.Message;
import samsamoo.ai_mockly.global.common.SuccessResponse;

@RequiredArgsConstructor
@RestController
@RequestMapping("/members")
public class MemberController implements MemberApi {

    private final MemberService memberService;

    @Override
    @PatchMapping("/{memberId}/nickname")
    public ResponseEntity<SuccessResponse<Message>> updateNickname(@PathVariable Long memberId, @RequestBody UpdateNicknameReq request) {
        return ResponseEntity.ok(memberService.updateNickname(memberId, request.getNickname()));
    }
}

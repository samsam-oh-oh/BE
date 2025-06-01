package samsamoo.ai_mockly.domain.member.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import samsamoo.ai_mockly.domain.member.application.MemberService;
import samsamoo.ai_mockly.domain.member.domain.Member;
import samsamoo.ai_mockly.domain.member.dto.request.UpdateNicknameReq;
import samsamoo.ai_mockly.domain.member.dto.response.MemberInfoRes;
import samsamoo.ai_mockly.global.annotation.LoginMember;
import samsamoo.ai_mockly.global.common.Message;
import samsamoo.ai_mockly.global.common.SuccessResponse;

import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/members")
public class MemberController implements MemberApi {

    private final MemberService memberService;

    @Override
    @GetMapping("/me")
    public ResponseEntity<SuccessResponse<MemberInfoRes>> getMemberInfo(@LoginMember Optional<Member> memberOpt) {
        Long memberId = memberOpt.map(Member::getId).orElse(null);
        return ResponseEntity.ok(memberService.getMemberInfo(memberId));
    }

    @Override
    @PatchMapping("/me/nickname")
    public ResponseEntity<SuccessResponse<Message>> updateNickname(@LoginMember Member member, @RequestBody UpdateNicknameReq request) {
        Long memberId = member.getId();
        return ResponseEntity.ok(memberService.updateNickname(memberId, request.getNickname()));
    }

    @Override
    @PatchMapping("/me/image")
    public ResponseEntity<SuccessResponse<Message>> modifyProfileImage(@LoginMember Member member, @RequestPart MultipartFile profileImage) {
        Long memberId = member.getId();
        return ResponseEntity.ok(memberService.modifyProfileImage(memberId, profileImage));
    }
}

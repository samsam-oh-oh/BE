package samsamoo.ai_mockly.domain.auth.presentation;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import samsamoo.ai_mockly.domain.auth.application.AuthService;
import samsamoo.ai_mockly.domain.auth.dto.request.LoginReq;
import samsamoo.ai_mockly.domain.auth.dto.request.LogoutReq;
import samsamoo.ai_mockly.domain.auth.dto.response.DuplicateCheckRes;
import samsamoo.ai_mockly.domain.auth.dto.response.LoginRes;
import samsamoo.ai_mockly.domain.member.domain.Member;
import samsamoo.ai_mockly.global.annotation.LoginMember;
import samsamoo.ai_mockly.global.common.Message;
import samsamoo.ai_mockly.global.common.SuccessResponse;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController implements AuthApi {

    private final AuthService authService;

    @Override
    @PostMapping("/login")
    public ResponseEntity<SuccessResponse<LoginRes>> signUpOrLogin(@Valid @RequestBody LoginReq loginReq) {
        return ResponseEntity.ok(authService.signUpOrLogin(loginReq));
    }

    @Override
    @GetMapping("/nicknames")
    public ResponseEntity<SuccessResponse<DuplicateCheckRes>> checkNicknameDuplicate(@RequestParam(value = "nickname") String nickname) {
        return ResponseEntity.ok(authService.checkNicknameDuplicate(nickname));
    }

    @Override
    @DeleteMapping("/logout")
    public ResponseEntity<SuccessResponse<Message>> logout(@LoginMember Member member, @RequestBody LogoutReq logoutReq) {
        Long memberId = member.getId();
        return ResponseEntity.ok(authService.logout(memberId, logoutReq));
    }

    @Override
    @DeleteMapping("/exit")
    public ResponseEntity<SuccessResponse<Message>> exit(@LoginMember Member member) {
        Long memberId = member.getId();
        return ResponseEntity.ok(authService.exit(memberId));
    }
}

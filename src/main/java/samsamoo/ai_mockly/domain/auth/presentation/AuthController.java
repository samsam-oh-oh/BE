package samsamoo.ai_mockly.domain.auth.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import samsamoo.ai_mockly.domain.auth.application.AuthService;
import samsamoo.ai_mockly.domain.auth.dto.response.DuplicateCheckRes;
import samsamoo.ai_mockly.global.common.SuccessResponse;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController implements AuthApi {

    private final AuthService authService;

    @Override
    @GetMapping("/nicknames")
    public ResponseEntity<SuccessResponse<DuplicateCheckRes>> checkNicknameDuplicate(@RequestParam(value = "nickname") String nickname) {
        return ResponseEntity.ok(authService.checkNicknameDuplicate(nickname));
    }
}

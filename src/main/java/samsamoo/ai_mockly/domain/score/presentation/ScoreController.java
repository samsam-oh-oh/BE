package samsamoo.ai_mockly.domain.score.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import samsamoo.ai_mockly.domain.member.domain.Member;
import samsamoo.ai_mockly.domain.score.application.ScoreService;
import samsamoo.ai_mockly.domain.score.dto.response.RankingListRes;
import samsamoo.ai_mockly.global.annotation.LoginMember;
import samsamoo.ai_mockly.global.common.SuccessResponse;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/scores")
public class ScoreController implements ScoreApi {

    private final ScoreService scoreService;

    @Override
    @GetMapping("/rank")
    public ResponseEntity<SuccessResponse<List<RankingListRes>>> getRankingList(@LoginMember Optional<Member> memberOpt) {
        Long memberId = memberOpt.map(Member::getId).orElse(null);
        return ResponseEntity.ok(scoreService.getRankingList(memberId));
    }
}

package samsamoo.ai_mockly.domain.score.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import samsamoo.ai_mockly.domain.score.application.ScoreService;
import samsamoo.ai_mockly.domain.score.dto.response.RankingListRes;
import samsamoo.ai_mockly.global.common.SuccessResponse;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/scores")
public class ScoreController {

    private final ScoreService scoreService;

    @GetMapping("/rank")
    public ResponseEntity<SuccessResponse<List<RankingListRes>>> getRankingList() {
        return ResponseEntity.ok(scoreService.getRankingList());
    }
}

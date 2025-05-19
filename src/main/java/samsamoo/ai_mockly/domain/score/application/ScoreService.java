package samsamoo.ai_mockly.domain.score.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import samsamoo.ai_mockly.domain.score.domain.Score;
import samsamoo.ai_mockly.domain.score.domain.repository.ScoreRepository;
import samsamoo.ai_mockly.domain.score.dto.response.RankingListRes;
import samsamoo.ai_mockly.global.common.SuccessResponse;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ScoreService {

    private final ScoreRepository scoreRepository;

    public SuccessResponse<List<RankingListRes>> getRankingList() {
        List<Score> scoreList = scoreRepository.findAllByHighScoreOrderByTotalScoreDesc(true);

        List<RankingListRes> rankingList = scoreList.stream()
                .limit(5)
                .map(score -> RankingListRes.builder()
                        .id(score.getMember().getId())
                        .nickname(score.getMember().getNickname())
                        .score(score.getTotalScore())
                        .build())
                .collect(Collectors.toList());

        return SuccessResponse.of(rankingList);
    }
}

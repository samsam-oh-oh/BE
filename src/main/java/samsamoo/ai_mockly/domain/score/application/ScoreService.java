package samsamoo.ai_mockly.domain.score.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import samsamoo.ai_mockly.domain.feedback.domain.Feedback;
import samsamoo.ai_mockly.domain.feedback.domain.repository.FeedbackRepository;
import samsamoo.ai_mockly.domain.feedbackaccess.repository.FeedbackAccessRepository;
import samsamoo.ai_mockly.domain.member.domain.Member;
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
    private final FeedbackRepository feedbackRepository;
    private final FeedbackAccessRepository feedbackAccessRepository;

    public SuccessResponse<List<RankingListRes>> getRankingList(Long memberId) {
        List<Score> scoreList = scoreRepository.findTop5ByHighScoreOrderByTotalScoreDesc(true);

        List<RankingListRes> rankingList = scoreList.stream()
                .map(score -> {
                    Member scoreMember = score.getMember();
                    Feedback feedback = feedbackRepository.findTopByMemberOrderByCreatedAtDesc(scoreMember)
                            .orElse(null);
                    Boolean unlocked = false;

                    if(memberId != null) {
                        unlocked = feedback.getMember().equals(scoreMember) || feedbackAccessRepository.existsByViewerAndFeedback(scoreMember, feedback);
                    }

                    return RankingListRes.builder()
                            .id(score.getMember().getId())
                            .nickname(score.getMember().getNickname())
                            .score(score.getTotalScore())
                            .feedback(feedback != null ? feedback.getContent() : null)
                            .unlocked(unlocked)
                            .build();
                })
                .collect(Collectors.toList());

        return SuccessResponse.of(rankingList);
    }
}

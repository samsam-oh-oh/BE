package samsamoo.ai_mockly.domain.score.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import samsamoo.ai_mockly.domain.feedback.domain.Feedback;
import samsamoo.ai_mockly.domain.feedback.domain.repository.FeedbackRepository;
import samsamoo.ai_mockly.domain.feedbackaccess.repository.FeedbackAccessRepository;
import samsamoo.ai_mockly.domain.member.domain.Member;
import samsamoo.ai_mockly.domain.member.domain.repository.MemberRepository;
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
    private final MemberRepository memberRepository;

    public SuccessResponse<List<RankingListRes>> getRankingList(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("해당 아이디를 갖는 유저가 없습니다."));

        List<Score> scoreList = scoreRepository.findTop5ByHighScoreOrderByTotalScoreDesc(true);

        List<RankingListRes> rankingList = scoreList.stream()
                .map(score -> {
                    Member scoreMember = score.getMember();
                    Feedback feedback = feedbackRepository.findTopByMemberOrderByCreatedAtDesc(scoreMember)
                            .orElse(null);
                    Boolean unlocked = false;

                    if(memberId != null && feedback != null) {
                        unlocked = feedback.getMember().equals(member) || feedbackAccessRepository.existsByViewerAndFeedback(member, feedback);
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

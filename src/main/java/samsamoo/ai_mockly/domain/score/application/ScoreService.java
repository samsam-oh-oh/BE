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
import samsamoo.ai_mockly.domain.scoredetails.domain.Category;
import samsamoo.ai_mockly.domain.scoredetails.domain.ScoreDetails;
import samsamoo.ai_mockly.global.common.SuccessResponse;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
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
        List<Score> scoreList = scoreRepository.findTop5ByHighScoreOrderByTotalScoreDesc(true);

        List<RankingListRes> rankingList = scoreList.stream()
                .map(score -> {
                    Member scoreMember = score.getMember();

                    Feedback feedback = feedbackRepository.findByMemberAndCreatedAt(scoreMember, score.getCreatedAt())
                            .orElse(null);

                    Boolean unlocked = false;

                    if(memberId != null && feedback != null) {
                        Member member = memberRepository.findById(memberId)
                                .orElseThrow(() -> new IllegalArgumentException("해당 아이디를 갖는 유저가 없습니다."));

                        unlocked = feedback.getMember().equals(member) || feedbackAccessRepository.existsByViewerAndFeedback(member, feedback);
                    }

                    List<ScoreDetails> details = score.getScoreDetails();

                    // 점수 계산(조화평균)
                    double technicalScore = harmonicMean(
                            details.stream()
                                    .filter(e -> Category.TECHNICAL_AND_PROBLEM_SOLVING_CATEGORY.contains(e.getCategory()))
                                    .map(ScoreDetails::getScoreValue)
                                    .filter(v ->  v > 0)
                                    .toList()
                    );

                    double communicationScore = harmonicMean(
                            details.stream()
                                    .filter(e -> Category.COMMUNICATION_AND_EXPRESSION_CATEGORY.contains(e.getCategory()))
                                    .map(ScoreDetails::getScoreValue)
                                    .filter(v ->  v > 0)
                                    .toList()
                    );

                    return RankingListRes.builder()
                            .id(score.getMember().getId())
                            .nickname(score.getMember().getNickname())
                            .techScore(round(technicalScore, 2))
                            .communicateScore(round(communicationScore, 2))
                            .totalScore(score.getTotalScore())
                            .feedback(feedback != null ? feedback.getContent() : null)
                            .unlocked(unlocked)
                            .build();
                })
                .collect(Collectors.toList());

        return SuccessResponse.of(rankingList);
    }

    // 조화 평균 계산 메서드
    private double harmonicMean(List<Integer> values) {
        List<Integer> nonZeroValues = values.stream().filter(v -> v > 0).toList();
        if(nonZeroValues.isEmpty()) return 0.0;
        double sum = nonZeroValues.stream().mapToDouble(v -> 1.0 / v).sum();
        return nonZeroValues.size() / sum;
    }
    // 소수점 자리 제한
    private double round(double value, int digits) {
        return BigDecimal.valueOf(value)
                .setScale(digits, RoundingMode.HALF_UP)
                .doubleValue();
    }
}

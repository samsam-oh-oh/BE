package samsamoo.ai_mockly.domain.feedbackaccess.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import samsamoo.ai_mockly.domain.feedback.domain.Feedback;
import samsamoo.ai_mockly.domain.feedbackaccess.FeedbackAccess;
import samsamoo.ai_mockly.domain.member.domain.Member;

public interface FeedbackAccessRepository extends JpaRepository<FeedbackAccess, Long> {

    boolean existsByViewerAndFeedback(Member viewer, Feedback feedback);
}

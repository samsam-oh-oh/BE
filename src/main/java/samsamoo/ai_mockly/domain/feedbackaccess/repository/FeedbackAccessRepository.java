package samsamoo.ai_mockly.domain.feedbackaccess.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import samsamoo.ai_mockly.domain.feedbackaccess.FeedbackAccess;

public interface FeedbackAccessRepository extends JpaRepository<FeedbackAccess, Long> {

}

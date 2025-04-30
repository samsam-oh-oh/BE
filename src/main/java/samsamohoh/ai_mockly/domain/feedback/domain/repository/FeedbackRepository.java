package samsamohoh.ai_mockly.domain.feedback.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import samsamohoh.ai_mockly.domain.feedback.domain.Feedback;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

}

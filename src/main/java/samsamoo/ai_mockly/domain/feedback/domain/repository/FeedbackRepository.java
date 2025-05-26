package samsamoo.ai_mockly.domain.feedback.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import samsamoo.ai_mockly.domain.feedback.domain.Feedback;
import samsamoo.ai_mockly.domain.member.domain.Member;

import java.util.List;
import java.util.Optional;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

    List<Feedback> findAllByMember(Member member);

    Optional<Feedback> findByMember(Member member);
}

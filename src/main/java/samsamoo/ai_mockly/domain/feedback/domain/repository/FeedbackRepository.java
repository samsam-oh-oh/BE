package samsamoo.ai_mockly.domain.feedback.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import samsamoo.ai_mockly.domain.feedback.domain.Feedback;
import samsamoo.ai_mockly.domain.member.domain.Member;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

    List<Feedback> findAllByMember(Member member);

    @Query("SELECT f FROM Feedback f WHERE f.member = :member AND f.createdAt BETWEEN :startTime AND :endTime " +
            "ORDER BY f.createdAt DESC LIMIT 1")
    Optional<Feedback> findByMemberAndCreatedAtBetweenOrderByCreatedAtDesc(@Param("member") Member member,
                                                                           @Param("startTime") LocalDateTime startTime,
                                                                           @Param("endTime") LocalDateTime endTime);
}

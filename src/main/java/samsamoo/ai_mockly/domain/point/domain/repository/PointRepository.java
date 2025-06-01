package samsamoo.ai_mockly.domain.point.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import samsamoo.ai_mockly.domain.member.domain.Member;
import samsamoo.ai_mockly.domain.point.domain.Point;

import java.util.List;

public interface PointRepository extends JpaRepository<Point, Long> {

    @Query("SELECT COALESCE(SUM(p.amount), 0) FROM Point p WHERE p.member = :member AND p.state = 'ACTIVE'")
    Integer sumActiveAmountByMember(@Param("member") Member member);

    List<Point> findAllByMember(Member member);
}

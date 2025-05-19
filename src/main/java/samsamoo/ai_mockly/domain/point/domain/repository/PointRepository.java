package samsamoo.ai_mockly.domain.point.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import samsamoo.ai_mockly.domain.point.domain.Point;

public interface PointRepository extends JpaRepository<Point, Long> {

    @Query("SELECT COALESCE(SUM(p.amount), 0) FROM Point p WHERE p.member.id = :memberId AND p.state = 'ACTIVE'")
    Integer sumActiveAmountByMemberId(@Param("memberId") Long memberId);
}

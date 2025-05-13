package samsamoo.ai_mockly.domain.member.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import samsamoo.ai_mockly.domain.member.domain.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {

}

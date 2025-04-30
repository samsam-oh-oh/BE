package samsamohoh.ai_mockly.domain.user.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import samsamohoh.ai_mockly.domain.user.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {

}

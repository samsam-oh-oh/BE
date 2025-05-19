package samsamoo.ai_mockly.domain.score.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import samsamoo.ai_mockly.domain.score.domain.Score;

import java.util.List;

public interface ScoreRepository extends JpaRepository<Score, Long> {

    List<Score> findTop5ByHighScoreOrderByTotalScoreDesc(boolean highScore);
}

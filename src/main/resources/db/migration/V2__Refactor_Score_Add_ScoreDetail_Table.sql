-- 기존 score 테이블에서 세부 점수 컬럼 제거
ALTER TABLE score
    DROP COLUMN logicality_score;
ALTER TABLE score
    DROP COLUMN concreteness_score;
ALTER TABLE score
    DROP COLUMN idealism_score;

-- 새 score_detail 테이블 생성
CREATE TABLE score_details
(
    id                 BIGINT AUTO_INCREMENT NOT NULL,
    created_at         datetime NOT NULL,
    updated_at         datetime NOT NULL,
    score_id           BIGINT       NOT NULL,
    category           VARCHAR(255) NOT NULL,   -- Enum(ScoreCategory) 값 저장
    score_value        INT          NOT NULL CHECK (score_value >= 0 AND score_value <= 100),
    CONSTRAINT pk_score_detail PRIMARY KEY (id),
    CONSTRAINT FK_SCORE_DETAIL_ON_SCORE FOREIGN KEY (score_id) REFERENCES score (id) ON DELETE CASCADE
);

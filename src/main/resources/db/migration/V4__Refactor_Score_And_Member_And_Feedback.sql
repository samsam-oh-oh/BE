ALTER TABLE member
    MODIFY max_score DOUBLE;

ALTER TABLE score
    MODIFY total_score DOUBLE NOT NULL;

ALTER TABLE feedback
    MODIFY COLUMN content TEXT;
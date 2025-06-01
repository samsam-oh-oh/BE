CREATE TABLE feedback_access
(
    id          BIGINT   AUTO_INCREMENT NOT NULL,
    created_at  datetime NOT NULL,
    updated_at  datetime NOT NULL,
    viewer_id   BIGINT   NOT NULL,
    feedback_id BIGINT   NOT NULL,
    CONSTRAINT pk_feedback_access PRIMARY KEY (id)
);

ALTER TABLE feedback_access
    ADD CONSTRAINT FK_FEEDBACK_ACCESS_ON_FEEDBACK FOREIGN KEY (feedback_id) REFERENCES feedback (id);

ALTER TABLE feedback_access
    ADD CONSTRAINT FK_FEEDBACK_ACCESS_ON_VIEWER FOREIGN KEY (viewer_id) REFERENCES member (id);
CREATE TABLE member
(
    id            BIGINT AUTO_INCREMENT NOT NULL,
    created_at    datetime     NOT NULL,
    updated_at    datetime     NOT NULL,
    kakao_id      VARCHAR(255) NOT NULL,
    nickname      VARCHAR(255) NOT NULL,
    profile_image VARCHAR(255) NULL,
    max_score     INT          NULL,
    CONSTRAINT pk_members PRIMARY KEY (id)
);

CREATE TABLE feedback
(
    id         BIGINT AUTO_INCREMENT NOT NULL,
    created_at datetime     NOT NULL,
    updated_at datetime     NOT NULL,
    member_id  BIGINT       NULL,
    content    VARCHAR(255) NULL,
    CONSTRAINT pk_feedback PRIMARY KEY (id)
);

CREATE TABLE point
(
    id         BIGINT AUTO_INCREMENT NOT NULL,
    created_at datetime     NOT NULL,
    updated_at datetime     NOT NULL,
    member_id  BIGINT       NOT NULL,
    amount     INT          NULL,
    type       VARCHAR(255) NULL,
    expired_at datetime     NULL,
    state      VARCHAR(255) NULL,
    CONSTRAINT pk_points PRIMARY KEY (id)
);

CREATE TABLE score
(
    id                 BIGINT AUTO_INCREMENT NOT NULL,
    created_at         datetime NOT NULL,
    updated_at         datetime NOT NULL,
    member_id          BIGINT   NULL,
    total_score        INT      NOT NULL,
    high_score         BIT(1)   NOT NULL,
    logicality_score   INT      NULL,
    concreteness_score INT      NULL,
    idealism_score     INT      NULL,
    CONSTRAINT pk_score PRIMARY KEY (id)
);

ALTER TABLE feedback
    ADD CONSTRAINT FK_FEEDBACK_ON_MEMBER FOREIGN KEY (member_id) REFERENCES member (id);

ALTER TABLE point
    ADD CONSTRAINT FK_POINTS_ON_MEMBER FOREIGN KEY (member_id) REFERENCES member (id);

ALTER TABLE score
    ADD CONSTRAINT FK_SCORE_ON_MEMBER FOREIGN KEY (member_id) REFERENCES member (id);
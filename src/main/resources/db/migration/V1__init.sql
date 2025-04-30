CREATE TABLE feedback
(
    id            BIGINT AUTO_INCREMENT NOT NULL,
    created_date  datetime     NOT NULL,
    modified_date datetime     NOT NULL,
    user_id       BIGINT       NULL,
    content       VARCHAR(255) NOT NULL,
    CONSTRAINT pk_feedback PRIMARY KEY (id)
);

CREATE TABLE point
(
    id            BIGINT AUTO_INCREMENT NOT NULL,
    created_date  datetime     NOT NULL,
    modified_date datetime     NOT NULL,
    user_id       BIGINT       NULL,
    amount        INT          NOT NULL,
    type          TEXT         NOT NULL,
    expired_date  datetime     NOT NULL,
    status        VARCHAR(255) NOT NULL,
    CONSTRAINT pk_point PRIMARY KEY (id)
);

CREATE TABLE score
(
    id                 BIGINT AUTO_INCREMENT NOT NULL,
    created_date       datetime NOT NULL,
    modified_date      datetime NOT NULL,
    user_id            BIGINT   NULL,
    total_score        INT      NOT NULL,
    high_score         BIT(1)                NOT NULL,
    logicality_score   INT      NOT NULL,
    concreteness_score INT      NOT NULL,
    idealism_score     INT      NOT NULL,
    CONSTRAINT pk_score PRIMARY KEY (id)
);

CREATE TABLE user
(
    id            BIGINT AUTO_INCREMENT NOT NULL,
    created_date  datetime     NOT NULL,
    modified_date datetime     NOT NULL,
    nickname      VARCHAR(255) NOT NULL,
    profile_image VARCHAR(255) NULL,
    state         VARCHAR(255) NOT NULL,
    max_score     INT          NULL,
    CONSTRAINT pk_user PRIMARY KEY (id)
);

ALTER TABLE feedback
    ADD CONSTRAINT FK_FEEDBACK_ON_USER FOREIGN KEY (user_id) REFERENCES user (id);

ALTER TABLE point
    ADD CONSTRAINT FK_POINT_ON_USER FOREIGN KEY (user_id) REFERENCES user (id);

ALTER TABLE score
    ADD CONSTRAINT FK_SCORE_ON_USER FOREIGN KEY (user_id) REFERENCES user (id);
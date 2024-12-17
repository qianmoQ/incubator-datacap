CREATE TABLE datacap_workflow
(
    id          BIGINT AUTO_INCREMENT NOT NULL,
    name        VARCHAR(255) NULL,
    code        VARCHAR(255) NULL,
    active      BIT(1) NULL,
    create_time datetime NULL,
    update_time datetime NULL,
    state       VARCHAR(255) NULL,
    message     VARCHAR(255) NULL,
    work        VARCHAR(255) NULL,
    elapsed     BIGINT NULL,
    executor    VARCHAR(255) NULL,
    configure   TEXT NULL,
    j_from_id   BIGINT NULL,
    j_to_id     BIGINT NULL,
    j_user_id   BIGINT NULL,
    CONSTRAINT pk_datacap_workflow PRIMARY KEY (id)
);

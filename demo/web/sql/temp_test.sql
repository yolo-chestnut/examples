-- 2020.3.24
CREATE TABLE temp_test.mb_user (
    id BIGINT UNSIGNED auto_increment NOT NULL,
    name varchar(50) NULL,
    org_id BIGINT UNSIGNED NULL,
    CONSTRAINT mb_user_PK PRIMARY KEY (id)
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8mb4
COLLATE=utf8mb4_0900_ai_ci;
-- 2020.3.24
CREATE TABLE temp_test.mb_org (
    id BIGINT UNSIGNED auto_increment NOT NULL,
    org_name varchar(50) NULL,
    CONSTRAINT mb_org_PK PRIMARY KEY (id)
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8mb4
COLLATE=utf8mb4_0900_ai_ci;

CREATE EXTENSION IF NOT EXISTS pgcrypto;
CREATE EXTENSION IF NOT EXISTS pg_trgm;

CREATE TABLE "user" (
                        id BIGSERIAL PRIMARY KEY,
                        username VARCHAR(50) NOT NULL UNIQUE,
                        password VARCHAR(255) NOT NULL,
                        nickname VARCHAR(50) NOT NULL UNIQUE,
                        email VARCHAR(100) NOT NULL UNIQUE,
                        role VARCHAR(20) NOT NULL DEFAULT 'USER',
                        created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
                        updated_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

-- 게시판
CREATE TABLE board (
                       id BIGSERIAL PRIMARY KEY,
                       name VARCHAR(100) NOT NULL,
                       slug VARCHAR(100) NOT NULL,
                       description TEXT,
                       visibility VARCHAR(20) NOT NULL DEFAULT 'PUBLIC',
                       sort_order INT NOT NULL DEFAULT 0,
                       created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
                       updated_at TIMESTAMPTZ NOT NULL DEFAULT now(),
                       CONSTRAINT ux_board_slug UNIQUE (slug)
);
-- 인덱스
CREATE INDEX idx_board_name ON board(name);

-- 게시글
CREATE TABLE post (
                      id BIGSERIAL PRIMARY KEY,
                      board_id BIGINT NOT NULL REFERENCES board(id),
                      user_id BIGINT NOT NULL REFERENCES "user"(id),
                      title VARCHAR(200) NOT NULL,
                      slug  VARCHAR(200) NOT NULL,
                      content TEXT NOT NULL,
                      status VARCHAR(20) NOT NULL DEFAULT 'PUBLISHED',
                      view_count BIGINT NOT NULL DEFAULT 0,
                      created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
                      updated_at TIMESTAMPTZ NOT NULL DEFAULT now(),
                      CONSTRAINT ux_post_board_slug UNIQUE (board_id, slug)
);

-- 인덱스
CREATE INDEX idx_post_board_created_at ON post(board_id, created_at DESC);

-- 댓글
CREATE TABLE comment (
                         id BIGSERIAL PRIMARY KEY,
                         post_id BIGINT NOT NULL REFERENCES post(id) ON DELETE CASCADE,
                         user_id BIGINT NOT NULL REFERENCES "user"(id),
                         content TEXT NOT NULL,
                         created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
                         updated_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

-- 인덱스
CREATE INDEX idx_comment_post_created_at ON comment(post_id, created_at);


-- 태그
CREATE TABLE tag (
                     id BIGSERIAL PRIMARY KEY,
                     name VARCHAR(50) NOT NULL UNIQUE,
                     created_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

-- 게시글-태그(다대다)
CREATE TABLE posttag (
                         post_id BIGINT NOT NULL REFERENCES post(id) ON DELETE CASCADE,
                         tag_id  BIGINT NOT NULL REFERENCES tag(id),
                         PRIMARY KEY (post_id, tag_id)
);
CREATE INDEX idx_posttag_post ON posttag(post_id);
CREATE INDEX idx_posttag_tag  ON posttag(tag_id);

-- 첨부파일 (엔티티 기준: Post FK 없음, 업로더/스토리지 메타만)
CREATE TABLE attachment (
                            id BIGSERIAL PRIMARY KEY,
                            original_name VARCHAR(255) NOT NULL,
                            stored_name   VARCHAR(255) NOT NULL,
                            content_type  VARCHAR(100) NOT NULL,
                            size          BIGINT NOT NULL,
                            storage_uri   TEXT NOT NULL,
                            uploader_id   BIGINT NOT NULL,
                            created_at    TIMESTAMPTZ NOT NULL DEFAULT now()
);

-- 좋아요(복합키)
CREATE TABLE postlike (
                          user_id BIGINT NOT NULL REFERENCES "user"(id) ON DELETE CASCADE,
                          post_id BIGINT NOT NULL REFERENCES post(id)    ON DELETE CASCADE,
                          created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
                          PRIMARY KEY (user_id, post_id)
);

CREATE TABLE bookmark (
                          user_id BIGINT NOT NULL REFERENCES "user"(id) ON DELETE CASCADE,
                          post_id BIGINT NOT NULL REFERENCES post(id) ON DELETE CASCADE,
                          created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
                          PRIMARY KEY (user_id, post_id)
);

CREATE TABLE refreshtoken (
                              id BIGSERIAL PRIMARY KEY,
                              user_id BIGINT NOT NULL REFERENCES "user"(id) ON DELETE CASCADE,
                              token TEXT NOT NULL UNIQUE,
                              expires_at TIMESTAMPTZ NOT NULL,
                              created_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE report (
                        id BIGSERIAL PRIMARY KEY,
                        reporter_id BIGINT NOT NULL REFERENCES "user"(id),
                        target_type TEXT NOT NULL, -- POST/COMMENT
                        target_id BIGINT NOT NULL,
                        reason TEXT,
                        status TEXT NOT NULL DEFAULT 'OPEN',
                        created_at TIMESTAMPTZ NOT NULL DEFAULT now()
);
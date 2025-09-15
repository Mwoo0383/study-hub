CREATE EXTENSION IF NOT EXISTS pgcrypto;
CREATE EXTENSION IF NOT EXISTS pg_trgm;

CREATE TABLE "user" (
                        id BIGSERIAL PRIMARY KEY,
                        email TEXT NOT NULL UNIQUE,
                        password_hash TEXT NOT NULL,
                        nickname TEXT NOT NULL UNIQUE,
                        role TEXT NOT NULL DEFAULT 'ROLE_USER',
                        status TEXT NOT NULL DEFAULT 'ACTIVE',
                        created_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE board (
                       id BIGSERIAL PRIMARY KEY,
                       name TEXT NOT NULL UNIQUE,
                       created_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE post (
                      id BIGSERIAL PRIMARY KEY,
                      board_id BIGINT NOT NULL REFERENCES board(id),
                      author_id BIGINT NOT NULL REFERENCES "user"(id),
                      title TEXT NOT NULL,
                      content TEXT NOT NULL,
                      is_deleted BOOLEAN NOT NULL DEFAULT false,
                      view_count INT NOT NULL DEFAULT 0,
                      created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
                      updated_at TIMESTAMPTZ
);

CREATE TABLE comment (
                         id BIGSERIAL PRIMARY KEY,
                         post_id BIGINT NOT NULL REFERENCES post(id) ON DELETE CASCADE,
                         author_id BIGINT NOT NULL REFERENCES "user"(id),
                         content TEXT NOT NULL,
                         parent_id BIGINT,
                         is_deleted BOOLEAN NOT NULL DEFAULT false,
                         created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
                         updated_at TIMESTAMPTZ
);

CREATE TABLE tag (
                     id BIGSERIAL PRIMARY KEY,
                     name TEXT NOT NULL UNIQUE
);

CREATE TABLE posttag (
                         post_id BIGINT NOT NULL REFERENCES post(id) ON DELETE CASCADE,
                         tag_id BIGINT NOT NULL REFERENCES tag(id),
                         PRIMARY KEY (post_id, tag_id)
);

CREATE TABLE attachment (
                            id BIGSERIAL PRIMARY KEY,
                            post_id BIGINT NOT NULL REFERENCES post(id) ON DELETE CASCADE,
                            original_name TEXT NOT NULL,
                            stored_name TEXT NOT NULL,
                            size INT NOT NULL,
                            content_type TEXT,
                            created_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE postlike (
                          user_id BIGINT NOT NULL REFERENCES "user"(id) ON DELETE CASCADE,
                          post_id BIGINT NOT NULL REFERENCES post(id) ON DELETE CASCADE,
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

-- 인덱스
CREATE INDEX idx_post_board_created ON post(board_id, created_at DESC) WHERE is_deleted = false;
CREATE INDEX idx_post_title_trgm   ON post USING GIN (title gin_trgm_ops);
CREATE INDEX idx_post_content_trgm ON post USING GIN (content gin_trgm_ops);
CREATE INDEX idx_posttag_post ON posttag(post_id);
CREATE INDEX idx_posttag_tag  ON posttag(tag_id);
CREATE INDEX idx_comment_post_created ON comment(post_id, created_at);
CREATE UNIQUE INDEX ux_user_email    ON "user"(email);
CREATE UNIQUE INDEX ux_user_nickname ON "user"(nickname);
-- 게시글 테이블
CREATE TABLE IF NOT EXISTS post
(
    id           BIGSERIAL    PRIMARY KEY,
    board_id     BIGINT       NOT NULL REFERENCES board(id) ON DELETE RESTRICT,
    title        VARCHAR(200) NOT NULL,
    slug         VARCHAR(200) NOT NULL,
    content      TEXT         NOT NULL,
    status       VARCHAR(20)  NOT NULL DEFAULT 'PUBLISHED', -- DRAFT/PUBLISHED/ARCHIVED
    view_count   BIGINT       NOT NULL DEFAULT 0,
    created_at   TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
    updated_at   TIMESTAMPTZ  NOT NULL DEFAULT NOW(),

    -- 🟢 여기서 바로 유니크 제약을 건다 (인덱스가 slug를 앞서 참조하지 않도록)
    CONSTRAINT ux_post_board_slug UNIQUE (board_id, slug)
);

-- 조회 최적화 인덱스
CREATE INDEX IF NOT EXISTS idx_post_board_created_at ON post(board_id, created_at DESC);

-- updated_at 자동 갱신 트리거
DO $$
    BEGIN
        IF NOT EXISTS (SELECT 1 FROM pg_trigger WHERE tgname = 'trg_post_updated_at') THEN
            CREATE OR REPLACE FUNCTION set_post_updated_at()
                RETURNS TRIGGER AS $func$
            BEGIN
                NEW.updated_at := NOW();
                RETURN NEW;
            END;
            $func$ LANGUAGE plpgsql;

            CREATE TRIGGER trg_post_updated_at
                BEFORE UPDATE ON post
                FOR EACH ROW
            EXECUTE FUNCTION set_post_updated_at();
        END IF;
    END$$;

-- 게시판 테이블
CREATE TABLE IF NOT EXISTS board
(
    id           BIGSERIAL PRIMARY KEY,
    name         VARCHAR(100)  NOT NULL,              -- 게시판 이름
    slug         VARCHAR(100)  NOT NULL,              -- URL용 식별자 (영문/하이픈)
    description  TEXT          NULL,
    visibility   VARCHAR(20)   NOT NULL DEFAULT 'PUBLIC',  -- PUBLIC/PRIVATE
    sort_order   INT           NOT NULL DEFAULT 0,     -- 노출 순서

    created_at   TIMESTAMPTZ   NOT NULL DEFAULT NOW(),
    updated_at   TIMESTAMPTZ   NOT NULL DEFAULT NOW()
    );

-- slug는 URL 라우팅에 쓰이므로 유니크 권장
CREATE UNIQUE INDEX IF NOT EXISTS ux_board_slug ON board(slug);

-- 이름으로도 조회 가능하면 인덱스
-- CREATE INDEX IF NOT EXISTS idx_board_name ON board(name);

-- updated_at 자동 갱신(선택: 트리거)
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM pg_trigger WHERE tgname = 'trg_board_updated_at'
    ) THEN
        CREATE OR REPLACE FUNCTION set_board_updated_at()
        RETURNS TRIGGER AS $func$
BEGIN
            NEW.updated_at := NOW();
RETURN NEW;
END;
        $func$ LANGUAGE plpgsql;

CREATE TRIGGER trg_board_updated_at
    BEFORE UPDATE ON board
    FOR EACH ROW
    EXECUTE FUNCTION set_board_updated_at();
END IF;
END$$;

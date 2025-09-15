-- V2__create_board_table.sql

CREATE TABLE IF NOT EXISTS board
(
    id           BIGSERIAL PRIMARY KEY,
    name         VARCHAR(100)  NOT NULL,
    slug         VARCHAR(100)  NOT NULL UNIQUE,  -- ← 여기서 바로 UNIQUE
    description  TEXT          NULL,
    visibility   VARCHAR(20)   NOT NULL DEFAULT 'PUBLIC',
    sort_order   INT           NOT NULL DEFAULT 0,
    created_at   TIMESTAMPTZ   NOT NULL DEFAULT NOW(),
    updated_at   TIMESTAMPTZ   NOT NULL DEFAULT NOW()
);

-- (선택) 이름 인덱스
CREATE INDEX IF NOT EXISTS idx_board_name ON board(name);

-- updated_at 자동 갱신 트리거
DO $$
    BEGIN
        IF NOT EXISTS (SELECT 1 FROM pg_trigger WHERE tgname = 'trg_board_updated_at') THEN
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

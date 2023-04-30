-- psql -f $PWD/sql/01_schema.sql postgresql://springuser:secret@localhost:5432/springdb?sslmode=require
DROP TABLE IF EXISTS "issues";
CREATE TABLE IF NOT EXISTS "issues" (
   "id" BIGSERIAL PRIMARY KEY,
   "owner_id" BIGINT NOT NULL REFERENCES "users",
   "name" TEXT NOT NULL,
   "content" TEXT NOT NULL,
   "closed" BOOLEAN NOT NULL DEFAULT FALSE,
   -- "removed" TIMESTAMPTZ
   "created" TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS "tasks" (
   "id" BIGSERIAL PRIMARY KEY,
   "owner_id" BIGINT NOT NULL REFERENCES "users",
   "name" TEXT NOT NULL,
   "content" TEXT NOT NULL,
   "closed" BOOLEAN NOT NULL DEFAULT FALSE,
   "created" TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);
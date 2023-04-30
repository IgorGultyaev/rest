
-- psql -f $PWD/sql/05_schema.sql postgresql://springuser:secret@localhost:5432/springdb?sslmode=require
DROP TABLE IF EXISTS "users";
CREATE TABLE "users" (
   "id" BIGSERIAL PRIMARY KEY,
   "login" TEXT UNIQUE NOT NULL,
   "password" TEXT NOT NULL,
   "profile" JSONB NOT NULL DEFAULT '{"avatar": "system.svg"}',
   "roles" TEXT[] NOT NULL DEFAULT '{ROLE_USER}',
   "account_non_expired" BOOLEAN DEFAULT TRUE,
   "account_non_locked" BOOLEAN DEFAULT TRUE,
   "credentials_non_expired" BOOLEAN DEFAULT TRUE,
   "enabled" BOOLEAN DEFAULT TRUE,
   "created" TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);



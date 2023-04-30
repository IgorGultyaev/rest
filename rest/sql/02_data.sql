-- psql -f $PWD/sql/02_data.sql postgresql://springuser:secret@localhost:5432/springdb?sslmode=require
INSERT INTO "issues" ("owner_id", "name", "content")
VALUES
(2, 'first issue', 'first issue content'),
(2, 'second issue', 'second issue content'),
(2, 'third issue', 'third issue content');


-- INSERT 0 3



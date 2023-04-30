-- psql -f $PWD/sql/02_data.sql postgresql://springuser:secret@localhost:5432/springdb?sslmode=require
INSERT INTO "task" ("name", "task")
VALUES
('first task', 'first task name'),
('second task', 'second task name'),
('third task', 'third task name');

-- JUST FOR DEMO (not hardcode admin users)
-- psql -f $PWD/sql/06_data.sql postgresql://springuser:secret@localhost:5432/springdb?sslmode=require


INSERT INTO "users"
(
   "login",
   "password",
   "roles"
)
VALUES
(
   'admin',
   '$2a$10$yFejlukusqsupzwOwxbX2eflubcCUUieCQNdbSWcAZ7EA579Me9N2', -- spring encodepassword -a bcrypt secret
   '{ROLE_ADMIN, ROLE_USER}'
),
(
   'user',
   '$2a$10$yFejlukusqsupzwOwxbX2eflubcCUUieCQNdbSWcAZ7EA579Me9N2', -- spring encodepassword -a bcrypt secret
   DEFAULT
)
;





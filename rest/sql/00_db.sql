-- file: sql/00_db.sql
-- execute in terminal: sudo -u postgres psql -f $PWD/sql/00_db.sql
create database springdb;
create user springuser with encrypted password 'secret';
grant all privileges on database springdb to springuser;



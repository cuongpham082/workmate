CREATE DATABASE workmate;

CREATE TABLE role
(
  id bigserial NOT NULL,
  name character varying(20),
  stamp bigint,
  CONSTRAINT role_pkey PRIMARY KEY (id)
);
INSERT INTO role (name, stamp) VALUES
('ROLE_ADMIN', 1), ('ROLE_MODERATOR', 1), ('ROLE_USER', 1);

CREATE TABLE account
(
  id bigserial NOT NULL,
  logo character varying(200),
  name character varying(200),
  short_name character varying(3),
  stamp bigint,
  CONSTRAINT account_pkey PRIMARY KEY (id),
  CONSTRAINT short_name_unique UNIQUE (short_name),
  CONSTRAINT name_unique UNIQUE (name)
);
INSERT INTO account (name, short_name, logo, stamp) VALUES
('Seer Pharma', 'SEE', '', 1);

CREATE TABLE users
(
  id bigserial NOT NULL,
  email character varying(50),
  password character varying(120),
  stamp bigint,
  username character varying(20),
  company_id bigint,
  CONSTRAINT users_pkey PRIMARY KEY (id),
  CONSTRAINT company_id_fkey FOREIGN KEY (company_id)
      REFERENCES company (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT email_unique UNIQUE (email),
  CONSTRAINT username_unique UNIQUE (username)
);
INSERT INTO users (email, username, password, stamp, company_id) VALUES
('admin@testingemail.com', 'admin', '$2a$10$8BzgQDkHCUihfBOkkSg9ZelpONaynV06DgKP/audHEw4z3mX4L1bK', 1, 1);

CREATE TABLE user_roles
(
  user_id bigint NOT NULL,
  role_id bigint NOT NULL,
  CONSTRAINT user_roles_pkey PRIMARY KEY (user_id, role_id),
  CONSTRAINT user_id_fkey FOREIGN KEY (user_id)
      REFERENCES users (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT role_id_fkey FOREIGN KEY (role_id)
      REFERENCES role (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);
INSERT INTO user_roles (user_id, role_id) VALUES
(1, 1);
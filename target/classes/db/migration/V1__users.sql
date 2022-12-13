DROP TABLE IF EXISTS users;

CREATE TABLE users (
  id bigserial PRIMARY KEY,
  username varchar(50) NOT NULL UNIQUE,
  email varchar(150) NOT NULL UNIQUE,
  password varchar(60) NOT NULL,
  image varchar(250),
  enabled boolean NOT NULL
);

CREATE TABLE authorities (
  id bigserial PRIMARY KEY,
  username varchar(50) NOT NULL,
  email varchar(150) NOT NULL,
  authority varchar(50) NOT NULL,
  constraint fk_authorities_users foreign key(username) references users(username) ON DELETE CASCADE
);

create unique index ix_auth_username on authorities(username, authority);
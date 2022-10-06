DROP TABLE IF EXISTS watchlist;

CREATE TABLE watchlist (
  id bigserial PRIMARY KEY,
  userid bigserial NOT NULL,
  movieid integer NOT NULL,
  constraint fk_userid foreign key(userid) references users(id),
  UNIQUE (userid, movieid)
);
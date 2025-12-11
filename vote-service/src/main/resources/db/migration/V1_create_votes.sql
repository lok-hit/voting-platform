CREATE TABLE votes (
  id BIGSERIAL PRIMARY KEY,
  election_id BIGINT NOT NULL,
  option_id BIGINT NOT NULL,
  voter_id BIGINT NOT NULL,
  created_at TIMESTAMP WITH TIME ZONE DEFAULT now()
);

CREATE UNIQUE INDEX ux_votes_election_voter ON votes(election_id, voter_id);
CREATE INDEX idx_votes_option_id ON votes(option_id);
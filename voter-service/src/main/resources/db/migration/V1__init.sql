-- src/main/resources/db/migration/V1__init.sql

CREATE TABLE IF NOT EXISTS voters (
    id UUID PRIMARY KEY,
    email TEXT NOT NULL UNIQUE,
    status TEXT NOT NULL CHECK (status IN ('ACTIVE','BLOCKED')),
    registered_at TIMESTAMPTZ NOT NULL
);

CREATE TABLE IF NOT EXISTS votes (
    id UUID PRIMARY KEY,
    voter_id UUID NOT NULL REFERENCES voters(id),
    election_id TEXT NOT NULL,
    option_id TEXT NOT NULL,
    cast_at TIMESTAMPTZ NOT NULL,
    CONSTRAINT uq_voter_election UNIQUE (voter_id, election_id)
);

CREATE INDEX IF NOT EXISTS idx_votes_voter_id ON votes (voter_id);
CREATE INDEX IF NOT EXISTS idx_votes_election_id ON votes (election_id);
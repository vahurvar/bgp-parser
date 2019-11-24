CREATE TABLE prefix_temp(
    prefix_cidr cidr NOT NULL,
    prefix_timestamp TIMESTAMP NOT NULL,
    as_path TEXT NOT NULL,
    unique(prefix_cidr, prefix_timestamp, as_path)
);
CREATE TYPE origin_type AS ENUM ('EGP', 'IGP', 'INCOMPLETE');

CREATE TABLE bgp(
    id BIGSERIAL PRIMARY KEY,
    dump_timestamp TIMESTAMP NOT NULL,
    prefix cidr NOT NULL,
    from_ip inet NOT NULL,
    from_asn VARCHAR(10) NOT NULL,
    as_path VARCHAR(10)[] NOT NULL,
    origin origin_type NOT NULL,
    next_hop inet NOT NULL,
    file_name VARCHAR(50)
);

CREATE INDEX bgp_prefix ON bgp(from_ip);
CREATE INDEX bgp_from_ip ON bgp(from_ip);
CREATE INDEX bgp_from_asn ON bgp(from_asn);

CREATE TABLE prefix_timestamp(
    id BIGSERIAL PRIMARY KEY,
    prefix_cidr cidr,
    prefix_date date,
    unique (prefix_cidr, prefix_date)
);

CREATE INDEX prefix_timestamp_cidr ON prefix_timestamp(prefix_cidr);
CREATE INDEX prefix_timestamp_date ON prefix_timestamp(prefix_date);
CREATE INDEX prefix_timestamp_date_cidr ON prefix_timestamp(prefix_cidr, prefix_date);

CREATE VIEW prefix_count AS SELECT prefix_cidr, count(1) from prefix_timestamp group by prefix_cidr;
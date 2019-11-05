CREATE TYPE origin_type AS ENUM ('EGP', 'IGP', 'INCOMPLETE');

CREATE TABLE bgp(
    id SERIAL PRIMARY KEY,
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
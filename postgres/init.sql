create table prefix_temp (
	prefix_cidr cidr,
	prefix_timestamp integer[],
	as_path text
);

create table prefix_data (
	prefix_cidr cidr,
	prefix_timestamp integer,
	as_path text
);
create table prefix_temp (
	prefix_cidr cidr,
	prefix_timestamp integer,
	as_path text
);

create table prefix_data (
	prefix_cidr cidr,
	prefix_timestamp integer,
	as_path text
);

create table prefix_cidr
(
	timestamps integer[],
	dates date[],
	first_seen date,
	last_seen date,
	count_dates smallint,
	count_timestamps smallint,
	timestamps_ts timestamp without time zone[],
	prefix_id integer,
	id serial not null
		constraint prefix_cidr_pkey
			primary key
);

create index idx_prefix_cidr_first_seen
	on prefix_cidr (first_seen);

create index idx_prefix_cidr_last_seen
	on prefix_cidr (last_seen);

create index idx_prefix_cidr_count
	on prefix_cidr (count_dates);

create index idx_prefix_cidr_count_timestamps
	on prefix_cidr (count_timestamps);

create index idx_prefix_cidr
	on prefix_cidr (dates);

create index idx_prefix_cidr_dates
	on prefix_cidr (dates);

create index prefix_cidr_prefix_id
	on prefix_cidr (prefix_id);

create table path
(
	as_path text,
	id serial not null
		constraint test_t_pkey
			primary key,
	paths bigint[],
	announced_by bigint,
	last_hop bigint
);

create index idx_path_announced_by
	on path (announced_by);

create index idx_path_last_hop
	on path (last_hop);

create index idx_path_as_path
	on path (as_path);

create table prefix
(
	prefix cidr,
	id serial not null
		constraint prefix_pkey
			primary key,
	nr_supernets smallint,
	is_local boolean
);

create index idx_prefix_prefix
	on prefix (prefix);

create index idx_prefix_is_local
	on prefix (is_local);

create index idx_prefix_nr_supernets
	on prefix (nr_supernets);

create table selection
(
	prefix cidr,
	first_seen date,
	last_seen date,
	announced_by bigint,
	last_hop bigint,
	prefix_country varchar(2),
	last_hop_country varchar(2),
	announced_by_country varchar(2)
);

create table prefix_path_ts
(
	prefix_timestamp integer[],
	path_id integer
		constraint fk_prefix_path_ts_path_id
			references path,
	prefix_id integer
		constraint fk_prefix_path_ts_prefix_id
			references prefix
);

create index idx_prefix_path_ts_prefix_id
	on prefix_path_ts (prefix_id);

create index idx_prefix_path_ts_path_id
	on prefix_path_ts (path_id);

create index idx_prefix_path_ts_prefix_id_path_id
	on prefix_path_ts (prefix_id, path_id);


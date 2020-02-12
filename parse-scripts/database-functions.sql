create function array_distinct(anyarray) returns anyarray
	immutable
	language sql
as $$
SELECT array_agg(DISTINCT x) FROM unnest($1) t(x);
$$;

create function array_greatest(anyarray) returns anyelement
	language sql
as $$
SELECT max(x) FROM unnest($1) as x;
$$;

create function array_smallest(anyarray) returns anyelement
	language sql
as $$
SELECT min(x) FROM unnest($1) as x;
$$;

create function to_ts(integer[]) returns timestamp without time zone[]
	stable
	strict
	language plpgsql
as $$
DECLARE
    arr ALIAS FOR $1;
    retVal timestamp[];
BEGIN
    FOR i IN 1 .. array_upper(arr, 1) LOOP
            retVal[i] := to_timestamp(arr[i]);
        END LOOP;
    RETURN retVal;
END;
$$;

create function get_all_not_announced_last_x_days(date date, nr_days_before int)
    RETURNS TABLE(prefix cidr) AS
$body$
SELECT p.prefix FROM prefix_cidr
JOIN prefix p on prefix_cidr.prefix_id = p.id
WHERE first_seen >= date AND last_seen <= date
  AND dates @> array[date]
  AND NOT dates && (
    SELECT array_agg(date_trunc('day', dd):: date)FROM generate_series((date-nr_days_before)::timestamp , (date-1)::timestamp , '1 day'::interval) dd
);
$body$
    language sql;


create function get_all_not_announced_last_x_days_and_after_y(date date, nr_days_before int, nr_days_after int)
    RETURNS TABLE(prefix cidr) AS
$body$
SELECT p.prefix FROM prefix_cidr
JOIN prefix p on prefix_cidr.prefix_id = p.id
WHERE first_seen >= date AND last_seen <= date
  AND dates @> array[date]
  AND NOT dates && (
    SELECT array_agg(date_trunc('day', dd):: date)FROM generate_series((date-nr_days_before)::timestamp , (date-1)::timestamp , '1 day'::interval) dd
)
  AND NOT dates && (
    SELECT array_agg(date_trunc('day', dd):: date)FROM generate_series((date+1)::timestamp , (date+nr_days_after)::timestamp , '1 day'::interval) dd
);
$body$
    language sql;

create function isnumeric(text) returns boolean
	immutable
	strict
	language plpgsql
as $$
DECLARE x NUMERIC;
BEGIN
    x = $1::NUMERIC;
    RETURN TRUE;
EXCEPTION WHEN others THEN
    RETURN FALSE;
END;
$$;

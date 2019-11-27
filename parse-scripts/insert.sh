for var in "$@"
do
    echo "$var"
    PGPASSWORD= psql -h 127.0.0.1 -p 80 -U root bgp -c "\copy prefix_temp(prefix_timestamp, prefix_cidr, as_path) FROM '/home/varrisvahur/$var' WITH DELIMITER ',' CSV;"
    rm $var
done
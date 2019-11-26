for var in "$@"
do
    echo "$var"
    psql -U root bgp -c "COPY prefix_temp(prefix_timestamp, prefix_cidr, as_path) FROM '/$var' WITH DELIMITER ',' CSV;"
    rm $var
done
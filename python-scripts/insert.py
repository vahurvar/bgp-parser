import sys, os

if __name__ == "__main__":
    os.system('PGPASSWORD=')
    for i, arg in enumerate(sys.argv):
        if i != 0:
            print('Parsing', arg)
            os.system("psql -h 127.0.0.1 -p 80 -U root bgp -c 'COPY prefix_temp(prefix_timestamp, prefix_cidr, as_path) FROM '" + arg + "' WITH DELIMITER ',' CSV;'")
            os.system('rm ' + arg)

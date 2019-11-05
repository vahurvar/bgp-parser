import psycopg2
from config import config

class Dao:

    def __init__(self, *args, **kwargs):
        self.params = config()

    def test(self):
        """ Connect to the PostgreSQL database server """
        conn = None
        try:
            conn = psycopg2.connect(**self.params)

            cur = conn.cursor()
            
            cur.execute('SELECT version()')
            db_version = cur.fetchone()
            print(db_version)
        
            cur.close()
        except (Exception, psycopg2.DatabaseError) as error:
            print(error)
        finally:
            if conn is not None:
                conn.close()
                print('Database connection closed.')

    def insert_prefix(self, dicts):
        sql = """ INSERT INTO prefix(dump_timestamp, prefix, dump_type, from_ip, from_asn, origin, next_hop) 
        VALUES(to_timestamp(%s), %s, %s, %s, %s, %s, %s) """
        
        conn = None
        
        try:
            conn = psycopg2.connect(**self.params)
        
            cursor = conn.cursor()

            cursor.executemany(sql, [ self.get_prfix_tuple(d) for d in dicts])

            # for bgp_dict in dicts:
                # cursor.execute(sql, self.get_prfix_tuple(bgp_dict))
                
                # cursor.execute('SELECT LASTVAL()')
                # last_id = cursor.fetchone()[0]
                
                # as_path = [ (bgp_dict['as_path'][i], i, last_id) for i in range(len(bgp_dict['as_path']))]
                # cursor.executemany("INSERT INTO as_path(asn, path_index, prefix_id) VALUES(%s, %s, %s)", as_path)

            conn.commit()

            cursor.close()
        except (Exception, psycopg2.DatabaseError) as error:
            print(error)
        finally:
            if conn is not None:
                conn.close()

    def get_prfix_tuple(self, bgp_dict):
            return (
                bgp_dict['dump_timestamp'],
                bgp_dict['prefix'],
                bgp_dict['dump_type'],
                bgp_dict['from_ip'],
                bgp_dict['from_asn'],
                bgp_dict['origin'],
                bgp_dict['next_hop']
        )
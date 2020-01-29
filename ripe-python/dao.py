import psycopg2
from config import config

class Dao:

    def __init__(self, *args, **kwargs):
        self.params = config()

    def select(self, query):
        conn = None
        results = []
        
        try:
            conn = psycopg2.connect(**self.params)
            cursor = conn.cursor()
            cursor.execute(query)
            results = cursor.fetchall()
            cursor.close()
        except (Exception, psycopg2.DatabaseError) as error:
            print(error)
        finally:
            if conn is not None:
                conn.close()

        return list(map(lambda res: res[0], results))
    
    def test(self):
        print(self.select('SELECT version()'))

    def update(self, sql, params):
        conn = None
        
        try:
            conn = psycopg2.connect(**self.params)    
            cursor = conn.cursor()
            cursor.execute(sql, params)
            conn.commit()
            cursor.close()
        except (Exception, psycopg2.DatabaseError) as error:
            print(error)
        finally:
            if conn is not None:
                conn.close()

    def get_all_prefixes_from_selection_without_country(self):
        return self.select("SELECT DISTINCT prefix FROM selection WHERE prefix_country IS NULL")

    def get_all_last_hops_from_selection(self):
        return self.select("SELECT DISTINCT last_hop FROM selection WHERE last_hop_country IS NULL AND last_hop IS NOT NULL")

    def get_all_anouncments_from_selection(self):
        return self.select("SELECT DISTINCT announced_by FROM selection WHERE announced_by_country IS NULL")

    def update_prefix_country_selection(self, prefix, prefix_country):
        self.update("UPDATE selection SET prefix_country =%s WHERE prefix = %s", (prefix_country, prefix))

    def update_last_hop_country_selection(self, last_hop, last_hop_country):
        self.update("UPDATE selection SET last_hop_country =%s WHERE last_hop = %s", (last_hop_country, last_hop))

    def update_announced_by_country_selection(self, announced_by_country, announced_by):
        self.update("UPDATE selection SET announced_by_country =%s WHERE announced_by = %s", (announced_by, announced_by_country))
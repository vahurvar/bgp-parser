from dao import Dao
import json
import requests

def get_country(prefix):
    reply = requests.get('https://stat.ripe.net/data/maxmind-geo-lite/data.json?resource=' + str(prefix))
    locations = reply.json()['data']['located_resources']
    if len(locations) != 0:
        return locations[0]['locations'][0]['country']
    else:
        return '?'

def get_as_country(asn):
    reply = requests.get('https://stat.ripe.net/data/maxmind-geo-lite-announced-by-as/data.json?resource=AS' + str(asn))
    locations = reply.json()['data']['located_resources']
    if len(locations) != 0:
        country = '?'
        percentage = 0
        for loc in locations:
            c = loc['locations'][0]['country']
            per = loc['locations'][0]['covered_percentage']
            if per > percentage:
                percentage = per
                country = c

        return country
    else:
        return '?'

if __name__ == "__main__":
    d = Dao()
    d.test()

    res = d.get_all_prefixes_from_selection_without_country()

    print('Fetched {} prefixes'.format(len(res)))

    for prefix in res:
        country = get_country(prefix)
        print(prefix, country)
        d.update_prefix_country_selection(prefix, country)

    last_hops = d.get_all_last_hops_from_selection()
    print('Fetched {} last hop AS'.format(len(last_hops)))

    for asn in last_hops:
        country = get_as_country(asn)
        print(asn, country)
        d.update_last_hop_country_selection(asn, country)    
    
    announced = d.get_all_anouncments_from_selection()
    print('Fetched {} announced AS'.format(len(announced)))

    for asn in announced:
        country = get_as_country(asn)
        print(asn, country)
        d.update_announced_by_country_selection(asn, country)
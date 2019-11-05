from dao import Dao

d = Dao()
n = 1_000_000

def parse(line):
    split = line.split('|')
    # print(split)
    return {
        'dump_type': split[0],
        'dump_timestamp': split[1],
        'from_ip': split[3],
        'from_asn': split[4],
        'prefix': split[5],
        'as_path': split[6].split(' '),
        'origin': split[7],
        'next_hop': split[8],
        }

with open("out") as infile:
    counter = 0
    prefixes = []
    for line in infile:
        prefixes.append(parse(line))
        if counter == n:
            print('Writing {} entries to DB'.format(len(prefixes)))
            d.insert_prefix(prefixes)
            prefixes = []
            counter = 0
        counter += 1

    if len(prefixes) != 0:
        d.insert_prefix(prefixes)
import pandas as pd
import sys, os, csv

columns = [1, 5, 6]
next_hop_idx = 8
as_path_index = 6

def check_if_ipv4(ip):
    if isinstance(ip, str):
        if ip.count('.') == 3:
            return True
    return False

def writeout(df, file):
    with open(file, 'a') as f:
        df.to_csv(f, header=False, index=False, quoting=csv.QUOTE_NONNUMERIC)

def parse(filename, chunksize=(10 ** 6)*3):
    for chunk in pd.read_csv(filename, sep='|', header=None, chunksize=chunksize):
        filter = chunk[chunk[next_hop_idx].apply(check_if_ipv4) == True]            
        writeout(filter[columns], filename + '.csv')

if __name__ == "__main__":
    for i, arg in enumerate(sys.argv):
        if i != 0:
            print('Parsing', arg)
            parse(arg)
            os.system('rm ' + arg)

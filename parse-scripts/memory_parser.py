import sys,csv

state = {}

if __name__ == "__main__":
    for i, arg in enumerate(sys.argv):
        if i != 0:
            print('Parsing', arg)
            with open(arg) as fp:
                line = fp.readline()
                while line:
                    line = line.replace('"', '')
                    spl = line.split(",")
                    ts = int(spl[0])
                    prefix = spl[1].strip()
                    path = spl[2].strip()
                    if state.get((prefix, path)):
                        state.get((prefix, path)).add(ts)
                    else:
                        state[(prefix, path)] = {ts}
                    line = fp.readline()
            print(len(state))
    
    print("Writing to disk")
    w = csv.writer(open("output.csv", "w"), quoting=csv.QUOTE_NONNUMERIC)
    for key, val in state.items():
        w.writerow([key[0], key[1], val])

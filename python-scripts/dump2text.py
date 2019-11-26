import sys, os

if __name__ == "__main__":
    for i, arg in enumerate(sys.argv):
        if i != 0:
            print('Parsing', arg)
            os.system('bgpdump -m -t change -O ' + arg + '.out ' + arg)
            os.system('rm ' + arg)
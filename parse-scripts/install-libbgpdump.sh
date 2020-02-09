wget http://ris.ripe.net/source/bgpdump/libbgpdump-1.6.0.tgz
tar xzf libbgpdump-1.6.0.tgz
cd libbgpdump-1.6.0
sh ./bootstrap.sh
make ./bgpdump -T
sudo cp bgpdump /bin/
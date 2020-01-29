FROM ubuntu
RUN apt update && apt upgrade -y
RUN apt install -y wget git build-essential apt-utils zlib1g-dev libbz2-dev curl dnsutils curl nano python-pip telnet
RUN wget https://raw.githubusercontent.com/alexchap/Albatros-Project/master/testing/tools/mdfmt/pybgpdump-read-only/pybgpdump.py
RUN wget http://ris.ripe.net/source/bgpdump/libbgpdump-1.6.0.tgz && tar xzf libbgpdump-1.6.0.tgz
RUN cd libbgpdump-1.6.0 && sh ./bootstrap.sh && make && ./bgpdump -T && cp bgpdump /bin/
CMD [ "bash" ]
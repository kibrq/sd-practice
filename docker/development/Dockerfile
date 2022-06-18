FROM ubuntu:focal

RUN apt update
RUN apt install -y wget

RUN wget -q https://download.jetbrains.com/idea/ideaIC-2022.1.1.tar.gz
RUN tar -xf ideaIC-2022.1.1.tar.gz
RUN rm ideaIC-2022.1.1.tar.gz

RUN apt install -y libxtst6 libxrender1 libxi6 libfreetype6 fontconfig
RUN apt update && apt install --fix-missing -y git build-essential libssl-dev automake make autoconf libncurses5-dev
COPY docker/scripts/install-erlang.sh /install-erlang.sh
RUN bash install-erlang.sh


ENTRYPOINT bash -c 'cd $(ls -d */ | grep "idea" --) && bin/idea.sh'

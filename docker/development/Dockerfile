FROM ubuntu:focal

RUN apt update && DEBIAN_FRONTEND=noninteractive apt install -y wget openjdk-17-jdk

RUN wget -q https://download.jetbrains.com/idea/ideaIC-2023.2.5.tar.gz
RUN tar -xf ideaIC-2023.2.5.tar.gz
RUN rm ideaIC-2023.2.5.tar.gz

RUN apt update && apt install -y libxtst6 libxrender1 libxi6 libfreetype6 fontconfig
RUN apt update && apt install --fix-missing -y git build-essential libssl-dev automake make autoconf libncurses5-dev

ENTRYPOINT bash -c 'cd $(ls -d */ | grep "idea" --) && bin/idea.sh'

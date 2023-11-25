XSOCK=/tmp/.X11-unix && docker run -it --name hwproj_development_1 -v $XSOCK:$XSOCK -e DISPLAY=$DISPLAY --net=hwproj_default -v $(pwd):/app hwproj_development:latest

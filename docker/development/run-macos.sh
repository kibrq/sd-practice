# First run in separate process:
# socat TCP-LISTEN:6000,reuseaddr,fork UNIX-CLIENT:\"$DISPLAY\"
docker run -it --name hwproj_development -e DISPLAY=docker.for.mac.host.internal:0 -v "$(pwd)":/app hwproj_development

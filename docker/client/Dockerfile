FROM node:18-alpine

WORKDIR /app

ENTRYPOINT /bin/sh -c 'npm install && npm run build && npx serve -s build'

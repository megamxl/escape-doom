FROM node

ARG FILE=app.js

COPY ${FILE} app.js

RUN ulimit -t 2

CMD ["timeout" ,"10", "node", "app.js"]
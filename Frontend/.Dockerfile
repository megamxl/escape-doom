FROM node:alpine3.16

WORKDIR /react-vite-app

EXPOSE 80

COPY package.json package-lock.json ./

RUN npm install --silent

COPY . ./

RUN npm run build

ENTRYPOINT ["npm", "run", "preview"]

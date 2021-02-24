FROM node:14.16

WORKDIR /usr/app
COPY package.json .
RUN npm install --quite
COPY . .
CMD ["npm", "start"]
FROM node:26.2.0-alpine AS BUILD

WORKDIR /app
COPY package*.json ./
RUN npm ci

COPY . .

RUN npm run build

FROM nginx:alpine

COPY --from=build /app/dist/two-factor-demo-FE/browser /usr/share/nginx/html

EXPOSE 80

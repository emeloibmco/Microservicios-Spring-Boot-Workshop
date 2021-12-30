
FROM node:12-alpine AS builder

WORKDIR /app
COPY . .
RUN npm install && npm run build


FROM node:12-alpine

RUN npm install -g serve

COPY --from=builder app/dist/cursos /app

EXPOSE 8080
ENTRYPOINT ["serve", "-l", "8080", "/app"]


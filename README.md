# Spring-JWT

Sample and simple implementation to extract and use jwt token from header in microservices architecture behind ARCHWAY Application gateway

## Build

```shell
mvn clean package
```

## Use springboot

### Configure env variables in env.properties

```properties
PORT=9292
JWT_PUBLIC_KEY_FILE=../jwt_public_key.pem
JWT_ALGORITHM=RSA256
```

### launch shell

```shell
mvn springboot:run
```

## Use docker

### build image

```shell
docker build -t ghcr.io/softwarity/spring-jwt:latest --build-arg GIT_TAG_REV=$(git rev-parse HEAD) .
```

### launch docker

```shell
docker run -p 9292:8080 -v /absolutepath/jwt_public_key.pem:/jwt_public_key.pem -e JWT_PUBLIC_KEY_FILE=/jwt_public_key.pem -e JWT_ALGORITHM=RSA256 ghcr.io/softwarity/spring-jwt:latest
```

### launch swarm

```shell
# create secret
docker secret create jwt_public_key ../jwt_public_key.pem
# deploy
docker stack deploy -c docker-compose.yml $STACKNAME
```

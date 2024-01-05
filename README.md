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
docker build -t ghcr.io/hhdevelopment:latest --build-arg GIT_TAG_REV=$(git rev-parse HEAD) .
```

### launch docker

```shell
docker run -p 9292:9292 -e JWT_PUBLIC_KEY_FILE=../jwt_public_key.pem -e JWT_ALGORITHM=RSA256 ghcr.io/hhdevelopment:latest
```

### launch swarm

```shell
docker stack deploy -c docker-compose.yml $STACKNAME
```

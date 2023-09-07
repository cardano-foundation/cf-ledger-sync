#!/bin/bash

APPLICATION_NAME=cf-ledger-sync

VERSION=$(git describe --tags --always)

PRIVATE_DOCKER_REGISTRY_URL="pro.registry.gitlab.metadata.dev.cf-deployments.org/base-infrastructure/docker-registry"

DOCKER_IMAGE="${PRIVATE_DOCKER_REGISTRY_URL}/${APPLICATION_NAME}:${VERSION}"

./gradlew application:bootJar -x test && \
  docker buildx use amd64 && \
  docker buildx build --platform linux/amd64 --load \
    --no-cache \
    --progress plain \
    -t "${DOCKER_IMAGE}" \
    .

docker push "${DOCKER_IMAGE}"

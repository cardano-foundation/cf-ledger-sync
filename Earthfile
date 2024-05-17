VERSION 0.8

ARG --global ALL_BUILD_TARGETS="ledger-sync aggregation streamer scheduler"

ARG --global DOCKER_IMAGE_PREFIX="cf-ledger-sync"
ARG --global DOCKER_IMAGES_EXTRA_TAGS=""
ARG --global DOCKER_REGISTRIES="hub.docker.com"
ARG --global HUB_DOCKER_COM_ORG=cardanofoundation

all:
  LOCALLY
  ARG RELEASE_TAG
  FOR image_target IN $ALL_BUILD_TARGETS
    BUILD +${image_target} --RELEASE_TAG=${RELEASE_TAG}
  END

docker-publish:
  ARG EARTHLY_GIT_SHORT_HASH
  ARG RELEASE_TAG
  WAIT
    BUILD +all --RELEASE_TAG=${RELEASE_TAG}
  END
  LOCALLY
  LET IMAGE_NAME = ""
  FOR registry IN $DOCKER_REGISTRIES
    FOR image_target IN $ALL_BUILD_TARGETS
      IF [ "$image_target" = "ledger-sync" ]
        SET IMAGE_NAME = ${DOCKER_IMAGE_PREFIX}
      ELSE
        SET IMAGE_NAME = ${DOCKER_IMAGE_PREFIX}-${image_target}
      END
      IF [ ! -z "$DOCKER_IMAGES_EXTRA_TAGS" ]
        FOR image_tag IN $DOCKER_IMAGES_EXTRA_TAGS
          IF [ "$registry" = "hub.docker.com" ]
            RUN docker tag ${IMAGE_NAME}:latest ${HUB_DOCKER_COM_ORG}/${IMAGE_NAME}:${image_tag}
            RUN echo docker push ${HUB_DOCKER_COM_ORG}/${IMAGE_NAME}:${image_tag}
          ELSE
            RUN docker tag ${IMAGE_NAME}:latest ${registry}/${IMAGE_NAME}:${image_tag}
            RUN echo docker push ${registry}/${IMAGE_NAME}:${image_tag}
          END
        END
      END
      IF [ "$registry" = "hub.docker.com" ]
        RUN docker tag ${IMAGE_NAME}:latest ${HUB_DOCKER_COM_ORG}/${IMAGE_NAME}:${EARTHLY_GIT_SHORT_HASH}
        RUN echo docker push ${HUB_DOCKER_COM_ORG}/${IMAGE_NAME}:${EARTHLY_GIT_SHORT_HASH}
      ELSE
        RUN docker tag ${IMAGE_NAME}:latest ${registry}/${IMAGE_NAME}:${EARTHLY_GIT_SHORT_HASH}
        RUN echo docker push ${registry}/${IMAGE_NAME}:${EARTHLY_GIT_SHORT_HASH}
      END
    END
  END

maven-central-publish:
  FROM DOCKERFILE -f Dockerfile --target build .
  RUN \
      --secret MAVEN_CENTRAL_GPG_PRIVATE_KEY \
      mkdir -p ~/.gradle && \
      echo "${MAVEN_CENTRAL_GPG_PRIVATE_KEY}" > ~/.gradle/secring.gpg.b64 && \
      base64 -d ~/.gradle/secring.gpg.b64 > ~/.gradle/secring.gpg
  RUN \
      --secret MAVEN_CENTRAL_GPG_KEY_ID \
      --secret MAVEN_CENTRAL_GPG_PASSPHRASE \
      --secret MAVEN_USERNAME \
      --secret MAVEN_PASSWORD \
      ./gradlew publish --warn --stacktrace \
      -Psigning.keyId=${MAVEN_CENTRAL_GPG_KEY_ID} \
      -Psigning.password=${MAVEN_CENTRAL_GPG_PASSPHRASE} \
      -Psigning.secretKeyRingFile=$(echo ~/.gradle/secring.gpg)

TEMPLATED_DOCKERFILE_BUILD:
  FUNCTION
  ARG DOCKERFILE_TARGET
  ARG DOCKER_IMAGE_NAME
  ARG RELEASE_TAG
  FROM DOCKERFILE -f Dockerfile --target ${DOCKERFILE_TARGET} .
  SAVE IMAGE ${DOCKER_IMAGE_NAME}:latest
  IF [ ! -z "$RELEASE_TAG" ]
    RUN mv /app/*jar /app/${DOCKERFILE_TARGET}-${RELEASE_TAG}.jar
    RUN md5sum /app/*jar > /app/${DOCKERFILE_TARGET}-${RELEASE_TAG}.jar.md5sum
  END
  SAVE ARTIFACT /app/* AS LOCAL build/

ledger-sync:
  ARG EARTHLY_TARGET_NAME
  ARG RELEASE_TAG
  DO +TEMPLATED_DOCKERFILE_BUILD \
    --DOCKERFILE_TARGET=${EARTHLY_TARGET_NAME} \
    --DOCKER_IMAGE_NAME=${DOCKER_IMAGE_PREFIX} \
    --RELEASE_TAG=${RELEASE_TAG}

aggregation:
  ARG EARTHLY_TARGET_NAME
  ARG RELEASE_TAG
  DO +TEMPLATED_DOCKERFILE_BUILD \
    --DOCKERFILE_TARGET=${EARTHLY_TARGET_NAME} \
    --DOCKER_IMAGE_NAME=${DOCKER_IMAGE_PREFIX}-${EARTHLY_TARGET_NAME} \
    --RELEASE_TAG=${RELEASE_TAG}

streamer:
  ARG EARTHLY_TARGET_NAME
  ARG RELEASE_TAG
  DO +TEMPLATED_DOCKERFILE_BUILD \
    --DOCKERFILE_TARGET=${EARTHLY_TARGET_NAME} \
    --DOCKER_IMAGE_NAME=${DOCKER_IMAGE_PREFIX}-${EARTHLY_TARGET_NAME} \
    --RELEASE_TAG=${RELEASE_TAG}

scheduler:
  ARG EARTHLY_TARGET_NAME
  ARG RELEASE_TAG
  DO +TEMPLATED_DOCKERFILE_BUILD \
    --DOCKERFILE_TARGET=${EARTHLY_TARGET_NAME} \
    --DOCKER_IMAGE_NAME=${DOCKER_IMAGE_PREFIX}-${EARTHLY_TARGET_NAME} \
    --RELEASE_TAG=${RELEASE_TAG}

docker-compose-up:
  LOCALLY
  WAIT
    BUILD +all
  END

  ARG network=preprod
  ARG background
  IF [ "${background}" = "true" ]
    ENV DOCKER_COMPOSE_UP_ARGS="-d"
  END
  IF [ "${network}" = "preview" ]
    ARG CARDANO_NODE_HOST=preview-node.world.dev.cardano.org
    ARG CARDANO_NODE_PORT=3001
    ARG CARDANO_PROTOCOL_MAGIC=2
  ELSE IF [ "${network}" = "preprod" ]
    ARG CARDANO_NODE_HOST=preprod-node.world.dev.cardano.org
    ARG CARDANO_NODE_PORT=3001
    ARG CARDANO_PROTOCOL_MAGIC=1
  ELSE IF [ "${network}" = "sanchonet" ]
    ARG CARDANO_NODE_HOST=sanchonet-node.play.dev.cardano.org
    ARG CARDANO_NODE_PORT=3001
    ARG CARDANO_PROTOCOL_MAGIC=4
  ELSE IF [ "${network}" = "mainnet" ]
    ARG CARDANO_NODE_HOST=backbone.cardano-mainnet.iohk.io
    ARG CARDANO_NODE_PORT=3001
    ARG CARDANO_PROTOCOL_MAGIC=764824073
  END

  ENV NETWORK=${network}
  ENV STORE_CARDANO_HOST=${CARDANO_NODE_HOST}
  ENV STORE_CARDANO_PORT=${CARDANO_NODE_PORT}
  ENV STORE_CARDANO_PROTOCOL_MAGIC=${CARDANO_PROTOCOL_MAGIC}
  ENV CF_LEDGER_SYNC_DOCKER_IMAGE=cf-ledger-sync:latest

  RUN docker compose up ${DOCKER_COMPOSE_UP_ARGS}

docker-compose-down:
  LOCALLY
  
  ARG cleanup
  IF [ "${cleanup}" = "true" ]
    ENV DOCKER_COMPOSE_DOWN_ARGS="-v"
  END

  RUN docker compose down ${DOCKER_COMPOSE_DOWN_ARGS}

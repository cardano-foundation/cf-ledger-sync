VERSION 0.8

IMPORT --allow-privileged github.com/cardano-foundation/cf-gha-workflows/./earthfiles/functions:main AS functions

ARG --global DOCKER_IMAGES_TARGETS="ledger-sync aggregation streamer scheduler"

ARG --global DOCKER_IMAGE_PREFIX="cf-ledger-sync"
ARG --global DOCKER_IMAGES_EXTRA_TAGS=""
ARG --global DOCKER_REGISTRIES=""
ARG --global RELEASE_TAG=""
ARG --global PUSH=false

ARG --global GRADLE_BUILD_ARGS="clean build -PskipSigning=true --stacktrace"

all:
  WAIT
    BUILD +gradle-build
  END
  LOCALLY
  FOR image_target IN $DOCKER_IMAGES_TARGETS
    BUILD +$image_target --PUSH=$PUSH
  END

docker-publish:
  BUILD +all --PUSH=$PUSH

TEMPLATED_DOCKERFILE_BUILD:
  FUNCTION
  ARG DOCKERFILE_TARGET
  ARG DOCKER_IMAGE_NAME
  FROM DOCKERFILE --build-arg GRADLE_BUILD_ARGS=${GRADLE_BUILD_ARGS} -f Dockerfile --target ${DOCKERFILE_TARGET} .
  SAVE IMAGE ${DOCKER_IMAGE_NAME}:latest


gradle-build:
  FROM DOCKERFILE \
    --build-arg GRADLE_BUILD_ARGS="${GRADLE_BUILD_ARGS}" \
    -f Dockerfile --target build .
  IF [ -z "$RELEASE_TAG" ]
    ARG RELEASE_TAG=$(grep version gradle.properties | awk '{print $NF}')
  END
  LET APP_JAR_PATH=""
  LET APP_JAR_NAME=""
  RUN mkdir -p /release
  FOR target IN $DOCKER_IMAGES_TARGETS
    IF [ $target = "ledger-sync" ]
      SET APP_JAR_PATH="/app/application/build/libs"
      SET APP_JAR_NAME=${DOCKER_IMAGE_PREFIX}-${RELEASE_TAG}.jar
    ELSE
      SET APP_JAR_PATH="/app/${target}-app/build/libs"
      SET APP_JAR_NAME=${DOCKER_IMAGE_PREFIX}-${target}-${RELEASE_TAG}.jar
    END
    RUN mv ${APP_JAR_PATH}/*jar /release/${APP_JAR_NAME}
    RUN md5sum /release/${APP_JAR_NAME} > /release/${APP_JAR_NAME}.md5sum
  END
  SAVE ARTIFACT /release/* AS LOCAL release/

ledger-sync:
  ARG EARTHLY_TARGET_NAME
  DO +TEMPLATED_DOCKERFILE_BUILD \
    --DOCKERFILE_TARGET=${EARTHLY_TARGET_NAME} \
    --DOCKER_IMAGE_NAME=${DOCKER_IMAGE_PREFIX}
  DO functions+DOCKER_TAG_N_PUSH \
     --PUSH=$PUSH \
     --DOCKER_IMAGE_NAME=${DOCKER_IMAGE_PREFIX} \
     --DOCKER_IMAGES_EXTRA_TAGS="${DOCKER_IMAGES_EXTRA_TAGS} ${RELEASE_TAG}"

aggregation:
  ARG EARTHLY_TARGET_NAME
  DO +TEMPLATED_DOCKERFILE_BUILD \
    --DOCKERFILE_TARGET=${EARTHLY_TARGET_NAME} \
    --DOCKER_IMAGE_NAME=${DOCKER_IMAGE_PREFIX}-${EARTHLY_TARGET_NAME}
  DO functions+DOCKER_TAG_N_PUSH \
    --PUSH=$PUSH \
    --DOCKER_IMAGE_NAME=${DOCKER_IMAGE_PREFIX}-${EARTHLY_TARGET_NAME} \
    --DOCKER_IMAGES_EXTRA_TAGS="${DOCKER_IMAGES_EXTRA_TAGS} ${RELEASE_TAG}"

streamer:
  ARG EARTHLY_TARGET_NAME
  DO +TEMPLATED_DOCKERFILE_BUILD \
    --DOCKERFILE_TARGET=${EARTHLY_TARGET_NAME} \
    --DOCKER_IMAGE_NAME=${DOCKER_IMAGE_PREFIX}-${EARTHLY_TARGET_NAME}
  DO functions+DOCKER_TAG_N_PUSH \
    --PUSH=$PUSH \
    --DOCKER_IMAGE_NAME=${DOCKER_IMAGE_PREFIX}-${EARTHLY_TARGET_NAME} \
    --DOCKER_IMAGES_EXTRA_TAGS="${DOCKER_IMAGES_EXTRA_TAGS} ${RELEASE_TAG}"

scheduler:
  ARG EARTHLY_TARGET_NAME
  DO +TEMPLATED_DOCKERFILE_BUILD \
    --DOCKERFILE_TARGET=${EARTHLY_TARGET_NAME} \
    --DOCKER_IMAGE_NAME=${DOCKER_IMAGE_PREFIX}-${EARTHLY_TARGET_NAME}
  DO functions+DOCKER_TAG_N_PUSH \
    --PUSH=$PUSH \
    --DOCKER_IMAGE_NAME=${DOCKER_IMAGE_PREFIX}-${EARTHLY_TARGET_NAME} \
    --DOCKER_IMAGES_EXTRA_TAGS="${DOCKER_IMAGES_EXTRA_TAGS} ${RELEASE_TAG}"

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

VERSION 0.8

ARG --global ALL_BUILD_TARGETS="ledger-sync aggregation streamer scheduler"

ARG --global DOCKER_IMAGE_PREFIX="cf-ledger-sync"
ARG --global DOCKER_IMAGES_EXTRA_TAGS=""
ARG --global DOCKER_REGISTRIES="hub.docker.com"
ARG --global HUB_DOCKER_COM_ORG=cardanofoundation

all:
  LOCALLY
  FOR image_target IN $ALL_BUILD_TARGETS
    BUILD +$image_target
  END

docker-publish:
  ARG EARTHLY_GIT_SHORT_HASH
  WAIT
    BUILD +all
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

ledger-sync:
  ARG EARTHLY_TARGET_NAME
  FROM DOCKERFILE -f Dockerfile --target ${EARTHLY_TARGET_NAME} .
  SAVE IMAGE ${DOCKER_IMAGE_PREFIX}:latest

aggregation:
  ARG EARTHLY_TARGET_NAME
  FROM DOCKERFILE -f Dockerfile --target $EARTHLY_TARGET_NAME .
  SAVE IMAGE ${DOCKER_IMAGE_PREFIX}-${EARTHLY_TARGET_NAME}:latest

streamer:
  ARG EARTHLY_TARGET_NAME
  FROM DOCKERFILE -f Dockerfile --target $EARTHLY_TARGET_NAME .
  SAVE IMAGE ${DOCKER_IMAGE_PREFIX}-${EARTHLY_TARGET_NAME}:latest

scheduler:
  ARG EARTHLY_TARGET_NAME
  FROM DOCKERFILE -f Dockerfile --target $EARTHLY_TARGET_NAME .
  SAVE IMAGE ${DOCKER_IMAGE_PREFIX}-${EARTHLY_TARGET_NAME}:latest

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

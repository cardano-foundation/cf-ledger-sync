# Prerequisites

* [docker]
* [earthly]

# Running local environment using docker-compose

There is no need to build the images first, as earthly targets include the build dependency, so you can just do this right the way:

* Bring the environment up:
```
earthly +docker-compose-up --network=preview
earthly +docker-compose-up --network=preprod
earthly +docker-compose-up --network=sanchonet
earthly +docker-compose-up --network=mainnet --background=true
```
You can add the `--background=true` argument to let docker-compose run in the background :)
* Bring the environment down:
```
# Either
earthly +docker-compose-down
# Or just...
docker-compose down [-v]
```
Optionally, you can also specify `--cleanup=true` which will cleanup the docker volumes (postgres data).

# Build

* Build all the available images:
```
earthly +all
```
* Build specific images:
```
earthly +ledger-sync
earthly +aggregation
earthly +streamer
earthly +scheduler
```

These commands will produce the corresponding local docker images such as:
```
cf-ledger-sync:latest
cf-ledger-sync-scheduler:latest
cf-ledger-sync-aggregation:latest
cf-ledger-sync-streamer:latest
```

[earhly]: https://docs.earthly.dev/install
[docker]: https://docs.docker.com/get-docker/

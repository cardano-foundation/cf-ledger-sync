# Changelog

## [0.3.15](https://github.com/cardano-foundation/cf-ledger-consumer/compare/v0.3.14...v0.3.15) (2023-08-11)


### Bug Fixes

* attempting to force a release ([8737740](https://github.com/cardano-foundation/cf-ledger-consumer/commit/873774010f0f0ecb608440a4a1664ba5b0ef83e3))
* attempting to force a release ([b7615f6](https://github.com/cardano-foundation/cf-ledger-consumer/commit/b7615f6a1a0397b08d292ce52d10c36f768c7618))

## [0.3.14](https://github.com/cardano-foundation/cf-ledger-consumer/compare/v0.3.13...v0.3.14) (2023-08-09)


### Bug Fixes

* unique account ([f5fe799](https://github.com/cardano-foundation/cf-ledger-consumer/commit/f5fe79961e07e9532c4200a85d6436e86cd396be))

## [0.3.13](https://github.com/cardano-foundation/cf-ledger-consumer/compare/v0.3.12...v0.3.13) (2023-08-04)


### Bug Fixes

* attempting to force a release ([bed072a](https://github.com/cardano-foundation/cf-ledger-consumer/commit/bed072aa17ff042dfbff2248339d7e7c0a8dd72d))

## [0.3.12](https://github.com/cardano-foundation/cf-ledger-consumer/compare/v0.3.11...v0.3.12) (2023-08-02)


### Bug Fixes

* introduced ssl management for client ([#252](https://github.com/cardano-foundation/cf-ledger-consumer/issues/252)) ([79dfd0f](https://github.com/cardano-foundation/cf-ledger-consumer/commit/79dfd0f1061019a4a658651227bd50a7033c6efc))

## [0.3.11](https://github.com/cardano-foundation/cf-ledger-consumer/compare/v0.3.10...v0.3.11) (2023-07-31)


### Features

* create missing index ([3dbea2a](https://github.com/cardano-foundation/cf-ledger-consumer/commit/3dbea2a9afc55df28074d3ece6c998cfc61e4ec2))
* create tx index ([2df74e5](https://github.com/cardano-foundation/cf-ledger-consumer/commit/2df74e5d0e58ddd94244dedd9b08aaae1236dcf1))

## [0.3.10](https://github.com/cardano-foundation/cf-ledger-consumer/compare/v0.3.9...v0.3.10) (2023-07-27)


### Bug Fixes

* Fix non-UTF8 asset name view migration script ([4852f3b](https://github.com/cardano-foundation/cf-ledger-consumer/commit/4852f3bf9e82ff4ebe57f0be195f9e0b2ea3ec7e))

## [0.3.9](https://github.com/cardano-foundation/cf-ledger-consumer/compare/v0.3.8...v0.3.9) (2023-07-26)


### Features

* **multi_asset:** Make non-UTF8 asset name view null ([f69b66f](https://github.com/cardano-foundation/cf-ledger-consumer/commit/f69b66f92f120aa50a89f58b3765044b7d46d55b))


### Bug Fixes

* **redeemer_data:** Use text instead of varchar to store large redeemer_data json ([d14009d](https://github.com/cardano-foundation/cf-ledger-consumer/commit/d14009d409a8caf0f04ef2726f09a2b6054420d8))

## [0.3.8](https://github.com/cardano-foundation/cf-ledger-consumer/compare/v0.3.7...v0.3.8) (2023-07-20)


### Bug Fixes

* **hotfix:** fix aws profile to match k8s templates ([1b6a71a](https://github.com/cardano-foundation/cf-ledger-consumer/commit/1b6a71a456f92d2cc71e87d4293e0f119468aaac))
* **hotfix:** fix aws profile to match k8s templates ([19695f0](https://github.com/cardano-foundation/cf-ledger-consumer/commit/19695f0e20b1f673980143ca3830f2e733d0b784))
* trying to fix kafka connection issue. ([836d7c0](https://github.com/cardano-foundation/cf-ledger-consumer/commit/836d7c094dd4810734e9112245c0ad8abc1ab159))

## [0.3.7](https://github.com/cardano-foundation/cf-ledger-consumer/compare/v0.3.6...v0.3.7) (2023-07-17)


### Features

* enabled SSL for redis cluster ([d7f9d9e](https://github.com/cardano-foundation/cf-ledger-consumer/commit/d7f9d9ee8580c4a8658964093778a0edee906fc8))

## [0.3.6](https://github.com/cardano-foundation/cf-ledger-consumer/compare/v0.3.5...v0.3.6) (2023-07-17)


### Features

* add script from tx_out ([f580ff8](https://github.com/cardano-foundation/cf-ledger-consumer/commit/f580ff8a532e8b353fa096e7787f27e542aaf4ff))
* datum insert flow ([4c73d89](https://github.com/cardano-foundation/cf-ledger-consumer/commit/4c73d897efca2eb45fea89a137a44005f60925b1))
* met-1144 add tx metadata hash ([17b49cb](https://github.com/cardano-foundation/cf-ledger-consumer/commit/17b49cb4f272f889bf1c4d12ffb3c59bac5f4e1b))
* met-1144 change version to 0.1.8-SNAPSHOT ([5af2946](https://github.com/cardano-foundation/cf-ledger-consumer/commit/5af29463c38a3e60f22604a9a51adcc991d692d1))
* met-1460 change datum logic ([f595b32](https://github.com/cardano-foundation/cf-ledger-consumer/commit/f595b32a66ee2617ee2be31cec71fac92e963ed2))
* met-1460 update common version ([b144035](https://github.com/cardano-foundation/cf-ledger-consumer/commit/b144035be9c5ffaf99e23312b684b4161f4de8dd))
* met-1460 update datum format ([76a437f](https://github.com/cardano-foundation/cf-ledger-consumer/commit/76a437f7b230169b0f01198c3be064cedbefc04f))
* met-1463 change script at least type to timelock ([b77ecf2](https://github.com/cardano-foundation/cf-ledger-consumer/commit/b77ecf26f893935587933b72ff7ed263793729c5))
* option for no caching data ([1a499df](https://github.com/cardano-foundation/cf-ledger-consumer/commit/1a499df1645ea4d02f362349a359ddbdc6da732b))
* update collateral script when tx failed ([75c87fd](https://github.com/cardano-foundation/cf-ledger-consumer/commit/75c87fd90e262496d97f8cb2cc2e3b0e70c56d29))
* update tx id of script ([ea5aa64](https://github.com/cardano-foundation/cf-ledger-consumer/commit/ea5aa6421a7f629743a421057725b8e52da3a408))


### Bug Fixes

* add script when tx failed ([3daa092](https://github.com/cardano-foundation/cf-ledger-consumer/commit/3daa0924cca981590e400c57dc94a0861cff4cbe))
* **redeemer:** Fix duplicated redeemer data handling ([ab9e707](https://github.com/cardano-foundation/cf-ledger-consumer/commit/ab9e707965f827ec380ccb23625d350e4e055fe9))
* **redis:** Add Redis template key serializer ([35cebdb](https://github.com/cardano-foundation/cf-ledger-consumer/commit/35cebdbadfa7f406be3705c35736e68e58c073af))
* **script:** Check if failed tx has collateral return before handling script ([533293a](https://github.com/cardano-foundation/cf-ledger-consumer/commit/533293acd0f19fa9a4a11015cf3a361b6f46858e))
* Use concurrent hash map for redeemers returning map ([a544ec7](https://github.com/cardano-foundation/cf-ledger-consumer/commit/a544ec7af1dc40a4908462ad735acf2e7f67f01d))


### Documentation

* update doc ([4aa7ab7](https://github.com/cardano-foundation/cf-ledger-consumer/commit/4aa7ab748ffca27430ec93e24f113a9bd097247a))

## [0.3.5](https://github.com/cardano-foundation/cf-ledger-consumer/compare/v0.3.4...v0.3.5) (2023-07-03)


### Features

* replace BOOSTRAP_SERVER_HOST/PORT with BOOTSTRAP_SERVERS ([f541c82](https://github.com/cardano-foundation/cf-ledger-consumer/commit/f541c82cbe1ffe09731c8ea51e8fc226f9fee460))

## [0.3.4](https://github.com/cardano-foundation/cf-ledger-consumer/compare/v0.3.3...v0.3.4) (2023-07-03)


### Bug Fixes

* get plutus script body byte ([36ffbd9](https://github.com/cardano-foundation/cf-ledger-consumer/commit/36ffbd92ec317acb8f64a398c7cd7cd00717aa9a))
* small changes trigger release ([749be3b](https://github.com/cardano-foundation/cf-ledger-consumer/commit/749be3bd8d006ae40e17023885bf5d3ebac65d3a))

## [0.3.3](https://github.com/cardano-foundation/cf-ledger-consumer/compare/v0.3.2...v0.3.3) (2023-06-29)


### Bug Fixes

* add junit5 plugin for pitest ([a1aab3d](https://github.com/cardano-foundation/cf-ledger-consumer/commit/a1aab3ddd52f857cd0530bcc1465e1f0c16e4400))
* repair pom and enable test report generation ([8649e0e](https://github.com/cardano-foundation/cf-ledger-consumer/commit/8649e0e5553b2e933612b7bc70b0e1bc57068df0))

## [0.3.2](https://github.com/cardano-foundation/cf-ledger-consumer/compare/v0.3.1...v0.3.2) (2023-06-29)


### Features

* add code of conduct and contributing information (MET-1380) ([a82a8d2](https://github.com/cardano-foundation/cf-ledger-consumer/commit/a82a8d22ed9275868d72eba627aaeed949e3ad85))


### Bug Fixes

* Fix slow epoch param handling ([21a9618](https://github.com/cardano-foundation/cf-ledger-consumer/commit/21a961802b0809ea2f6e488648be42af17490f8c))
* Fix wrong migrating table ([0ffe753](https://github.com/cardano-foundation/cf-ledger-consumer/commit/0ffe7537f7b1f83d0f5abe64a692dcec1c049422))
* **gha:** fixed PR builds ([159d8ba](https://github.com/cardano-foundation/cf-ledger-consumer/commit/159d8ba0bd59a7e7027d28718f49581e399cec3c))
* MET-1248 fix address tx balance stake address accounting ([ec5ae8c](https://github.com/cardano-foundation/cf-ledger-consumer/commit/ec5ae8c913a6f8938eaf244d5b71e279b3211c6b))
* MET-1248 fix EpochParamServiceImpl test ([a91db81](https://github.com/cardano-foundation/cf-ledger-consumer/commit/a91db81ec3571a2ea147b82373ff7bac2ee94cba))
* MET-1248 fix no StakeAddress entity session ([c5cb0d1](https://github.com/cardano-foundation/cf-ledger-consumer/commit/c5cb0d1c4992de7709150caad18b497e6b335fc4))
* MET-1248 introduce local prepared MaTxOut map and fix parallel query ([2813431](https://github.com/cardano-foundation/cf-ledger-consumer/commit/2813431a86d7b48f04efdc7a4b5fe5a88c998709))
* remove unused configuration class ([b990c33](https://github.com/cardano-foundation/cf-ledger-consumer/commit/b990c33ee4c9fb5218f0747ad06838933d9e91a2))

## [0.3.1](https://github.com/cardano-foundation/cf-ledger-consumer/compare/v0.3.0...v0.3.1) (2023-06-22)


### Bug Fixes

* **gha:** fix condition for main branch workflow trigger ([73d3180](https://github.com/cardano-foundation/cf-ledger-consumer/commit/73d3180d0dd484ae56c3c10c333885e19a0f0e24))
* **gha:** fix condition for main branch workflow trigger ([11db6b5](https://github.com/cardano-foundation/cf-ledger-consumer/commit/11db6b547ae0ce23d9a5d12bedafc3dd555bf4d3))

## [0.3.0](https://github.com/cardano-foundation/cf-ledger-consumer/compare/v0.2.16...v0.3.0) (2023-06-20)


### ⚠ BREAKING CHANGES

* met-1246 change insert genesis data flow

### Features

* met 160 custom metrics for consumer ([bf2a84e](https://github.com/cardano-foundation/cf-ledger-consumer/commit/bf2a84edd499c2d00995e24e5f7731305bda66f2))
* MET-1150 take stake address into account with address balance entities ([a6e954f](https://github.com/cardano-foundation/cf-ledger-consumer/commit/a6e954f0abac07588833cd498be1b898c9fa066b))
* met-1246 add retry ([c2b06fc](https://github.com/cardano-foundation/cf-ledger-consumer/commit/c2b06fc93ed3b19a01b17eb66ebef7bebeabcd68))
* met-1246 change insert genesis data flow ([532ed8a](https://github.com/cardano-foundation/cf-ledger-consumer/commit/532ed8a741f650aeed55a718214bd56526f8bdc9))
* met-1246 restore read from file ([dabda2b](https://github.com/cardano-foundation/cf-ledger-consumer/commit/dabda2b5eee758f42c258af66f93060fa001b5cb))


### Bug Fixes

* met-1239 fill missing columns in table protocol param ([c62d959](https://github.com/cardano-foundation/cf-ledger-consumer/commit/c62d959e32dde2a83406419ad53591178a4d49a7))
* met-1246 add missing docker file ([073e529](https://github.com/cardano-foundation/cf-ledger-consumer/commit/073e52917d21574b2da7f6fb760f6a19daaadfdb))
* **test:** MET-1150 fix random ID generator for AddressBalanceServiceImplTest ([8281925](https://github.com/cardano-foundation/cf-ledger-consumer/commit/8281925266f3d4c91fc24f06b2f52294e75f9542))


### Documentation

* add environments value ([105bd39](https://github.com/cardano-foundation/cf-ledger-consumer/commit/105bd3942283bb6788b0efa6227dd2f34670d870))
* met-1246 add functions documents ([e8ce45d](https://github.com/cardano-foundation/cf-ledger-consumer/commit/e8ce45d765ce6b82f1d38aa9f19aa5985331a425))
* met-1246 preview network ([bd94e33](https://github.com/cardano-foundation/cf-ledger-consumer/commit/bd94e33089a6e1830e1ea35e50b755a08924f1c5))

## [0.2.16](https://github.com/cardano-foundation/cf-ledger-consumer/compare/v0.2.15...v0.2.16) (2023-06-06)


### Features

* [MET-753] add verified_contract column to address table ([84441d8](https://github.com/cardano-foundation/cf-ledger-consumer/commit/84441d866d2acaa38ae90337e37457a90d942451))
* MET-691 add name_view column in MultiAsset entity ([add0c51](https://github.com/cardano-foundation/cf-ledger-consumer/commit/add0c510f0d742d3bac0f72f86d1b01a3543cd16))

## [0.2.15](https://github.com/cardano-foundation/cf-ledger-consumer/compare/v0.2.14...v0.2.15) (2023-06-01)


### Bug Fixes

* Fix tx out preparation when no aggregated tx out supplied ([b80ec4a](https://github.com/cardano-foundation/cf-ledger-consumer/commit/b80ec4acfeb201ac4862c45e4975963ce61c3c22))
* Remove not needed tx_chart SQL migration scripts ([878035d](https://github.com/cardano-foundation/cf-ledger-consumer/commit/878035d2aaa90e2946878863dd45c0402b79ffb5))

## [0.2.14](https://github.com/cardano-foundation/cf-ledger-consumer/compare/v0.2.13...v0.2.14) (2023-05-31)


### Features

* met-236 add new optimize query index ([f17b7f6](https://github.com/cardano-foundation/cf-ledger-consumer/commit/f17b7f67a69653f0e4d5be902d66364e1acdccbb))
* met-236 merge all sql file to one ([dba60dc](https://github.com/cardano-foundation/cf-ledger-consumer/commit/dba60dcdbae157a3bcd267966e0ac1809d777289))
* MET-835 implement cached layer for certificate sync services ([09e1714](https://github.com/cardano-foundation/cf-ledger-consumer/commit/09e17140df8945a1ce420302a79d0ba1a8406f4e))
* MET-835 introduce local entity variables, move away from cached repository ([3856271](https://github.com/cardano-foundation/cf-ledger-consumer/commit/3856271d04f3c33421a1dd9ff9def9c829bd6cf0))
* MET-835 remove cached entity repositories ([b00912e](https://github.com/cardano-foundation/cf-ledger-consumer/commit/b00912e22b603eafaa31be88c8f03e4ceae13980))
* MET-835 split several queries to its own transaction to boost query speed ([613d5af](https://github.com/cardano-foundation/cf-ledger-consumer/commit/613d5afac3d014e4de1d082a2a9239401d980a4f))
* **tx_chart:** MET-689 implement tx chart aggregation service ([10c2eaf](https://github.com/cardano-foundation/cf-ledger-consumer/commit/10c2eafba44c96831bbdada0b267fd93035a6dcf))


### Bug Fixes

* EpochServiceImplTest test cases ([914315f](https://github.com/cardano-foundation/cf-ledger-consumer/commit/914315f9cf4471a50f3304ba41a4bf4af82f0fd4))
* fixed default value for validate-on-migrate flyway flag ([58597f7](https://github.com/cardano-foundation/cf-ledger-consumer/commit/58597f73f52587b3abaea970619a5877ccc43ad1))
* fixed default value for validate-on-migrate flyway flag ([8d3d9d3](https://github.com/cardano-foundation/cf-ledger-consumer/commit/8d3d9d31293c93b012fd255d3b0e69dcd336ec19))
* met-236 add dev env ([1f59c35](https://github.com/cardano-foundation/cf-ledger-consumer/commit/1f59c359a0e0d648d05029c410e2286475ff0f9d))
* met-236 add tx_chart and report ([8f0a60d](https://github.com/cardano-foundation/cf-ledger-consumer/commit/8f0a60d58d313baf9f39342c8c847aecd3fbc51e))
* met-236 add tx_chart and report ([8ff4f96](https://github.com/cardano-foundation/cf-ledger-consumer/commit/8ff4f96a7d4df48be9f956c4850d55e37a6f2403))
* met-236-final-base-db-sync ([a5beec0](https://github.com/cardano-foundation/cf-ledger-consumer/commit/a5beec0ed5f547030f64fefa8a4b07ac85590be0))
* met-236-final-base-db-sync-v1 ([c4c0cc2](https://github.com/cardano-foundation/cf-ledger-consumer/commit/c4c0cc2242b8aed074904c43fb126ff3bf065bf7))
* **test:** MET-835 fix DatumServiceImpl test ([5671727](https://github.com/cardano-foundation/cf-ledger-consumer/commit/567172746b6131473136d2ef253f119b3b58c9b5))

## [0.2.13](https://github.com/cardano-foundation/cf-ledger-consumer/compare/v0.2.12...v0.2.13) (2023-05-18)


### Features

* **flyway:** added env var to disable schema migration hashes validation ([382ac08](https://github.com/cardano-foundation/cf-ledger-consumer/commit/382ac082e2edd2640f1f3118bf70a1a458114975))
* MET-1107 add more rollback logs ([e6bf6cd](https://github.com/cardano-foundation/cf-ledger-consumer/commit/e6bf6cdd5563b8083b011a8242d7c07acfa6a0c8))
* MET-916 update rollback flow ([ab321e0](https://github.com/cardano-foundation/cf-ledger-consumer/commit/ab321e094409b4476840651c9972992c4e0a97fc))


### Bug Fixes

* kafka ack block message  when skip it ([12ba145](https://github.com/cardano-foundation/cf-ledger-consumer/commit/12ba145c4c0392435893c2949fe5ceb58aa81ea6))
* MET-1090 handle pool relay fields based on relay type ([512dc7a](https://github.com/cardano-foundation/cf-ledger-consumer/commit/512dc7a2c8875607cbeceae210f8826dc3988469))
* MET-1107 fix rollback log separator ([9eb2f7c](https://github.com/cardano-foundation/cf-ledger-consumer/commit/9eb2f7c9d45baf2f4829d162251a2922d505e4aa))

## [0.2.12](https://github.com/cardano-foundation/cf-ledger-consumer/compare/v0.2.11...v0.2.12) (2023-05-14)


### Bug Fixes

* **docker:** added genesis file as external asset ([09603f1](https://github.com/cardano-foundation/cf-ledger-consumer/commit/09603f17a946326ad37c7199be45447ba51cf128))
* **docker:** added genesis file as external asset ([b39a9bc](https://github.com/cardano-foundation/cf-ledger-consumer/commit/b39a9bc3b141fefe8645fe839d93e536cc5bf54c))
* met-237 unit-test, genesis service ([12f6e25](https://github.com/cardano-foundation/cf-ledger-consumer/commit/12f6e25436ecf3c92a7cdbfbc170f331781e9edc))

## [0.2.11](https://github.com/cardano-foundation/cf-ledger-consumer/compare/v0.2.10...v0.2.11) (2023-05-12)


### Features

* add base integration test, test data cover 80% source ([ef3b555](https://github.com/cardano-foundation/cf-ledger-consumer/commit/ef3b555576bea587c25f397555128cd26be37819))
* add genesis folder to root path ([4e5aea1](https://github.com/cardano-foundation/cf-ledger-consumer/commit/4e5aea1ff5ca9f7d1377c42568d08c0c8b3043d0))
* add local enviroment ([1266e88](https://github.com/cardano-foundation/cf-ledger-consumer/commit/1266e88e0eb661185f7a5820964af1ed3e744033))
* add migration pool_name column in pool offline data ([80afe81](https://github.com/cardano-foundation/cf-ledger-consumer/commit/80afe81dc9686f68926690d651473a70ca1d5928))
* add release, test and deployment pipeline ([a1b59c9](https://github.com/cardano-foundation/cf-ledger-consumer/commit/a1b59c93877dcb18c1ed61bcde2db9b13a4d0a14))
* **address_balance:** MET-693 implement address token balance calculation ([a8324de](https://github.com/cardano-foundation/cf-ledger-consumer/commit/a8324deec87aea7e2d0e88f6aaae2af95da67d54))
* change lovelace from BigDecimal to BigInteger ([4b993fc](https://github.com/cardano-foundation/cf-ledger-consumer/commit/4b993fce37695b61171b571f1876780e1ea618e5))
* Map Address entity to AddressToken/AddressTxBalance ([06c75fe](https://github.com/cardano-foundation/cf-ledger-consumer/commit/06c75fe75703c4bf86f16e036cc475512f4cbe6f))
* met-237 move genesis data to external json file, change common, java version ([7984805](https://github.com/cardano-foundation/cf-ledger-consumer/commit/7984805362e7828a8dba1380ed59b55086c7de8d))
* met-237 move genesis data to external json file, change common, java version ([dd23a8f](https://github.com/cardano-foundation/cf-ledger-consumer/commit/dd23a8ffb925bcb831543c10da20dec421ae8f58))
* met-237-remove-data-insert-sql ([54afd46](https://github.com/cardano-foundation/cf-ledger-consumer/commit/54afd46dc1ee583cf39359ca224ced19503d94a7))
* met-237-remove-data-insert-sql ([906cf22](https://github.com/cardano-foundation/cf-ledger-consumer/commit/906cf22e49171390b943f3e02f85ca58979dc77c))
* met-445-add-reward-distributed ([26c149d](https://github.com/cardano-foundation/cf-ledger-consumer/commit/26c149dcc6555bbe7b24c64bd1c3e6c58699d2a0))
* met-626 fill table epoch_param ([5c5c3c6](https://github.com/cardano-foundation/cf-ledger-consumer/commit/5c5c3c652307fe778d3c817424cca47fcf48645f))
* met-634-update-table ([73649ce](https://github.com/cardano-foundation/cf-ledger-consumer/commit/73649ce25f8cd281422936d39a5cb4da3947daf8))
* met-645-add-integration-test-and-unit-tests ([0de5d98](https://github.com/cardano-foundation/cf-ledger-consumer/commit/0de5d989f2f68ac307a0922fbb13ae0f225818e6))
* met-917-remove-foreign-key ([8600d9b](https://github.com/cardano-foundation/cf-ledger-consumer/commit/8600d9bdb0882d04716b1e9176f995e39077e73c))
* MET-918 adapt re-designed rollback history table ([c0f0ced](https://github.com/cardano-foundation/cf-ledger-consumer/commit/c0f0cedc5db7e959e21b1b2f0dcf00672e39f705))
* migration: Add address_token/address_tx_balance fk migration ([6118a52](https://github.com/cardano-foundation/cf-ledger-consumer/commit/6118a52cc9f88c6c0166eb5dbeb92871148a7e5c))
* migration: Remove has_used field from tx_out ([112c670](https://github.com/cardano-foundation/cf-ledger-consumer/commit/112c67000db3febe753dfd75547af148d5cce9a3))
* **multi_asset:** MET-692 calculate total volume ([84b8d6b](https://github.com/cardano-foundation/cf-ledger-consumer/commit/84b8d6b5d75b56d59069da74796087ffe2347c3a))
* Refactored package structure ([8b11400](https://github.com/cardano-foundation/cf-ledger-consumer/commit/8b11400127038529dc39fc78de148aeb3e2eace9))
* set address_id column as fk, remove address column ([8c5592f](https://github.com/cardano-foundation/cf-ledger-consumer/commit/8c5592ff752f6c92233555ec7406debd1dbe5d1e))
* **stake_address:** MET-694 calculate stake address balance and available rewards ([89f50ec](https://github.com/cardano-foundation/cf-ledger-consumer/commit/89f50ec5678f954a7321e2ffd9a82996eaf40b26))


### Bug Fixes

* add transaction when insert ([b9e15c3](https://github.com/cardano-foundation/cf-ledger-consumer/commit/b9e15c335667e3f4c6f6072c33e02352ecc27917))
* adjust version of ledgersync common repo ([0a1b649](https://github.com/cardano-foundation/cf-ledger-consumer/commit/0a1b649107dadb18e8c2e66cbb7759cda9771b53))
* extra entropy from string list ([9c6a564](https://github.com/cardano-foundation/cf-ledger-consumer/commit/9c6a5645d96a8d931c299ebe60fc3b2bc631f623))
* fix logic and fly_way init file ([d12be8b](https://github.com/cardano-foundation/cf-ledger-consumer/commit/d12be8b72c4821f86a9859915f2a291126dd8a9a))
* fix logic and fly_way init file ([96fc66d](https://github.com/cardano-foundation/cf-ledger-consumer/commit/96fc66d2a6db1fab2a533eb4d80b467f60335d18))
* json mapper of handle auxiliary ([cb45028](https://github.com/cardano-foundation/cf-ledger-consumer/commit/cb45028f780f3eac84a3ad5b9c58e81ba81ad1fc))
* MET-1003 bump Hibernate version to 6.2.0-Final ([3f15a2c](https://github.com/cardano-foundation/cf-ledger-consumer/commit/3f15a2cbffc60a40610fc7f44cf5a8dd8e720359))
* MET-1003 scan all entities within CF packages ([00a5a56](https://github.com/cardano-foundation/cf-ledger-consumer/commit/00a5a56a52de37dca0df4d7b88e65d29ee85497e))
* MET-1014 annotate @Param for named custom queries ([81391a0](https://github.com/cardano-foundation/cf-ledger-consumer/commit/81391a0b618cf9f7c766fd39dfc8f16bfd95e9c9))
* MET-918 rename rollback history table migration schema ([fbae6ad](https://github.com/cardano-foundation/cf-ledger-consumer/commit/fbae6ad22f9e2d850f37c728a79cd933cd5a1c32))
* remove container name ([e2f07b0](https://github.com/cardano-foundation/cf-ledger-consumer/commit/e2f07b00d45842497754c9f8c36db28c6a25d0ce))
* remove hidden merge conflicts ([8c32683](https://github.com/cardano-foundation/cf-ledger-consumer/commit/8c3268341a2119711e234f3da439ad3fb9ee7f62))
* TxMetaData fix not insert ByteNull ([8cd91dd](https://github.com/cardano-foundation/cf-ledger-consumer/commit/8cd91dd429347d9efdf109942ee67cbf19bec094))
* TxMetaData fix not insert ByteNull ([aeec491](https://github.com/cardano-foundation/cf-ledger-consumer/commit/aeec4912a3ac869805d883607fb88dcb8da94056))
* TxMetadata key duplicate bug in one Tx. ParamProposal add pool deposit ([19591fe](https://github.com/cardano-foundation/cf-ledger-consumer/commit/19591fec3b4e9bed3a43c6827fac9288bcfeddb6))
* TxMetadata key duplicate bug. ([bf6dcc9](https://github.com/cardano-foundation/cf-ledger-consumer/commit/bf6dcc99c7c83a24ac6ddcf706f4cd17d7a88236))
* undo changes in V_1_1_0 and V1_1_2 and introduce a new migration for it ([a813f19](https://github.com/cardano-foundation/cf-ledger-consumer/commit/a813f1978b1a9d09720f084853f7e23292cc495f))
* update common package version ([fbc08dc](https://github.com/cardano-foundation/cf-ledger-consumer/commit/fbc08dc0eff0dcb1106c56d91ef484f8b988e008))
* Update java version to 11 as spring boot 3.x needs Java 17 ([47167cd](https://github.com/cardano-foundation/cf-ledger-consumer/commit/47167cd8d4804b2142f5f053c13e63e75da4e38b))
* update pom.xml ([010022d](https://github.com/cardano-foundation/cf-ledger-consumer/commit/010022d1f8399952afe812a8d5e3d6de5d68e193))
* update README.md and trigger release ([ab44a59](https://github.com/cardano-foundation/cf-ledger-consumer/commit/ab44a596c827c7d6a5ae22ca6bdd8d0278bb55d9))
* use latest explorer-consumer-common version ([131d15b](https://github.com/cardano-foundation/cf-ledger-consumer/commit/131d15b1ef50761f3166972ce1b4dfcd38eadb91))

## [0.2.10](https://github.com/cardano-foundation/cf-ledger-consumer/compare/v0.2.9...v0.2.10) (2023-05-11)


### Features

* met-237 move genesis data to external json file, change common, java version ([7984805](https://github.com/cardano-foundation/cf-ledger-consumer/commit/7984805362e7828a8dba1380ed59b55086c7de8d))
* met-237-remove-data-insert-sql ([54afd46](https://github.com/cardano-foundation/cf-ledger-consumer/commit/54afd46dc1ee583cf39359ca224ced19503d94a7))


### Bug Fixes

* remove hidden merge conflicts ([8c32683](https://github.com/cardano-foundation/cf-ledger-consumer/commit/8c3268341a2119711e234f3da439ad3fb9ee7f62))
* update common package version ([fbc08dc](https://github.com/cardano-foundation/cf-ledger-consumer/commit/fbc08dc0eff0dcb1106c56d91ef484f8b988e008))
* update pom.xml ([010022d](https://github.com/cardano-foundation/cf-ledger-consumer/commit/010022d1f8399952afe812a8d5e3d6de5d68e193))

## [0.2.9](https://github.com/cardano-foundation/cf-ledger-consumer/compare/v0.2.8...v0.2.9) (2023-05-10)


### Bug Fixes

* update README.md and trigger release ([ab44a59](https://github.com/cardano-foundation/cf-ledger-consumer/commit/ab44a596c827c7d6a5ae22ca6bdd8d0278bb55d9))

## [0.2.8](https://github.com/cardano-foundation/cf-ledger-consumer/compare/v0.2.7...v0.2.8) (2023-05-10)


### Features

* met-445-add-reward-distributed ([26c149d](https://github.com/cardano-foundation/cf-ledger-consumer/commit/26c149dcc6555bbe7b24c64bd1c3e6c58699d2a0))

## [0.2.7](https://github.com/cardano-foundation/cf-ledger-consumer/compare/v0.2.6...v0.2.7) (2023-05-08)


### Bug Fixes

* MET-1003 bump Hibernate version to 6.2.0-Final ([3f15a2c](https://github.com/cardano-foundation/cf-ledger-consumer/commit/3f15a2cbffc60a40610fc7f44cf5a8dd8e720359))
* MET-1003 scan all entities within CF packages ([00a5a56](https://github.com/cardano-foundation/cf-ledger-consumer/commit/00a5a56a52de37dca0df4d7b88e65d29ee85497e))
* MET-1014 annotate @Param for named custom queries ([81391a0](https://github.com/cardano-foundation/cf-ledger-consumer/commit/81391a0b618cf9f7c766fd39dfc8f16bfd95e9c9))

## [0.2.6](https://github.com/cardano-foundation/cf-ledger-consumer/compare/v0.2.5...v0.2.6) (2023-05-05)


### Features

* add migration pool_name column in pool offline data ([80afe81](https://github.com/cardano-foundation/cf-ledger-consumer/commit/80afe81dc9686f68926690d651473a70ca1d5928))
* add release, test and deployment pipeline ([a1b59c9](https://github.com/cardano-foundation/cf-ledger-consumer/commit/a1b59c93877dcb18c1ed61bcde2db9b13a4d0a14))
* met-634-update-table ([73649ce](https://github.com/cardano-foundation/cf-ledger-consumer/commit/73649ce25f8cd281422936d39a5cb4da3947daf8))
* Refactored package structure ([8b11400](https://github.com/cardano-foundation/cf-ledger-consumer/commit/8b11400127038529dc39fc78de148aeb3e2eace9))


### Bug Fixes

* adjust version of ledgersync common repo ([0a1b649](https://github.com/cardano-foundation/cf-ledger-consumer/commit/0a1b649107dadb18e8c2e66cbb7759cda9771b53))
* Update java version to 11 as spring boot 3.x needs Java 17 ([47167cd](https://github.com/cardano-foundation/cf-ledger-consumer/commit/47167cd8d4804b2142f5f053c13e63e75da4e38b))

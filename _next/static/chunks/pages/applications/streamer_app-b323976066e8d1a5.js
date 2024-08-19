(self.webpackChunk_N_E=self.webpackChunk_N_E||[]).push([[376],{1906:function(e,n,a){(window.__NEXT_P=window.__NEXT_P||[]).push(["/applications/streamer_app",function(){return a(5929)}])},5929:function(e,n,a){"use strict";a.r(n),a.d(n,{__toc:function(){return l}});var t=a(5893),s=a(2673),r=a(2169),i=a(2069);a(9488);var o=a(2643);let l=[{depth:3,value:"Pre-requisites",id:"pre-requisites"},{depth:3,value:"Events",id:"events"},{depth:3,value:"Channels",id:"channels"},{depth:2,value:"How to run (Jar file)",id:"how-to-run-jar-file"},{depth:2,value:"How to run (Docker)",id:"how-to-run-docker"},{depth:3,value:"To start sync from a specific points",id:"to-start-sync-from-a-specific-points"},{depth:3,value:"Kafka Configuration",id:"kafka-configuration"}];function c(e){let n=Object.assign({h1:"h1",p:"p",a:"a",h3:"h3",ol:"ol",li:"li",ul:"ul",code:"code",strong:"strong",pre:"pre",span:"span",h2:"h2"},(0,o.a)(),e.components);return(0,t.jsxs)(t.Fragment,{children:[(0,t.jsx)(n.h1,{children:"Ledger Sync Streamer"}),"\n",(0,t.jsx)(n.p,{children:"Ledger Sync Streamer app reads data from a Cardano node and publishes blockchain data to a messaging middleware like Kafka or RabbitMQ.\nIt publishes blockchain data in the form of events. There are various types of events that can be published by the streamer app, but you can configure which events you want to publish."}),"\n",(0,t.jsxs)(n.p,{children:["As this app uses Spring Cloud Stream, it can support other supported binders in Spring Cloud Stream with some minor changes.\n(",(0,t.jsx)(n.a,{href:"https://spring.io/projects/spring-cloud-stream",children:"https://spring.io/projects/spring-cloud-stream"}),")"]}),"\n",(0,t.jsx)(n.h3,{id:"pre-requisites",children:"Pre-requisites"}),"\n",(0,t.jsx)(n.p,{children:"To run the streamer app, you need to have following components:"}),"\n",(0,t.jsxs)(n.ol,{children:["\n",(0,t.jsx)(n.li,{children:"Cardano Node or connect to a remote Cardano node"}),"\n",(0,t.jsx)(n.li,{children:"Database (PostgreSQL, MySQL, H2) : To store cursor/checkpoint data"}),"\n",(0,t.jsx)(n.li,{children:"Messaging middleware (Kafka, RabbitMQ) : To publish events"}),"\n"]}),"\n",(0,t.jsx)(n.h3,{id:"events",children:"Events"}),"\n",(0,t.jsx)(n.p,{children:"Events are published as JSON messages, and each event contains two fields:"}),"\n",(0,t.jsxs)(n.ul,{children:["\n",(0,t.jsx)(n.li,{children:"metadata: Contains metadata about the event, such as block number, slot number, era, etc."}),"\n",(0,t.jsxs)(n.li,{children:[(0,t.jsx)(n.code,{children:"<payload>"}),": Contains the actual data of the event."]}),"\n"]}),"\n",(0,t.jsx)(n.p,{children:"The supported events are:"}),"\n",(0,t.jsxs)(n.ul,{children:["\n",(0,t.jsxs)(n.li,{children:[(0,t.jsx)(n.strong,{children:"blockEvent"})," - Shelley and Post Shelley Blocks data (Includes everything transactions, witnesses..)"]}),"\n",(0,t.jsxs)(n.li,{children:[(0,t.jsx)(n.strong,{children:"rollbackEvent"})," - Rollback event with rollback point"]}),"\n",(0,t.jsxs)(n.li,{children:[(0,t.jsx)(n.strong,{children:"byronMainBlockEvent"})," - Byron Main Block data"]}),"\n",(0,t.jsxs)(n.li,{children:[(0,t.jsx)(n.strong,{children:"byronEbBlockEvent"})," - Byron Epoch Boundary Block data"]}),"\n",(0,t.jsxs)(n.li,{children:[(0,t.jsx)(n.strong,{children:"blockHeaderEvent"})," - Shelley and Post Shelley Block Header data"]}),"\n",(0,t.jsxs)(n.li,{children:[(0,t.jsx)(n.strong,{children:"genesisBlockEvent"})," - Genesis Block data"]}),"\n",(0,t.jsxs)(n.li,{children:[(0,t.jsx)(n.strong,{children:"certificateEvent"})," - Certificate data in a block"]}),"\n",(0,t.jsxs)(n.li,{children:[(0,t.jsx)(n.strong,{children:"transactionEvent"})," - Transaction data. One transactionEvent with all transactions in a block"]}),"\n",(0,t.jsxs)(n.li,{children:[(0,t.jsx)(n.strong,{children:"auxDataEvent"})," - Auxiliary data in a block"]}),"\n",(0,t.jsxs)(n.li,{children:[(0,t.jsx)(n.strong,{children:"mintBurnEvent"})," - Mint and Burn data in a block"]}),"\n",(0,t.jsxs)(n.li,{children:[(0,t.jsx)(n.strong,{children:"scriptEvent"})," - Script data of all transactions in a block"]}),"\n"]}),"\n",(0,t.jsx)(n.h3,{id:"channels",children:"Channels"}),"\n",(0,t.jsx)(n.p,{children:"By default, the events are published to default channels, but you also have the flexibility to\nconfigure custom channels as needed."}),"\n",(0,t.jsxs)(n.ul,{children:["\n",(0,t.jsxs)(n.li,{children:[(0,t.jsx)(n.strong,{children:"blockEvent"})," - blockTopic"]}),"\n",(0,t.jsxs)(n.li,{children:[(0,t.jsx)(n.strong,{children:"rollbackEvent"})," - rollbackTopic"]}),"\n",(0,t.jsxs)(n.li,{children:[(0,t.jsx)(n.strong,{children:"byronMainBlockEvent"})," - byronMainBlockTopic"]}),"\n",(0,t.jsxs)(n.li,{children:[(0,t.jsx)(n.strong,{children:"byronEbBlockEvent"})," - byronEbBlockTopic"]}),"\n",(0,t.jsxs)(n.li,{children:[(0,t.jsx)(n.strong,{children:"blockHeaderEvent"})," - blockHeaderTopic"]}),"\n",(0,t.jsxs)(n.li,{children:[(0,t.jsx)(n.strong,{children:"genesisBlockEvent"})," - genesisBlockTopic"]}),"\n",(0,t.jsxs)(n.li,{children:[(0,t.jsx)(n.strong,{children:"certificateEvent"})," - certificateTopic"]}),"\n",(0,t.jsxs)(n.li,{children:[(0,t.jsx)(n.strong,{children:"transactionEvent"})," - transactionTopic"]}),"\n",(0,t.jsxs)(n.li,{children:[(0,t.jsx)(n.strong,{children:"auxDataEvent"})," - auxDataTopic"]}),"\n",(0,t.jsxs)(n.li,{children:[(0,t.jsx)(n.strong,{children:"mintBurnEvent"})," - mintBurnTopic"]}),"\n",(0,t.jsxs)(n.li,{children:[(0,t.jsx)(n.strong,{children:"scriptEvent"})," - scriptTopic"]}),"\n"]}),"\n",(0,t.jsx)(n.p,{children:"The default channel can be changed by changing the following properties in application.properties file or Docker environment file."}),"\n",(0,t.jsx)(n.pre,{"data-language":"text","data-theme":"default",children:(0,t.jsx)(n.code,{"data-language":"text","data-theme":"default",children:(0,t.jsx)(n.span,{className:"line",children:(0,t.jsx)(n.span,{style:{color:"var(--shiki-color-text)"},children:"spring.cloud.stream.bindings.<event_name>-out-0.destination=<topic_name>"})})})}),"\n",(0,t.jsx)(n.p,{children:"For more details on different configuration options, please refer to the application-kafka.properties or application-rabbit.properties file in config folder."}),"\n",(0,t.jsx)(n.h2,{id:"how-to-run-jar-file",children:"How to run (Jar file)"}),"\n",(0,t.jsx)(n.p,{children:"To run the streamer app, you need Java 21 or a newer version.\nAfter building the project, you can execute the JAR file using the following commands:"}),"\n",(0,t.jsxs)(n.p,{children:["To run with ",(0,t.jsx)(n.strong,{children:"Kafka"})," binder:"]}),"\n",(0,t.jsxs)(n.ol,{children:["\n",(0,t.jsx)(n.li,{children:"Go to top level directory of the project."}),"\n",(0,t.jsxs)(n.li,{children:["Edit the ",(0,t.jsx)(n.code,{children:"config/application-kafka.properties"})," file and provide required information for Cardano node, datasource, Kafka."]}),"\n",(0,t.jsxs)(n.li,{children:["Run the streamer jar file with ",(0,t.jsx)(n.code,{children:"kafka"})," profile."]}),"\n"]}),"\n",(0,t.jsx)(n.pre,{"data-language":"text","data-theme":"default",children:(0,t.jsx)(n.code,{"data-language":"text","data-theme":"default",children:(0,t.jsx)(n.span,{className:"line",children:(0,t.jsx)(n.span,{style:{color:"var(--shiki-color-text)"},children:"java -jar -Dspring.profiles.active=kafka streamer-app/build/libs/ledger-sync-streamer-app-<version>.jar"})})})}),"\n",(0,t.jsxs)(n.p,{children:["To run with ",(0,t.jsx)(n.strong,{children:"Rabbit"})," binder:"]}),"\n",(0,t.jsxs)(n.ol,{children:["\n",(0,t.jsx)(n.li,{children:"Go to top level directory of the project."}),"\n",(0,t.jsxs)(n.li,{children:["Edit the ",(0,t.jsx)(n.code,{children:"config/application-rabbit.properties"})," file and provide required information for cardano node, datasource, RabbitMQ."]}),"\n",(0,t.jsxs)(n.li,{children:["Run the streamer jar file with ",(0,t.jsx)(n.code,{children:"rabbit"})," profile."]}),"\n"]}),"\n",(0,t.jsx)(n.pre,{"data-language":"text","data-theme":"default",children:(0,t.jsx)(n.code,{"data-language":"text","data-theme":"default",children:(0,t.jsx)(n.span,{className:"line",children:(0,t.jsx)(n.span,{style:{color:"var(--shiki-color-text)"},children:"java -jar -Dspring.profiles.active=rabbit streamer-app/build/libs/ledger-sync-streamer-app-<version>.jar"})})})}),"\n",(0,t.jsxs)(n.p,{children:[(0,t.jsx)(n.strong,{children:"Important:"})," If you want to start the sync again from start, you need to delete the schema from the database and start the sync again."]}),"\n",(0,t.jsx)(n.h2,{id:"how-to-run-docker",children:"How to run (Docker)"}),"\n",(0,t.jsxs)(n.p,{children:["There are two Docker compose files available to run the streamer app with Kafka or RabbitMQ in the ",(0,t.jsx)(n.strong,{children:"streamer-app"})," folder."]}),"\n",(0,t.jsxs)(n.p,{children:["To run with ",(0,t.jsx)(n.strong,{children:"Kafka"})," :"]}),"\n",(0,t.jsx)(n.pre,{"data-language":"shell","data-theme":"default",children:(0,t.jsx)(n.code,{"data-language":"shell","data-theme":"default",children:(0,t.jsxs)(n.span,{className:"line",children:[(0,t.jsx)(n.span,{style:{color:"var(--shiki-token-function)"},children:"docker"}),(0,t.jsx)(n.span,{style:{color:"var(--shiki-color-text)"},children:" "}),(0,t.jsx)(n.span,{style:{color:"var(--shiki-token-string)"},children:"compose"}),(0,t.jsx)(n.span,{style:{color:"var(--shiki-color-text)"},children:" "}),(0,t.jsx)(n.span,{style:{color:"var(--shiki-token-string)"},children:"-f"}),(0,t.jsx)(n.span,{style:{color:"var(--shiki-color-text)"},children:" "}),(0,t.jsx)(n.span,{style:{color:"var(--shiki-token-string)"},children:"docker-compose-kafka.yml"}),(0,t.jsx)(n.span,{style:{color:"var(--shiki-color-text)"},children:" "}),(0,t.jsx)(n.span,{style:{color:"var(--shiki-token-string)"},children:"up"})]})})}),"\n",(0,t.jsxs)(n.p,{children:["To run with ",(0,t.jsx)(n.strong,{children:"RabbitMQ"})," :"]}),"\n",(0,t.jsx)(n.pre,{"data-language":"shell","data-theme":"default",children:(0,t.jsx)(n.code,{"data-language":"shell","data-theme":"default",children:(0,t.jsxs)(n.span,{className:"line",children:[(0,t.jsx)(n.span,{style:{color:"var(--shiki-token-function)"},children:"docker"}),(0,t.jsx)(n.span,{style:{color:"var(--shiki-color-text)"},children:" "}),(0,t.jsx)(n.span,{style:{color:"var(--shiki-token-string)"},children:"compose"}),(0,t.jsx)(n.span,{style:{color:"var(--shiki-color-text)"},children:" "}),(0,t.jsx)(n.span,{style:{color:"var(--shiki-token-string)"},children:"-f"}),(0,t.jsx)(n.span,{style:{color:"var(--shiki-color-text)"},children:" "}),(0,t.jsx)(n.span,{style:{color:"var(--shiki-token-string)"},children:"docker-compose-rabbit.yml"}),(0,t.jsx)(n.span,{style:{color:"var(--shiki-color-text)"},children:" "}),(0,t.jsx)(n.span,{style:{color:"var(--shiki-token-string)"},children:"up"})]})})}),"\n",(0,t.jsxs)(n.p,{children:[(0,t.jsx)(n.strong,{children:"Important:"})," If you want to start the sync again from start, you need to delete the schema from the database and start the sync again."]}),"\n",(0,t.jsx)(n.h3,{id:"to-start-sync-from-a-specific-points",children:"To start sync from a specific points"}),"\n",(0,t.jsx)(n.p,{children:"Set the following properties in config file to start the sync from a specific point:"}),"\n",(0,t.jsx)(n.pre,{"data-language":"text","data-theme":"default",children:(0,t.jsxs)(n.code,{"data-language":"text","data-theme":"default",children:[(0,t.jsx)(n.span,{className:"line",children:(0,t.jsx)(n.span,{style:{color:"var(--shiki-color-text)"},children:"store.cardano.sync-start-slot=<absolute_slot>"})}),"\n",(0,t.jsx)(n.span,{className:"line",children:(0,t.jsx)(n.span,{style:{color:"var(--shiki-color-text)"},children:"store.cardano.sync-start-blockhash=<block_hash>"})})]})}),"\n",(0,t.jsx)(n.p,{children:"For Docker environment, you can set the following environment variables:"}),"\n",(0,t.jsx)(n.pre,{"data-language":"text","data-theme":"default",children:(0,t.jsxs)(n.code,{"data-language":"text","data-theme":"default",children:[(0,t.jsx)(n.span,{className:"line",children:(0,t.jsx)(n.span,{style:{color:"var(--shiki-color-text)"},children:"STORE_CARDANO_SYNC-START-SLOT=<absolute_slot>"})}),"\n",(0,t.jsx)(n.span,{className:"line",children:(0,t.jsx)(n.span,{style:{color:"var(--shiki-color-text)"},children:"STORE_CARDANO_SYNC-START-BLOCKHASH=<block_hash>"})})]})}),"\n",(0,t.jsx)(n.h3,{id:"kafka-configuration",children:"Kafka Configuration"}),"\n",(0,t.jsx)(n.p,{children:"The default message size in Kafka is 1 MB. To accommodate larger JSON payloads, which can exceed 1 MB in the case of some blocks, you will need to increase this value."}),"\n",(0,t.jsx)(n.pre,{"data-language":"text","data-theme":"default",children:(0,t.jsx)(n.code,{"data-language":"text","data-theme":"default",children:(0,t.jsx)(n.span,{className:"line",children:(0,t.jsx)(n.span,{style:{color:"var(--shiki-color-text)"},children:"KAFKA_MESSAGE_MAX_BYTES=20000000"})})})})]})}let d={MDXContent:function(){let e=arguments.length>0&&void 0!==arguments[0]?arguments[0]:{},{wrapper:n}=Object.assign({},(0,o.a)(),e.components);return n?(0,t.jsx)(n,{...e,children:(0,t.jsx)(c,{...e})}):c(e)},pageOpts:{filePath:"pages/applications/streamer_app.mdx",route:"/applications/streamer_app",timestamp:1724046104e3,pageMap:[{kind:"Meta",data:{index:"Overview",design:"Design",build_run:"Build & Run",docker:"Docker",schema:"Schema",applications:{title:"Applications"},about:{title:"About",type:"page"}}},{kind:"MdxPage",name:"about",route:"/about"},{kind:"Folder",name:"applications",route:"/applications",children:[{kind:"Meta",data:{ledger_sync_app:"Ledger Sync",streamer_app:"Ledger Sync Streamer"}},{kind:"MdxPage",name:"ledger_sync_app",route:"/applications/ledger_sync_app"},{kind:"MdxPage",name:"streamer_app",route:"/applications/streamer_app"}]},{kind:"MdxPage",name:"build_run",route:"/build_run"},{kind:"MdxPage",name:"design",route:"/design"},{kind:"MdxPage",name:"docker",route:"/docker"},{kind:"MdxPage",name:"index",route:"/"},{kind:"MdxPage",name:"schema",route:"/schema"}],flexsearch:{codeblocks:!0},title:"Ledger Sync Streamer",headings:l},pageNextRoute:"/applications/streamer_app",nextraLayout:r.ZP,themeConfig:i.Z};n.default=(0,s.j)(d)},2069:function(e,n,a){"use strict";var t=a(5893);a(7294);let s={logo:(0,t.jsx)("span",{children:(0,t.jsx)("b",{children:"Ledger Sync"})}),project:{link:"https://github.com/cardano-foundation/cf-ledger-sync"},chat:{link:"https://discord.gg/jw3yeYJw"},docsRepositoryBase:"https://github.com/cardano-foundation/cf-ledger-sync",footer:{text:"Ledger Sync project"},useNextSeoProps:()=>({titleTemplate:"%s – Ledger Sync"}),head:(0,t.jsxs)(t.Fragment,{children:[(0,t.jsx)("meta",{property:"description",content:"Ledger Sync is a Java-based application designed to provide efficient access to Cardano blockchain data"}),(0,t.jsx)("meta",{property:"og:title",content:"Ledger Sync is a Java-based application designed to provide efficient access to Cardano blockchain data"}),(0,t.jsx)("meta",{property:"og:description",content:"Ledger Sync is a Java-based application designed to provide efficient access to Cardano blockchain data"})]})};n.Z=s},5789:function(){}},function(e){e.O(0,[942,888,774,179],function(){return e(e.s=1906)}),_N_E=e.O()}]);
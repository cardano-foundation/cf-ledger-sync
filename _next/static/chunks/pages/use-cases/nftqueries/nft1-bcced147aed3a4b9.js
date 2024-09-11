(self.webpackChunk_N_E=self.webpackChunk_N_E||[]).push([[976],{3536:function(e,s,n){(window.__NEXT_P=window.__NEXT_P||[]).push(["/use-cases/nftqueries/nft1",function(){return n(7170)}])},7170:function(e,s,n){"use strict";n.r(s),n.d(s,{__toc:function(){return c}});var t=n(5893),a=n(2673),l=n(2169),i=n(2069);n(9488);var o=n(2643),r=n(2154);let c=[{depth:2,value:"1. View Collections",id:"1-view-collections"}];function d(e){let s=Object.assign({h2:"h2",p:"p",a:"a",ul:"ul",li:"li",pre:"pre",code:"code",span:"span",details:"details",summary:"summary"},(0,o.a)(),e.components);return(0,t.jsxs)(t.Fragment,{children:[(0,t.jsx)(s.h2,{id:"1-view-collections",children:"1. View Collections"}),"\n",(0,t.jsxs)(s.p,{children:["NFT stands for non-fungible token. It is intended to be a unique token as in one and only one ever will be minted and no other token is then comparably equivalent to it. For this first query, we aim to find mint transactions with the label 721 in the metadata of the token. The 721 tag refers to the original standard for NFTs on Cardano as defined in ",(0,t.jsx)(s.a,{href:"https://cips.cardano.org/cip/CIP-25",children:"CIP-25"}),"."]}),"\n",(0,t.jsx)(s.p,{children:"Our query will return NFTs (label 721) collections in Cardano with their respective:"}),"\n",(0,t.jsxs)(s.ul,{children:["\n",(0,t.jsx)(s.li,{children:"asset collection name,"}),"\n",(0,t.jsx)(s.li,{children:"policy ID"}),"\n",(0,t.jsx)(s.li,{children:"total supply"}),"\n",(0,t.jsx)(s.li,{children:"creation date"}),"\n"]}),"\n",(0,t.jsx)(r.UW,{type:"info",emoji:"ℹ️",children:(0,t.jsxs)(s.p,{children:["Remember you can also query the blockchain for this data using an explorer such as ",(0,t.jsx)(s.a,{href:"https://cexplorer.io/collection",children:"cexplorer"})]})}),"\n",(0,t.jsx)(s.pre,{"data-language":"sql","data-theme":"default",children:(0,t.jsxs)(s.code,{"data-language":"sql","data-theme":"default",children:[(0,t.jsx)(s.span,{className:"line",children:(0,t.jsx)(s.span,{style:{color:"var(--shiki-token-keyword)"},children:"SELECT"})}),"\n",(0,t.jsxs)(s.span,{className:"line",children:[(0,t.jsx)(s.span,{style:{color:"var(--shiki-color-text)"},children:"	ma."}),(0,t.jsx)(s.span,{style:{color:"var(--shiki-token-string-expression)"},children:'"policy"'}),(0,t.jsx)(s.span,{style:{color:"var(--shiki-color-text)"},children:" "}),(0,t.jsx)(s.span,{style:{color:"var(--shiki-token-keyword)"},children:"AS"}),(0,t.jsx)(s.span,{style:{color:"var(--shiki-color-text)"},children:" policy_id,"})]}),"\n",(0,t.jsxs)(s.span,{className:"line",children:[(0,t.jsx)(s.span,{style:{color:"var(--shiki-color-text)"},children:"	"}),(0,t.jsx)(s.span,{style:{color:"var(--shiki-token-function)"},children:"min"}),(0,t.jsx)(s.span,{style:{color:"var(--shiki-color-text)"},children:"(ma.time) "}),(0,t.jsx)(s.span,{style:{color:"var(--shiki-token-keyword)"},children:"AS"}),(0,t.jsx)(s.span,{style:{color:"var(--shiki-color-text)"},children:" creation_time,"})]}),"\n",(0,t.jsxs)(s.span,{className:"line",children:[(0,t.jsx)(s.span,{style:{color:"var(--shiki-color-text)"},children:"	"}),(0,t.jsx)(s.span,{style:{color:"var(--shiki-token-function)"},children:"sum"}),(0,t.jsx)(s.span,{style:{color:"var(--shiki-color-text)"},children:"(mtm.quantity)  "}),(0,t.jsx)(s.span,{style:{color:"var(--shiki-token-keyword)"},children:"AS"}),(0,t.jsx)(s.span,{style:{color:"var(--shiki-color-text)"},children:" total_supply"})]}),"\n",(0,t.jsxs)(s.span,{className:"line",children:[(0,t.jsx)(s.span,{style:{color:"var(--shiki-token-keyword)"},children:"FROM"}),(0,t.jsx)(s.span,{style:{color:"var(--shiki-color-text)"},children:" "})]}),"\n",(0,t.jsx)(s.span,{className:"line",children:(0,t.jsx)(s.span,{style:{color:"var(--shiki-color-text)"},children:"   	multi_asset ma"})}),"\n",(0,t.jsxs)(s.span,{className:"line",children:[(0,t.jsx)(s.span,{style:{color:"var(--shiki-token-keyword)"},children:"LEFT JOIN"}),(0,t.jsx)(s.span,{style:{color:"var(--shiki-color-text)"},children:" "})]}),"\n",(0,t.jsxs)(s.span,{className:"line",children:[(0,t.jsx)(s.span,{style:{color:"var(--shiki-color-text)"},children:"    	ma_tx_mint mtm "}),(0,t.jsx)(s.span,{style:{color:"var(--shiki-token-keyword)"},children:"ON"}),(0,t.jsx)(s.span,{style:{color:"var(--shiki-color-text)"},children:" ma.id "}),(0,t.jsx)(s.span,{style:{color:"var(--shiki-token-keyword)"},children:"="}),(0,t.jsx)(s.span,{style:{color:"var(--shiki-color-text)"},children:" mtm.ident"})]}),"\n",(0,t.jsxs)(s.span,{className:"line",children:[(0,t.jsx)(s.span,{style:{color:"var(--shiki-token-keyword)"},children:"LEFT JOIN"}),(0,t.jsx)(s.span,{style:{color:"var(--shiki-color-text)"},children:" "})]}),"\n",(0,t.jsxs)(s.span,{className:"line",children:[(0,t.jsx)(s.span,{style:{color:"var(--shiki-color-text)"},children:"	("}),(0,t.jsx)(s.span,{style:{color:"var(--shiki-token-keyword)"},children:"SELECT"}),(0,t.jsx)(s.span,{style:{color:"var(--shiki-color-text)"},children:" tm.tx_id "}),(0,t.jsx)(s.span,{style:{color:"var(--shiki-token-keyword)"},children:"AS"}),(0,t.jsx)(s.span,{style:{color:"var(--shiki-color-text)"},children:" txid "}),(0,t.jsx)(s.span,{style:{color:"var(--shiki-token-keyword)"},children:"FROM"}),(0,t.jsx)(s.span,{style:{color:"var(--shiki-color-text)"},children:" tx_metadata tm "})]}),"\n",(0,t.jsxs)(s.span,{className:"line",children:[(0,t.jsx)(s.span,{style:{color:"var(--shiki-color-text)"},children:"		"}),(0,t.jsx)(s.span,{style:{color:"var(--shiki-token-keyword)"},children:"WHERE"}),(0,t.jsx)(s.span,{style:{color:"var(--shiki-color-text)"},children:" tm."}),(0,t.jsx)(s.span,{style:{color:"var(--shiki-token-string-expression)"},children:'"key"'}),(0,t.jsx)(s.span,{style:{color:"var(--shiki-color-text)"},children:" "}),(0,t.jsx)(s.span,{style:{color:"var(--shiki-token-keyword)"},children:"="}),(0,t.jsx)(s.span,{style:{color:"var(--shiki-color-text)"},children:" "}),(0,t.jsx)(s.span,{style:{color:"var(--shiki-token-constant)"},children:"721"})]}),"\n",(0,t.jsxs)(s.span,{className:"line",children:[(0,t.jsx)(s.span,{style:{color:"var(--shiki-color-text)"},children:"		) "}),(0,t.jsx)(s.span,{style:{color:"var(--shiki-token-keyword)"},children:"AS"}),(0,t.jsx)(s.span,{style:{color:"var(--shiki-color-text)"},children:" tm "}),(0,t.jsx)(s.span,{style:{color:"var(--shiki-token-keyword)"},children:"ON"}),(0,t.jsx)(s.span,{style:{color:"var(--shiki-color-text)"},children:" tm.txid "}),(0,t.jsx)(s.span,{style:{color:"var(--shiki-token-keyword)"},children:"="}),(0,t.jsx)(s.span,{style:{color:"var(--shiki-color-text)"},children:" mtm.tx_id"})]}),"\n",(0,t.jsxs)(s.span,{className:"line",children:[(0,t.jsx)(s.span,{style:{color:"var(--shiki-token-keyword)"},children:"GROUP BY"}),(0,t.jsx)(s.span,{style:{color:"var(--shiki-color-text)"},children:" ma."}),(0,t.jsx)(s.span,{style:{color:"var(--shiki-token-string-expression)"},children:'"policy"'})]}),"\n",(0,t.jsxs)(s.span,{className:"line",children:[(0,t.jsx)(s.span,{style:{color:"var(--shiki-token-keyword)"},children:"LIMIT"}),(0,t.jsx)(s.span,{style:{color:"var(--shiki-color-text)"},children:" "}),(0,t.jsx)(s.span,{style:{color:"var(--shiki-token-constant)"},children:"200"})]})]})}),"\n",(0,t.jsxs)(s.details,{children:[(0,t.jsx)(s.summary,{children:(0,t.jsx)(s.p,{children:"Expected results format"})}),(0,t.jsx)(s.pre,{"data-language":"sql","data-theme":"default",children:(0,t.jsxs)(s.code,{"data-language":"sql","data-theme":"default",children:[(0,t.jsx)(s.span,{className:"line",children:(0,t.jsx)(s.span,{style:{color:"var(--shiki-color-text)"},children:"policy_id                                                | creation_time             | total_supply		"})}),"\n",(0,t.jsx)(s.span,{className:"line",children:(0,t.jsx)(s.span,{style:{color:"var(--shiki-token-comment)"},children:"---------------------------------------------------------+---------------------------+-------------"})}),"\n",(0,t.jsxs)(s.span,{className:"line",children:[(0,t.jsx)(s.span,{style:{color:"var(--shiki-color-text)"},children:"00000002df633853f6a47465c9496721d2d5b1291b8398016c0e87ae | "}),(0,t.jsx)(s.span,{style:{color:"var(--shiki-token-constant)"},children:"2021"}),(0,t.jsx)(s.span,{style:{color:"var(--shiki-token-keyword)"},children:"-"}),(0,t.jsx)(s.span,{style:{color:"var(--shiki-token-constant)"},children:"03"}),(0,t.jsx)(s.span,{style:{color:"var(--shiki-token-keyword)"},children:"-"}),(0,t.jsx)(s.span,{style:{color:"var(--shiki-token-constant)"},children:"01"}),(0,t.jsx)(s.span,{style:{color:"var(--shiki-color-text)"},children:" "}),(0,t.jsx)(s.span,{style:{color:"var(--shiki-token-constant)"},children:"21"}),(0,t.jsx)(s.span,{style:{color:"var(--shiki-color-text)"},children:":"}),(0,t.jsx)(s.span,{style:{color:"var(--shiki-token-constant)"},children:"47"}),(0,t.jsx)(s.span,{style:{color:"var(--shiki-color-text)"},children:":"}),(0,t.jsx)(s.span,{style:{color:"var(--shiki-token-constant)"},children:"37"}),(0,t.jsx)(s.span,{style:{color:"var(--shiki-color-text)"},children:"."}),(0,t.jsx)(s.span,{style:{color:"var(--shiki-token-constant)"},children:"000"}),(0,t.jsx)(s.span,{style:{color:"var(--shiki-color-text)"},children:"   | "}),(0,t.jsx)(s.span,{style:{color:"var(--shiki-token-constant)"},children:"1"})]}),"\n",(0,t.jsxs)(s.span,{className:"line",children:[(0,t.jsx)(s.span,{style:{color:"var(--shiki-color-text)"},children:"0000002c60240315c2371636214cf686e08a391742bff8f8b8ed56be | "}),(0,t.jsx)(s.span,{style:{color:"var(--shiki-token-constant)"},children:"2023"}),(0,t.jsx)(s.span,{style:{color:"var(--shiki-token-keyword)"},children:"-"}),(0,t.jsx)(s.span,{style:{color:"var(--shiki-token-constant)"},children:"06"}),(0,t.jsx)(s.span,{style:{color:"var(--shiki-token-keyword)"},children:"-"}),(0,t.jsx)(s.span,{style:{color:"var(--shiki-token-constant)"},children:"22"}),(0,t.jsx)(s.span,{style:{color:"var(--shiki-color-text)"},children:" "}),(0,t.jsx)(s.span,{style:{color:"var(--shiki-token-constant)"},children:"00"}),(0,t.jsx)(s.span,{style:{color:"var(--shiki-color-text)"},children:":"}),(0,t.jsx)(s.span,{style:{color:"var(--shiki-token-constant)"},children:"28"}),(0,t.jsx)(s.span,{style:{color:"var(--shiki-color-text)"},children:":"}),(0,t.jsx)(s.span,{style:{color:"var(--shiki-token-constant)"},children:"48"}),(0,t.jsx)(s.span,{style:{color:"var(--shiki-color-text)"},children:"."}),(0,t.jsx)(s.span,{style:{color:"var(--shiki-token-constant)"},children:"000"}),(0,t.jsx)(s.span,{style:{color:"var(--shiki-color-text)"},children:"   | "}),(0,t.jsx)(s.span,{style:{color:"var(--shiki-token-constant)"},children:"2"})]}),"\n",(0,t.jsxs)(s.span,{className:"line",children:[(0,t.jsx)(s.span,{style:{color:"var(--shiki-color-text)"},children:"000000adf8fcbdf03a5c154123aff674edf287fb13532a343b617fb2 | "}),(0,t.jsx)(s.span,{style:{color:"var(--shiki-token-constant)"},children:"2021"}),(0,t.jsx)(s.span,{style:{color:"var(--shiki-token-keyword)"},children:"-"}),(0,t.jsx)(s.span,{style:{color:"var(--shiki-token-constant)"},children:"06"}),(0,t.jsx)(s.span,{style:{color:"var(--shiki-token-keyword)"},children:"-"}),(0,t.jsx)(s.span,{style:{color:"var(--shiki-token-constant)"},children:"25"}),(0,t.jsx)(s.span,{style:{color:"var(--shiki-color-text)"},children:" "}),(0,t.jsx)(s.span,{style:{color:"var(--shiki-token-constant)"},children:"01"}),(0,t.jsx)(s.span,{style:{color:"var(--shiki-color-text)"},children:":"}),(0,t.jsx)(s.span,{style:{color:"var(--shiki-token-constant)"},children:"20"}),(0,t.jsx)(s.span,{style:{color:"var(--shiki-color-text)"},children:":"}),(0,t.jsx)(s.span,{style:{color:"var(--shiki-token-constant)"},children:"11"}),(0,t.jsx)(s.span,{style:{color:"var(--shiki-color-text)"},children:"."}),(0,t.jsx)(s.span,{style:{color:"var(--shiki-token-constant)"},children:"000"}),(0,t.jsx)(s.span,{style:{color:"var(--shiki-color-text)"},children:"   | "}),(0,t.jsx)(s.span,{style:{color:"var(--shiki-token-constant)"},children:"1"})]}),"\n",(0,t.jsxs)(s.span,{className:"line",children:[(0,t.jsx)(s.span,{style:{color:"var(--shiki-color-text)"},children:"000006126365b110b339bd39c5b5c0fc89eb146cc186ba8c90a3c9ca | "}),(0,t.jsx)(s.span,{style:{color:"var(--shiki-token-constant)"},children:"2021"}),(0,t.jsx)(s.span,{style:{color:"var(--shiki-token-keyword)"},children:"-"}),(0,t.jsx)(s.span,{style:{color:"var(--shiki-token-constant)"},children:"07"}),(0,t.jsx)(s.span,{style:{color:"var(--shiki-token-keyword)"},children:"-"}),(0,t.jsx)(s.span,{style:{color:"var(--shiki-token-constant)"},children:"22"}),(0,t.jsx)(s.span,{style:{color:"var(--shiki-color-text)"},children:" "}),(0,t.jsx)(s.span,{style:{color:"var(--shiki-token-constant)"},children:"00"}),(0,t.jsx)(s.span,{style:{color:"var(--shiki-color-text)"},children:":"}),(0,t.jsx)(s.span,{style:{color:"var(--shiki-token-constant)"},children:"00"}),(0,t.jsx)(s.span,{style:{color:"var(--shiki-color-text)"},children:":"}),(0,t.jsx)(s.span,{style:{color:"var(--shiki-token-constant)"},children:"26"}),(0,t.jsx)(s.span,{style:{color:"var(--shiki-color-text)"},children:"."}),(0,t.jsx)(s.span,{style:{color:"var(--shiki-token-constant)"},children:"000"}),(0,t.jsx)(s.span,{style:{color:"var(--shiki-color-text)"},children:"   | "}),(0,t.jsx)(s.span,{style:{color:"var(--shiki-token-constant)"},children:"48"})]}),"\n",(0,t.jsxs)(s.span,{className:"line",children:[(0,t.jsx)(s.span,{style:{color:"var(--shiki-color-text)"},children:"000020225c7dbf1e33be4961727348c002f0ffaf115e538f54201986 | "}),(0,t.jsx)(s.span,{style:{color:"var(--shiki-token-constant)"},children:"2022"}),(0,t.jsx)(s.span,{style:{color:"var(--shiki-token-keyword)"},children:"-"}),(0,t.jsx)(s.span,{style:{color:"var(--shiki-token-constant)"},children:"04"}),(0,t.jsx)(s.span,{style:{color:"var(--shiki-token-keyword)"},children:"-"}),(0,t.jsx)(s.span,{style:{color:"var(--shiki-token-constant)"},children:"04"}),(0,t.jsx)(s.span,{style:{color:"var(--shiki-color-text)"},children:" "}),(0,t.jsx)(s.span,{style:{color:"var(--shiki-token-constant)"},children:"21"}),(0,t.jsx)(s.span,{style:{color:"var(--shiki-color-text)"},children:":"}),(0,t.jsx)(s.span,{style:{color:"var(--shiki-token-constant)"},children:"22"}),(0,t.jsx)(s.span,{style:{color:"var(--shiki-color-text)"},children:":"}),(0,t.jsx)(s.span,{style:{color:"var(--shiki-token-constant)"},children:"31"}),(0,t.jsx)(s.span,{style:{color:"var(--shiki-color-text)"},children:"."}),(0,t.jsx)(s.span,{style:{color:"var(--shiki-token-constant)"},children:"000"}),(0,t.jsx)(s.span,{style:{color:"var(--shiki-color-text)"},children:"   | "}),(0,t.jsx)(s.span,{style:{color:"var(--shiki-token-constant)"},children:"21"})]}),"\n",(0,t.jsx)(s.span,{className:"line",children:(0,t.jsx)(s.span,{style:{color:"var(--shiki-color-text)"},children:"        ....                                             |  ....                     | ...."})}),"\n",(0,t.jsx)(s.span,{className:"line",children:" "}),"\n",(0,t.jsxs)(s.span,{className:"line",children:[(0,t.jsx)(s.span,{style:{color:"var(--shiki-color-text)"},children:"(Note: results trimmed "}),(0,t.jsx)(s.span,{style:{color:"var(--shiki-token-keyword)"},children:"for"}),(0,t.jsx)(s.span,{style:{color:"var(--shiki-color-text)"},children:" readability) "})]}),"\n",(0,t.jsx)(s.span,{className:"line",children:(0,t.jsx)(s.span,{style:{color:"var(--shiki-color-text)"},children:"             "})})]})})]}),"\n",(0,t.jsx)(s.p,{children:"\uD83D\uDD0E Let's take a closer look at each part of the query."}),"\n",(0,t.jsx)(s.p,{children:"We are looking (SELECT'ing) from the following tables:"}),"\n",(0,t.jsxs)(s.p,{children:[(0,t.jsx)(s.a,{href:"https://github.com/cardano-foundation/cf-ledger-sync/blob/main/docs/pages/schema.md#multi_asset",children:"multi_asset"})," (ma): This table stores information about all the different tokens or assets on Cardano. Each asset is uniquely identified by a combination of policy (the policy ID under which the asset was minted) and id (a unique identifier within that policy)."]}),"\n",(0,t.jsxs)(s.p,{children:[(0,t.jsx)(s.a,{href:"https://github.com/cardano-foundation/cf-ledger-sync/blob/main/docs/pages/schema.md#ma_tx_mint",children:"ma_tx_mint"})," (mtm): This table tracks the minting (creation) transactions of assets. It links to the ",(0,t.jsx)(s.a,{href:"https://github.com/cardano-foundation/cf-ledger-sync/blob/main/docs/pages/schema.md#multi_asset",children:"multi_asset"})," table using ",(0,t.jsx)(s.code,{children:"ident"}),", and also stores information about the quantity minted in each transaction."]}),"\n",(0,t.jsxs)(s.p,{children:[(0,t.jsx)(s.a,{href:"https://github.com/cardano-foundation/cf-ledger-sync/blob/main/docs/pages/schema.md#tx_metadata",children:"tx_metadata"})," (tm): This table holds additional metadata associated with transactions, and for this query, we are filtering on metadata with the key '721'."]}),"\n",(0,t.jsxs)(s.p,{children:[(0,t.jsx)(s.code,{children:"LEFT JOIN ma_tx_mint ON ..."})," joins the asset information (",(0,t.jsx)(s.a,{href:"https://github.com/cardano-foundation/cf-ledger-sync/blob/main/docs/pages/schema.md#multi_asset",children:"multi_asset"}),") with its minting transactions (",(0,t.jsx)(s.a,{href:"https://github.com/cardano-foundation/cf-ledger-sync/blob/main/docs/pages/schema.md#ma_tx_mint",children:"ma_tx_mint"}),"). The ",(0,t.jsx)(s.code,{children:"LEFT JOIN"})," ensures that even assets that have not been minted are included in the result, but their ",(0,t.jsx)(s.code,{children:"total_supply"})," will be ",(0,t.jsx)(s.code,{children:"NULL"}),"."]}),"\n",(0,t.jsxs)(s.p,{children:[(0,t.jsx)(s.code,{children:"LEFT JOIN (SELECT ... FROM tx_metadata) AS tm ON ..."})," is a subquery that filters ",(0,t.jsx)(s.code,{children:"tx_metadata"})," to only include rows where the metadata key is ",(0,t.jsx)(s.code,{children:"721"}),".  The result of this subquery is then joined with the previous result, effectively filtering out assets that are not associated with transactions containing this specific metadata."]}),"\n",(0,t.jsxs)(s.p,{children:[(0,t.jsx)(s.code,{children:'GROUP BY ma."policy"'})," groups the results by the policy (policy ID), meaning it will calculate the total_supply and find the earliest creation_time for each unique policy."]}),"\n",(0,t.jsxs)(s.p,{children:["The ",(0,t.jsx)(s.code,{children:"SELECT ..."})," clause selects the ",(0,t.jsx)(s.code,{children:"policy_id"}),", the earliest ",(0,t.jsx)(s.code,{children:"creation_time"})," for that policy, and the ",(0,t.jsx)(s.code,{children:"total_supply"})," (sum of quantities minted across all transactions for that policy)."]}),"\n",(0,t.jsxs)(s.p,{children:[(0,t.jsx)(s.code,{children:"LIMIT 200"})," limits the output to the first 200 policies."]})]})}let h={MDXContent:function(){let e=arguments.length>0&&void 0!==arguments[0]?arguments[0]:{},{wrapper:s}=Object.assign({},(0,o.a)(),e.components);return s?(0,t.jsx)(s,{...e,children:(0,t.jsx)(d,{...e})}):d(e)},pageOpts:{filePath:"pages/use-cases/nftqueries/nft1.mdx",route:"/use-cases/nftqueries/nft1",timestamp:1726040384e3,pageMap:[{kind:"MdxPage",name:"about",route:"/about"},{kind:"Folder",name:"applications",route:"/applications",children:[{kind:"Meta",data:{ledger_sync_app:"Ledger Sync",streamer_app:"Ledger Sync Streamer"}},{kind:"MdxPage",name:"ledger_sync_app",route:"/applications/ledger_sync_app"},{kind:"MdxPage",name:"streamer_app",route:"/applications/streamer_app"}]},{kind:"MdxPage",name:"build_run",route:"/build_run"},{kind:"MdxPage",name:"design",route:"/design"},{kind:"MdxPage",name:"docker",route:"/docker"},{kind:"MdxPage",name:"index",route:"/"},{kind:"MdxPage",name:"schema",route:"/schema"},{kind:"Folder",name:"use-cases",route:"/use-cases",children:[{kind:"Folder",name:"WalletQueries",route:"/use-cases/WalletQueries",children:[{kind:"Meta",data:{wallet01:"Find the ADA balance of a wallet",wallet02:"List a balance's UTXOs",wallet03:"Review wallet transaction history",wallet04:"List all tokens in a wallet",wallet05:"Query collection details",wallet06:"Query Token metadata",wallet07:"Query Active Stake Pools",wallet08:"Query Stake Pool details",wallet09:"Stake pool lifetime blocks",wallet10:"List current dReps",wallet11:"Find Active Governance Proposals",wallet12:"Smart contract transaction data"}},{kind:"MdxPage",name:"wallet01",route:"/use-cases/WalletQueries/wallet01"},{kind:"MdxPage",name:"wallet02",route:"/use-cases/WalletQueries/wallet02"},{kind:"MdxPage",name:"wallet03",route:"/use-cases/WalletQueries/wallet03"},{kind:"MdxPage",name:"wallet04",route:"/use-cases/WalletQueries/wallet04"},{kind:"MdxPage",name:"wallet05",route:"/use-cases/WalletQueries/wallet05"},{kind:"MdxPage",name:"wallet06",route:"/use-cases/WalletQueries/wallet06"},{kind:"MdxPage",name:"wallet07",route:"/use-cases/WalletQueries/wallet07"},{kind:"MdxPage",name:"wallet08",route:"/use-cases/WalletQueries/wallet08"},{kind:"MdxPage",name:"wallet09",route:"/use-cases/WalletQueries/wallet09"},{kind:"MdxPage",name:"wallet10",route:"/use-cases/WalletQueries/wallet10"},{kind:"MdxPage",name:"wallet11",route:"/use-cases/WalletQueries/wallet11"},{kind:"MdxPage",name:"wallet12",route:"/use-cases/WalletQueries/wallet12"}]},{kind:"MdxPage",name:"WalletQueries",route:"/use-cases/WalletQueries"},{kind:"Meta",data:{WalletQueries:"Wallet queries",nftqueries:"NFT marketplace"}},{kind:"Folder",name:"nftqueries",route:"/use-cases/nftqueries",children:[{kind:"Meta",data:{nft1:"View Collections",nft2:"View NFT collection details",nft3:"View all assets",nft4:"Minting an NFT",nft5:"Selling an NFT",nft6:"Buying an NFT"}},{kind:"MdxPage",name:"nft1",route:"/use-cases/nftqueries/nft1"},{kind:"MdxPage",name:"nft2",route:"/use-cases/nftqueries/nft2"},{kind:"MdxPage",name:"nft3",route:"/use-cases/nftqueries/nft3"},{kind:"MdxPage",name:"nft4",route:"/use-cases/nftqueries/nft4"},{kind:"MdxPage",name:"nft5",route:"/use-cases/nftqueries/nft5"},{kind:"MdxPage",name:"nft6",route:"/use-cases/nftqueries/nft6"}]},{kind:"MdxPage",name:"nftqueries",route:"/use-cases/nftqueries"}]},{kind:"MdxPage",name:"use-cases",route:"/use-cases"},{kind:"Meta",data:{about:"About",build_run:"Build Run",design:"Design",docker:"Docker",index:"Index",schema:"Schema","use-cases":"Use Cases"}}],flexsearch:{codeblocks:!0},title:"Nft1",headings:c},pageNextRoute:"/use-cases/nftqueries/nft1",nextraLayout:l.ZP,themeConfig:i.Z};s.default=(0,a.j)(h)},2069:function(e,s,n){"use strict";var t=n(5893);n(7294);let a={logo:(0,t.jsx)("span",{children:(0,t.jsx)("b",{children:"Ledger Sync"})}),project:{link:"https://github.com/cardano-foundation/cf-ledger-sync"},chat:{link:"https://discord.gg/jw3yeYJw"},docsRepositoryBase:"https://github.com/cardano-foundation/cf-ledger-sync",footer:{text:"Ledger Sync project"},useNextSeoProps:()=>({titleTemplate:"%s – Ledger Sync"}),head:(0,t.jsxs)(t.Fragment,{children:[(0,t.jsx)("meta",{property:"description",content:"Ledger Sync is a Java-based application designed to provide efficient access to Cardano blockchain data"}),(0,t.jsx)("meta",{property:"og:title",content:"Ledger Sync is a Java-based application designed to provide efficient access to Cardano blockchain data"}),(0,t.jsx)("meta",{property:"og:description",content:"Ledger Sync is a Java-based application designed to provide efficient access to Cardano blockchain data"})]})};s.Z=a},5789:function(){}},function(e){e.O(0,[942,888,774,179],function(){return e(e.s=3536)}),_N_E=e.O()}]);
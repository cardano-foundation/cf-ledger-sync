import React from 'react'
import { DocsThemeConfig } from 'nextra-theme-docs'

const config: DocsThemeConfig = {
  logo: <span><b>Ledger Sync</b></span>,
  project: {
    link: 'https://github.com/cardano-foundation/cf-ledger-sync',
  },
  chat: {
    link: 'https://discord.gg/jw3yeYJw',
  },
  docsRepositoryBase: 'https://github.com/cardano-foundation/cf-ledger-sync/blob/main/',
  footer: {
    text: 'Ledger Sync project',
  },
   useNextSeoProps() {
        return {
            titleTemplate: '%s – Ledger Sync'
        }
   },
    head: (
        <>
            <meta property="description" content="Ledger Sync is a Java-based application designed to provide efficient access to Cardano blockchain data"/>
            <meta property="og:title" content="Ledger Sync is a Java-based application designed to provide efficient access to Cardano blockchain data"/>
            <meta property="og:description" content="Ledger Sync is a Java-based application designed to provide efficient access to Cardano blockchain data"/>
        </>
    )
}

export default config

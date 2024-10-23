const withNextra = require('nextra')({
  theme: 'nextra-theme-docs',
  themeConfig: './theme.config.tsx',
})

module.exports = {
    ...withNextra(),
    basePath: '/cf-ledger-sync',
    assetPrefix: '/cf-ledger-sync/',
    images: {
        unoptimized: true,
    },
    output: 'export'
}

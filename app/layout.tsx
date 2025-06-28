import type { Metadata } from "next";
import localFont from "next/font/local";

import "./preflight.css";
import "./globals.css";

const switzer = localFont({
  src: [
    {
      path: "./fonts/Switzer-Regular.woff2",
      weight: "300",
      style: "normal",
    },
    {
      path: "./fonts/Switzer-SemiBold.woff2",
      weight: "700",
      style: "normal",
    },
  ],
});

export const metadata: Metadata = {
  title: "Ledger Sync: Efficient Data Querying for Cardano Blockchain",
  description:
    "Need fast, flexible access to Cardano blockchain data? Ledger Sync delivers enterprise-grade performance and simplified integration. Open-source.",
  icons: {
    icon: "/favicon.ico",
  },
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="en" className={switzer.className}>
      <body>{children}</body>
    </html>
  );
}

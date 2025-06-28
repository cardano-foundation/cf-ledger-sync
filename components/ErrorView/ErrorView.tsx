"use client";
import * as React from "react";
import Link from "next/link";

import { Layout } from "@/components/Layout/Layout";
import classes from "./ErrorView.module.css";

interface ErrorViewProps {
  startOverLink?: string;
  closeLink?: string;
}

export const ErrorView = ({ startOverLink, closeLink }: ErrorViewProps) => {
  return (
    <Layout gradient="blue" closeLink={closeLink}>
      <div className={classes.errorView}>
        <div className={classes.errorViewContent}>
          <h1 className={classes.errorViewTitle}>
            Sorry, something went wrong
          </h1>
          <h3 className={classes.errorViewDescription}>
            An unexpected error has occurred. Please try to start over the
            process.
          </h3>
          {startOverLink && (
            <Link href={startOverLink} className={classes.errorViewStartOver}>
              Start over
            </Link>
          )}
        </div>
      </div>
    </Layout>
  );
};

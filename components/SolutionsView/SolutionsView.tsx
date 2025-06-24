import * as React from "react";

import { Layout } from "@/components/Layout/Layout";
import { Solution } from "@/components/Solution/Solution";
import classes from "./SolutionsView.module.css";
import Link from "next/link";
import Image from "next/image";
import ArrowLeftIcon from "@/components/icons/arrow-left.svg";

export type SolutionType = {
  name: string;
  url?: string;
  description?: string;
  pros?: string[];
  cons?: string[];
};

interface SolutionsViewProps {
  solutions: SolutionType[];
}

export const SolutionsView = ({ solutions }: SolutionsViewProps) => {
  return (
    <Layout gradient="orange">
      <div className={classes.solutionsViewWrapper}>
        <a href="" className={classes.solutionsViewBackButton}>
          <Image priority src={ArrowLeftIcon} alt="Arrow left icon" />
          <span>Back</span>
        </a>
        <div className={classes.solutionsViewTitleWrapper}>
          <h2 className={classes.solutionsViewTitle}>
            Your solutions{" "}
            <span className={classes.solutionsViewCount}>
              ({solutions.length})
            </span>
          </h2>

          <Link
            href={process?.env?.FIRST_QUESTION_URL || ""}
            className={classes.solutionsViewStartOver}
          >
            Start over
          </Link>
        </div>

        <div className={classes.solutionsViewContainer}>
          {solutions.map(({ description, pros, cons, name, url }, index) => (
            <Solution
              key={`solution-${name}-${index}`}
              name={name}
              description={description}
              pros={pros}
              cons={cons}
              link={url}
            />
          ))}
        </div>
      </div>
    </Layout>
  );
};

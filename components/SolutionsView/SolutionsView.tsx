import * as React from "react";

import { Layout } from "@/components/Layout/Layout";
import { Solution } from "@/components/Solution/Solution";
import classes from "./SolutionsView.module.css";
import Link from "next/link";
import Image from "next/image";
import ArrowLeftIcon from "@/components/icons/arrow-left.svg";
import path from "path";
import fs from "fs";
import yaml from "js-yaml";
import { VertexWithLink } from "@/app/utils/loadDiagramData";

export type SolutionType = {
  name: string;
  url?: string;
  description?: string;
  pros?: string[];
  cons?: string[];
};

interface SolutionsViewProps {
  currentQuestion: VertexWithLink;
  prevQuestionId?: string;
  closeLink?: string;
}

export const SolutionsView = ({
  prevQuestionId,
  currentQuestion,
  closeLink,
}: SolutionsViewProps) => {
  const directoryPath = path.join(
    process.cwd(),
    "storage",
    "solutions",
    ...currentQuestion?.link.split("/"),
  );
  const fileNames = fs.readdirSync(directoryPath);
  const solutions = fileNames.map((fileName) => {
    const filePath = path.join(directoryPath, fileName);
    const fileOutput = fs.readFileSync(filePath, "utf-8");
    return yaml.load(fileOutput);
  }) as SolutionType[];

  return (
    <Layout gradient="orange" closeLink={closeLink}>
      <div className={classes.solutionsViewWrapper}>
        <div className={classes.solutionsViewContent}>
          {prevQuestionId && (
            <Link
              href={`/questionnaire/${prevQuestionId}`}
              className={classes.solutionsViewBackButton}
            >
              <Image priority src={ArrowLeftIcon} alt="" />
              <span>Back</span>
            </Link>
          )}
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
      </div>
    </Layout>
  );
};

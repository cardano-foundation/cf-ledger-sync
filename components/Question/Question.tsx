import * as React from "react";
import Link from "next/link";
import path from "path";
import fs from "fs";
import yaml from "js-yaml";
import Image from "next/image";
import { CollapsibleSidebar } from "@/components/CollapsibleSidebar/CollapsibleSidebar";
import ArrowRightIcon from "@/components/icons/arrow-right.svg";
import NutIcon from "@/components/icons/nut.svg";
import { ProsAndCons } from "@/components/ProsAndCons/ProsAndCons";
import { ToolsAndUseCases } from "@/components/ToolsAndUseCases/ToolsAndUseCases";
import classes from "./Question.module.css";

interface QuestionProps {
  text: string;
  link: string;
  isExternal?: boolean;
  id: string;
}

type AdditionalDataType = {
  pros?: string[];
  cons?: string[];
  usuallyUsed?: string[];
  toolsAndUseCases?: { title: string; text: string }[];
} | null;

export const Question = ({ text, link, isExternal, id }: QuestionProps) => {
  const filePath = path.join(
    process.cwd(),
    "storage",
    "questions",
    `${id}.yml`,
  );
  let additionalData: AdditionalDataType = null;
  if (fs.existsSync(filePath)) {
    const fileOutput = fs.readFileSync(filePath, "utf-8");
    additionalData = yaml.load(fileOutput) as AdditionalDataType;
  }

  return (
    <div className={classes.questionWrapper}>
      {isExternal ? (
        <a
          href={link}
          target="_blank"
          rel="noopener noreferrer"
          className={classes.questionLink}
        >
          <h2 className={classes.questionText}>{text}</h2>
          <span className={classes.questionCircle} />
        </a>
      ) : (
        <Link href={link} className={classes.questionLink}>
          <h2 className={classes.questionText}>{text}</h2>
          <span className={classes.questionCircle} />
        </Link>
      )}

      {(additionalData?.toolsAndUseCases ||
        additionalData?.pros ||
        additionalData?.cons ||
        additionalData?.usuallyUsed) && (
        <div className={classes.questionMoreOptionsWrapper}>
          {(additionalData?.pros ||
            additionalData?.cons ||
            additionalData?.usuallyUsed) && (
            <CollapsibleSidebar
              title="Pros & cons"
              description={text}
              hasSmallPadding
              trigger={
                <button className={classes.questionProsAndCons}>
                  <span>View pros & cons</span>
                  <Image priority src={ArrowRightIcon} alt="" />
                </button>
              }
            >
              <ProsAndCons
                pros={additionalData.pros}
                cons={additionalData.cons}
                usuallyUsed={additionalData.usuallyUsed}
              />
            </CollapsibleSidebar>
          )}

          {additionalData?.toolsAndUseCases && (
            <CollapsibleSidebar
              title="Tools and use cases"
              trigger={
                <button className={classes.questionToolsAndUseCases}>
                  <Image priority src={NutIcon} alt="" />
                  <span>Tools and use cases</span>
                </button>
              }
            >
              <ToolsAndUseCases items={additionalData.toolsAndUseCases} />
            </CollapsibleSidebar>
          )}
        </div>
      )}
    </div>
  );
};

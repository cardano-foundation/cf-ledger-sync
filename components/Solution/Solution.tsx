import classes from "./Solution.module.css";
import * as React from "react";
import Image from "next/image";
import ArrowSquareOutIcon from "@/components/icons/arrow-square-out.svg";
import { CollapsibleSidebar } from "@/components/CollapsibleSidebar/CollapsibleSidebar";
import { ProsAndCons } from "@/components/ProsAndCons/ProsAndCons";

interface SolutionProps {
  link?: string;
  name: string;
  pros?: string[];
  cons?: string[];
  description?: string;
}

export const Solution = ({
  link,
  name,
  pros,
  cons,
  description,
}: SolutionProps) => {
  return (
    <div className={classes.solutionWrapper}>
      <h1 className={classes.solutionName}>{name}</h1>

      <div className={classes.solutionContent}>
        {description && <p className={classes.solutionText}>{description}</p>}

        <div className={classes.solutionLinks}>
          {(pros || cons) && (
            <CollapsibleSidebar
              title={name}
              trigger={
                <button className={classes.solutionViewMore}>
                  View more ({name})
                </button>
              }
            >
              <ProsAndCons description={description} pros={pros} cons={cons} />
            </CollapsibleSidebar>
          )}

          {link && (
            <a
              href={link}
              target="_blank"
              rel="noopener noreferrer"
              className={classes.solutionLink}
            >
              <span className={classes.questionCircle}>Explore {name}</span>
              <Image priority src={ArrowSquareOutIcon} alt={`Go to ${link}`} />
            </a>
          )}
        </div>
      </div>
    </div>
  );
};

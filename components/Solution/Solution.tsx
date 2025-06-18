import classes from "./Solution.module.css";
import * as React from "react";
import Image from "next/image";
import ArrowSquareOutIcon from "@/components/icons/arrow-square-out.svg";

interface SolutionProps {
  link?: string;
  name: string;
  pros?: string;
  cons?: string;
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
        {pros && (
          <div>
            <h5 className={classes.solutionProsConsTitle}>Pros:</h5>
            <p className={classes.solutionText}>{pros}</p>
          </div>
        )}

        {cons && (
          <div>
            <h5 className={classes.solutionProsConsTitle}>Cons:</h5>
            <p className={classes.solutionText}>{cons}</p>
          </div>
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
  );
};

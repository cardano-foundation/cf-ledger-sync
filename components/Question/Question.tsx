import classes from "./Question.module.css";
import * as React from "react";
import Link from "next/link";

interface QuestionProps {
  text: string;
  link: string;
  isExternal?: boolean;
}

export const Question = ({ text, link, isExternal }: QuestionProps) => {
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
    </div>
  );
};

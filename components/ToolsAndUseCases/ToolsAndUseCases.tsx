import * as React from "react";
import classes from "./ToolsAndUseCases.module.css";

interface ProsAndConsProps {
  items: { title: string; text: string }[];
}

export const ToolsAndUseCases = ({ items }: ProsAndConsProps) => {
  return (
    <div className={classes.toolsAndUseCasesWrapper}>
      <div className={classes.toolsAndUseCases}>
        {items.map(({ title, text }) => (
          <div className={classes.toolsAndUseCasesItem} key={title}>
            <h3 className={classes.toolsAndUseCasesItemTitle}>{title}:</h3>
            <p className={classes.toolsAndUseCasesItemText}>{text}</p>
          </div>
        ))}
      </div>
    </div>
  );
};

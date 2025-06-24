import * as React from "react";
import classes from "./ListWithTitle.module.css";

interface ListWithTitleProps {
  title: string | React.ReactNode;
  items: string[];
}

export const ListWithTitle = ({ title, items }: ListWithTitleProps) => {
  return (
    <div className={classes.listWithTitle}>
      <h3 className={classes.listWithTitleTitle}>{title}</h3>

      <ul className={classes.listWithTitleList}>
        {items.map((item) => (
          <li key={`item-${item}`} className={classes.listWithTitleItem}>
            {item}
          </li>
        ))}
      </ul>
    </div>
  );
};

import * as React from "react";
import { ListWithTitle } from "@/components/ListWithTitle/ListWithTitle";
import classes from "./ProsAndCons.module.css";
import Image from "next/image";
import CheckIcon from "@/components/icons/check.svg";
import CrossIcon from "@/components/icons/cross.svg";
import ArrowSquareOutIcon from "@/components/icons/arrow-square-out.svg";

interface ProsAndConsProps {
  pros?: string[];
  cons?: string[];
  usuallyUsed?: string[];
  description?: string;
  link?: string;
  name?: string;
}

export const ProsAndCons = ({
  pros,
  cons,
  usuallyUsed,
  description,
  link,
  name,
}: ProsAndConsProps) => {
  return (
    <div className={classes.prosAndCons}>
      {pros && (
        <ListWithTitle
          title={
            <>
              <Image priority src={CheckIcon} alt="" />
              Pros:
            </>
          }
          items={pros}
        />
      )}

      {cons && (
        <ListWithTitle
          title={
            <>
              <Image priority src={CrossIcon} alt="" />
              Cons:
            </>
          }
          items={cons}
        />
      )}

      {usuallyUsed && (
        <ListWithTitle title="When this is usually used:" items={usuallyUsed} />
      )}

      {description && (
        <p className={classes.prosAndConsDescription}>{description}</p>
      )}

      {link && name && (
        <a
          href={link}
          target="_blank"
          rel="noopener noreferrer"
          className={classes.prosAndConsLink}
        >
          <span className={classes.questionCircle}>Explore {name}</span>
          <Image priority src={ArrowSquareOutIcon} alt="" />
        </a>
      )}
    </div>
  );
};

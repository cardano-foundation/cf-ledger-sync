import Image from "next/image";

import logoIcon from "@/components/icons/logo.svg";
import XIcon from "@/components/icons/x.svg";

import classes from "./NavigationHeader.module.css";

export const NavigationHeader = () => {
  return (
    <header className={classes.navigationHeaderWrapper}>
      <div className={classes.navigationHeaderContent}>
        <Image priority src={logoIcon} alt="Cardano Logo" />

        <button className={classes.navigationHeaderButton}>
          <Image priority src={XIcon} alt="Close Icon" />
        </button>
      </div>
    </header>
  );
};

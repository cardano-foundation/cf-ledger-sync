import Image from "next/image";

import logoIcon from "@/components/icons/logo.svg";
import XIcon from "@/components/icons/x.svg";

import classes from "./NavigationHeader.module.css";

interface NavigationHeaderProps {
  closeLink?: string;
}

export const NavigationHeader = ({ closeLink }: NavigationHeaderProps) => {
  return (
    <header className={classes.navigationHeaderWrapper}>
      <div className={classes.navigationHeaderContent}>
        <Image priority src={logoIcon} alt="Cardano Logo" />

        {closeLink && (
          <a href={closeLink} className={classes.navigationHeaderButton}>
            <Image priority src={XIcon} alt="Close Icon" />
          </a>
        )}
      </div>
    </header>
  );
};

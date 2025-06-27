import { ReactNode } from "react";
import Image from "next/image";

import gradientBlue from "@/components/icons/gradient-bg-blue.svg";
import gradientOrange from "@/components/icons/gradient-bg-double.svg";
import { NavigationHeader } from "@/components/NavigationHeader/NavigationHeader";

import classes from "./Layout.module.css";

interface LayoutProps {
  children: ReactNode;
  gradient: "orange" | "blue";
  closeLink?: string;
}

export const Layout = ({ children, gradient, closeLink }: LayoutProps) => {
  return (
    <div className={classes.layoutWrapper}>
      <NavigationHeader closeLink={closeLink} />
      <main className={classes.layoutContent}>{children}</main>

      <div className={classes.layoutGradient}>
        {gradient === "blue" && (
          <Image priority src={gradientBlue} alt="" />
        )}
        {gradient === "orange" && (
          <Image priority src={gradientOrange} alt="" />
        )}
      </div>
    </div>
  );
};

import { ReactNode } from "react";
import Image from "next/image";

import gradientBlue from "@/components/icons/gradient-bg-blue.svg";
import gradientOrange from "@/components/icons/gradient-bg-double.svg";
import { NavigationHeader } from "@/components/NavigationHeader/NavigationHeader";

import classes from "./Layout.module.css";

interface LayoutProps {
  children: ReactNode;
  gradient: "orange" | "blue";
}

export const Layout = ({ children, gradient }: LayoutProps) => {
  return (
    <div className={classes.layoutWrapper}>
      <NavigationHeader />
      <div className={classes.layoutContent}>{children}</div>

      <div className={classes.layoutGradient}>
        {gradient === "blue" && (
          <Image priority src={gradientBlue} alt="Gradient" />
        )}
        {gradient === "orange" && (
          <Image priority src={gradientOrange} alt="Gradient" />
        )}
      </div>
    </div>
  );
};

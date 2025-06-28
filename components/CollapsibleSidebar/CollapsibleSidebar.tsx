import {
  Root,
  Trigger,
  Portal,
  Overlay,
  Content,
  Title,
  Description,
  Close,
} from "@radix-ui/react-dialog";
import { clsx } from "clsx";
import classes from "./CollapsibleSidebar.module.css";
import Image from "next/image";
import XIcon from "@/components/icons/x.svg";

interface CollapsibleSidebarProps {
  trigger: React.ReactNode;
  children: React.ReactNode;
  title: string;
  description?: string;
  hasSmallPadding?: boolean;
}

export const CollapsibleSidebar = ({
  trigger,
  children,
  title,
  description,
  hasSmallPadding,
}: CollapsibleSidebarProps) => {
  return (
    <Root>
      <Trigger asChild>{trigger}</Trigger>
      <Portal>
        <Overlay className={classes.collapsibleSidebarOverlay} />
        <Content className={classes.collapsibleSidebarContent}>
          <div className={classes.collapsibleSidebarHeader}>
            <Title className={classes.collapsibleSidebarTitle}>{title}</Title>
            <Close asChild>
              <button
                aria-label="Close"
                className={classes.collapsibleSidebarCloseButton}
              >
                <Image priority src={XIcon} alt="Close Icon" />
              </button>
            </Close>
          </div>
          <div
            className={clsx(classes.collapsibleSidebarChildrenWrapper, {
              [classes.collapsibleSidebarChildrenWrapperSmallPadding]: hasSmallPadding,
            })}
          >
            <div className={classes.collapsibleSidebarChildren}>
              {description && (
                <Description className={classes.collapsibleSidebarDescription}>
                  {description}
                </Description>
              )}

              <div>{children}</div>
            </div>
          </div>
        </Content>
      </Portal>
    </Root>
  );
};

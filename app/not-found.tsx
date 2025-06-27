import { ErrorView } from "@/components/ErrorView/ErrorView";

export default function Custom404() {
  return (
    <ErrorView
      startOverLink={process?.env?.FIRST_QUESTION_URL}
      closeLink={process?.env?.CLOSE_URL}
    />
  );
}

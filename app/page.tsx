import { redirect } from "next/navigation";
import { ErrorView } from "@/components/ErrorView/ErrorView";

export default async function Home() {
  let redirectPath: string | undefined = undefined;

  try {
    //Rest of the code
    redirectPath = process?.env?.FIRST_QUESTION_URL;
  } catch (error) {
    console.error(error);
    //Rest of the code
    return <ErrorView closeLink={process?.env?.CLOSE_URL} />;
  } finally {
    //Clear resources
    if (redirectPath) redirect(redirectPath);
  }
}

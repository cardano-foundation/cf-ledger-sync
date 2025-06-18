import { redirect } from "next/navigation";

export default function Home() {
  redirect(process?.env?.FIRST_QUESTION_URL || "");
}

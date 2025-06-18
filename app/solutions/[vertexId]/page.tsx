import { loadDiagramData, parseVertexData } from "@/app/utils/loadDiagramData";

import {
  SolutionsView,
  SolutionType,
} from "@/components/SolutionsView/SolutionsView";
import fs from "fs";
import path from "path";
import yaml from "js-yaml";

export async function generateStaticParams() {
  const data = loadDiagramData();
  return data.vertices.map(([vertexId]) => ({ vertexId }));
}

export default async function Page({
  params,
}: {
  params: Promise<{ vertexId: string }>;
}) {
  const { vertexId } = await params;
  const data = parseVertexData(loadDiagramData());
  const currentQuestion = data.vertices.get(vertexId);

  if (
    !currentQuestion ||
    !currentQuestion.link ||
    currentQuestion.link.includes("https")
  ) {
    return <div>Question not found</div>;
  }

  const directoryPath = path.join(
    process.cwd(),
    ...currentQuestion?.link.split("/"),
  );
  const fileNames = fs.readdirSync(directoryPath);
  const solutions = fileNames.map((fileName) => {
    const filePath = path.join(directoryPath, fileName);
    const fileOutput = fs.readFileSync(filePath, "utf-8");
    return yaml.load(fileOutput);
  }) as SolutionType[];

  // If there are multiple edges, render them as answers
  return <SolutionsView solutions={solutions} />;
}

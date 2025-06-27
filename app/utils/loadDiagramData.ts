import fs from "fs";
import path from "path";

export type Edge = {
  classes: string[];
  end: string;
  id: string;
  interpolate: string;
  isUserDefinedId: boolean;
  labelType: string;
  length: number;
  start: string;
  stroke: string;
  text: string;
  type: string;
};

export type Vertex = {
  classes: string[];
  domId: string;
  id: string;
  labelType: string;
  props: Record<string, string>;
  styles: string[];
  text: string;
  type: string;
  link?: string;
};

export interface VertexWithLink extends Vertex {
  link: string;
}

type DiagramData = {
  edges: Edge[];
  vertices: [string, Vertex][];
};
export function loadDiagramData(): DiagramData {
  const filePath = path.join(
    process.cwd(),
    "public",
    "data",
    "generated-data.json",
  );
  const jsonData = fs.readFileSync(filePath, "utf-8");
  const data = JSON.parse(jsonData);
  return data as DiagramData;
}

type Diagram = {
  edges: Edge[];
  vertices: Map<string, Vertex>;
};
export const parseVertexData = (data: DiagramData): Diagram => {
  const edges = data.edges;
  const vertices = new Map<string, Vertex>();

  data.vertices.forEach(([id, vertex]) => {
    vertices.set(id, vertex);
  });

  return { edges, vertices };
};

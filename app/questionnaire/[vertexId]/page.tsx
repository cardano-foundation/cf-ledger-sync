import {
  loadDiagramData,
  parseVertexData,
  Vertex,
  VertexWithLink,
} from "@/app/utils/loadDiagramData";
import { JSX } from "react";
import { QuestionsView } from "@/components/QuestionsView/QuestionsView";
import { SolutionsView } from "@/components/SolutionsView/SolutionsView";
import { redirect } from "next/navigation";
import { ErrorView } from "@/components/ErrorView/ErrorView";

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
  if (!currentQuestion) {
    return <div>Question not found</div>;
  }
  const findEdges = (vertice?: Vertex) => {
    return data.edges.filter((edge) => {
      return edge.start === vertice?.id;
    });
  };

  const findPreviousEdges = (vertice?: Vertex) => {
    return data.edges.filter((edge) => {
      return edge.end === vertice?.id;
    });
  };

  const getPreviousQuestion = (currentVertex: Vertex) => {
    const connectedEdges = findPreviousEdges(currentVertex);
    const prevVertex = data.vertices.get(connectedEdges[0]?.start);

    const prevVertexConnectedEdges = findEdges(prevVertex);

    if (prevVertexConnectedEdges.length === 1 && prevVertex) {
      return getPreviousQuestion(prevVertex);
    }

    return prevVertex;
  };

  const renderQuestionChain = (
    question: Vertex | undefined,
    previousAnswer?: string,
  ): JSX.Element | null => {
    if (!question) {
      return (
        <ErrorView
          closeLink={process?.env?.CLOSE_URL}
          startOverLink={process?.env?.FIRST_QUESTION_URL}
        />
      );
    }

    const connectedEdges = findEdges(question);
    const nextVertex = data.vertices.get(connectedEdges[0]?.end);
    const previousQuestions = getPreviousQuestion(currentQuestion);

    // If there are no connected edges and a link render it as an endpoint with data from yaml
    if (question && question.link && !question.link.includes("https")) {
      return (
        <SolutionsView
          currentQuestion={question as VertexWithLink}
          prevQuestionId={previousQuestions?.id}
          closeLink={process?.env?.CLOSE_URL}
        />
      );
    }

    // If there's only one connected edge, render it as part of the QuestionsView
    if (connectedEdges.length === 1) {
      return renderQuestionChain(nextVertex, question.text);
    }

    // If this question has a link, render it as an external link
    if (question.link) {
      return redirect(question.link);
    }

    // If there are multiple edges, render them as answers
    return (
      <QuestionsView
        vertexes={data.vertices}
        answers={connectedEdges}
        question={question.text}
        previousAnswer={previousAnswer}
        prevQuestionId={previousQuestions?.id}
        closeLink={process?.env?.CLOSE_URL}
      />
    );
  };

  return renderQuestionChain(currentQuestion);
}

import * as React from "react";

import { Layout } from "@/components/Layout/Layout";
import { Edge, Vertex } from "@/app/utils/loadDiagramData";
import { Question } from "@/components/Question/Question";
import classes from "./QuestionsView.module.css";

interface QuestionsViewProps {
  question: string;
  answers: Edge[];
  vertexes: Map<string, Vertex>;
  previousAnswer?: string;
}

export const QuestionsView = ({
  question,
  vertexes,
  answers,
  previousAnswer,
}: QuestionsViewProps) => {
  return (
    <Layout gradient="blue">
      <div className={classes.questionsView}>
        {previousAnswer && (
          <h3 className={classes.questionsViewPreviousAnswer}>
            {previousAnswer}
          </h3>
        )}
        <h1 className={classes.questionsViewTitle}>{question}</h1>
        <div className={classes.questionsViewQuestions}>
          {answers.map((edge) => {
            const nextVertex = vertexes.get(edge.end);

            if (!nextVertex) {
              return null;
            }

            return nextVertex?.link ? (
              <Question
                key={`question-${nextVertex.id}`}
                text={edge.text || nextVertex.text}
                link={nextVertex.link}
                isExternal
                id={nextVertex.id}
              />
            ) : (
              <Question
                text={edge.text || nextVertex.text}
                link={`/questionnaire/${nextVertex?.id}`}
                id={nextVertex.id}
              />
            );
          })}
        </div>
      </div>
    </Layout>
  );
};

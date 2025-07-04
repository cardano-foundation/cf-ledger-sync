import * as React from "react";

import { Layout } from "@/components/Layout/Layout";
import { Edge, Vertex } from "@/app/utils/loadDiagramData";
import { Question } from "@/components/Question/Question";
import classes from "./QuestionsView.module.css";
import Link from "next/link";
import Image from "next/image";
import ArrowLeftIcon from "@/components/icons/arrow-left.svg";

interface QuestionsViewProps {
  question: string;
  answers: Edge[];
  vertexes: Map<string, Vertex>;
  previousAnswer?: string;
  prevQuestionId?: string;
  closeLink?: string;
}

export const QuestionsView = ({
  question,
  vertexes,
  answers,
  previousAnswer,
  prevQuestionId,
  closeLink,
}: QuestionsViewProps) => {
  return (
    <Layout gradient="blue" closeLink={closeLink}>
      <div className={classes.questionsViewWrapperWrapper}>
        <div className={classes.questionsViewWrapper}>
          {prevQuestionId && (
            <Link
              href={`/questionnaire/${prevQuestionId}`}
              className={classes.questionsViewBackButton}
            >
              <Image priority src={ArrowLeftIcon} alt="" />
              <span>Back</span>
            </Link>
          )}
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

                return nextVertex?.link &&
                  nextVertex?.link.includes("https") ? (
                  <Question
                    key={`question-${nextVertex.id}`}
                    text={edge.text || nextVertex.text}
                    link={nextVertex.link}
                    isExternal
                    id={nextVertex.id}
                  />
                ) : (
                  <Question
                    key={`question-${nextVertex.id}`}
                    text={edge.text || nextVertex.text}
                    link={`/questionnaire/${nextVertex?.id}`}
                    id={nextVertex.id}
                  />
                );
              })}
            </div>
          </div>
        </div>
      </div>
    </Layout>
  );
};

package dev.elearning.quiz.service;

import dev.elearning.quiz.model.Question;
import dev.elearning.quiz.model.Quiz;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuizEvaluationService {

    @Data
    @AllArgsConstructor
    public static class QuizEvaluation {
        private int score;
        private int correctAnswers;
        private int totalQuestions;
        private List<Integer> answers;
        private List<Boolean> correactAnswersList;
    }

    public QuizEvaluation evaluateQuiz(Quiz quiz, List<Integer> userAnswers) {
        List<Question> questions = quiz.getQuestions();
        int totalQuestions = questions.size();
        int correctAnswers = 0;
        List<Integer> answers = new ArrayList<>();
        List<Boolean> correactAnswersList = new ArrayList<>();

        for (int i = 0; i < questions.size(); i++) {
            Question question = questions.get(i);
            Integer userAnswer = (userAnswers != null && i < userAnswers.size()) ? userAnswers.get(i) : -1;
            answers.add(userAnswer);

            boolean isCorrect = userAnswer.equals(question.getCorrectAnswer());

            if (isCorrect) {
                correctAnswers++;
            }
            correactAnswersList.add(isCorrect);
        }

        int score = (totalQuestions > 0) ? (correctAnswers * 100) / totalQuestions : 0;

        return new QuizEvaluation(score, correctAnswers, totalQuestions, answers, correactAnswersList);
    }
}

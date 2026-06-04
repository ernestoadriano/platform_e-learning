package dev.elearning.quiz.service;

import dev.elearning.quiz.dto.request.QuestionRequest;
import dev.elearning.quiz.dto.request.QuizRequest;
import dev.elearning.quiz.dto.request.QuizSubmitRequest;
import dev.elearning.quiz.dto.response.*;
import dev.elearning.quiz.model.*;
import dev.elearning.quiz.repository.OptionRepository;
import dev.elearning.quiz.repository.QuestionRepository;
import dev.elearning.quiz.repository.QuizAttemptRepository;
import dev.elearning.quiz.repository.QuizRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class QuizService {

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private OptionRepository optionRepository;

    @Autowired
    private QuizAttemptRepository attemptRepository;

    @Autowired
    private QuizEvaluationService evaluationService;

    public QuizResponse getQuizLessonId(Long lessonId) {
        Quiz quiz = quizRepository.findByLessonId(lessonId)
                .orElseThrow(() -> new RuntimeException("Quiz not found for lesson: " + lessonId));
        return toResponse(quiz);
    }

    public QuizWithQuestionsResponse getQuizWithQuestion(Long lessonId) {
        Quiz quiz = getQuizByLessonId(lessonId);

        QuizWithQuestionsResponse response = new QuizWithQuestionsResponse();
        response.setId(quiz.getId());
        response.setTitle(quiz.getTitle());
        response.setDescription(quiz.getDescription());
        response.setPassingScore(quiz.getPassingScore());
        response.setTimeLimitMinutes(quiz.getTimeLimitMinutes());

        List<QuestionResponse> questions = quiz.getQuestions().stream()
                .map(this::toQuestionResponse)
                .toList();

        response.setQuestions(questions);

        return response;
    }

    public List<QuizAttemptResponse> getUserAttempts(Long userId, Long lessonId) {
        return attemptRepository.findAllByUserIdAndLessonId(userId, lessonId).stream()
                .map(this::toAttemptResponse)
                .toList();
    }

    public boolean hasUserPassedQuiz(Long userId, Long lessonId) {
        Quiz quiz = quizRepository.findByLessonId(lessonId)
                .orElseThrow(() -> new RuntimeException("Quiz not found"));
        return attemptRepository.existsByUserIdAndQuizIdAndPassedTrue(userId, lessonId);
    }

    public int getRemainingAttempts(Long userId, Long lessonId) {
        Quiz quiz = getQuizByLessonId(lessonId);
        long attemptsCount = attemptRepository.countByUserIdAndQuizId(userId, lessonId);

        return Math.max(0, quiz.getMaxAttempts() - (int) attemptsCount);
    }

    @Transactional
    public QuizResultResponse submitQuiz(Long userId, Long lessonId, QuizSubmitRequest request) {
        Quiz quiz = getQuizByLessonId(lessonId);

        if (getRemainingAttempts(userId, lessonId) <= 0) {
            throw new RuntimeException("No attempts remaning for this quiz");
        }

        QuizEvaluationService.QuizEvaluation evaluation = evaluationService.evaluateQuiz(quiz, request.getAnswers());

        int attemptNumber = (int) attemptRepository.countByUserIdAndQuizId(userId, quiz.getId()) + 1;

        QuizAttempt attempt = new QuizAttempt();
        attempt.setUserId(userId);
        attempt.setQuiz(quiz);
        attempt.setAttemptNumber(attemptNumber);
        attempt.setScore(evaluation.getScore());
        attempt.setPassed(evaluation.getScore() >= quiz.getPassingScore());
        attempt.setCompletedAt(LocalDateTime.now());


        List<Answer> answers = new ArrayList<>();
        for (int i = 0; i < evaluation.getAnswers().size(); i++) {
            Answer answer = new Answer();
            answer.setAttempt(attempt);
            answer.setQuestion(quiz.getQuestions().get(i));
            answer.setSelectedOption(evaluation.getAnswers().get(i));
            answer.setIsCorrect(evaluation.getAnswers().get(i).equals(quiz.getQuestions().get(i).getCorrectAnswer()));
            answers.add(answer);
        }

        attempt.setAnswers(answers);
        attempt = attemptRepository.save(attempt);

        QuizResultResponse response = new QuizResultResponse();
        response.setAttemptId(attempt.getId());
        response.setAttemptNumber(attemptNumber);
        response.setScore(evaluation.getScore());
        response.setPassed(attempt.getPassed());
        response.setTotalQuestions(evaluation.getTotalQuestions());
        response.setMessage(attempt.getPassed() ?
                "Congratulations! You passed the quiz!" :
                "You need " + quiz.getPassingScore() + "% to pass. Try again!");
        response.setAnswers(evaluation.getAnswers());
        response.setCorrectAnswersList(evaluation.getCorreactAnswersList());

        return response;
    }

    @Transactional
    public QuizResponse createQuiz(QuizRequest request) {
        if (quizRepository.existsByLessonId(request.getLessonId())) {
            throw new RuntimeException("Quiz already exists for this lesson");
        }

        Quiz quiz = new Quiz();
        quiz.setTitle(request.getTitle());
        quiz.setDescription(request.getDescription());
        quiz.setLessonId(request.getLessonId());
        quiz.setPassingScore(request.getPassingScore() != null ? request.getPassingScore() : 70);
        quiz.setTimeLimitMinutes(request.getTimeLimitMinutes());
        quiz.setMaxAttempts(request.getMaxAttempts() != null ? request.getMaxAttempts() : 3);

        if (request.getQuestions() != null) {
            for (int i = 0; i < request.getQuestions().size(); i++) {
                QuestionRequest qReq = request.getQuestions().get(i);
                Question question = new Question();
                question.setText(qReq.getText());
                question.setQuiz(quiz);
                question.setQuestionOrder(i);
                question.setCorrectAnswer(qReq.getCorreactAnswer());
                question.setExplanation(qReq.getExplanation());

                List<Option> options = new ArrayList<>();
                for (int j = 0; j < qReq.getOptions().size(); j++) {
                    Option option = new Option();
                    option.setText(qReq.getOptions().get(j));
                    option.setOptionOrder(j);
                    option.setQuestion(question);
                    options.add(option);
                }
                question.setOptions(options);
                quiz.getQuestions().add(question);
            }
        }
        quiz = quizRepository.save(quiz);

        return toResponse(quiz);
    }

    private Quiz getQuizByLessonId(Long lessonId){
        return quizRepository.findByLessonId(lessonId)
                .orElseThrow(() -> new RuntimeException("Quiz not found lesson: " + lessonId));
    }

    private QuizResponse toResponse(Quiz quiz) {
        QuizResponse response = new QuizResponse();
        response.setId(quiz.getId());
        response.setTitle(quiz.getTitle());
        response.setDescription(quiz.getDescription());
        response.setLessonId(quiz.getLessonId());
        response.setPassingScore(quiz.getPassingScore());
        response.setMaxAttempts(quiz.getMaxAttempts());
        return response;
    }

    private QuestionResponse toQuestionResponse(Question question) {
        QuestionResponse response = new QuestionResponse();
        response.setId(question.getId());
        response.setText(question.getText());
        response.setQuestionOrder(question.getQuestionOrder());

        List<String> options = question.getOptions().stream()
                .map(Option::getText)
                .toList();
        response.setOptions(options);

        return response;
    }

    private QuizAttemptResponse toAttemptResponse(QuizAttempt attempt) {
        QuizAttemptResponse response = new QuizAttemptResponse();
        response.setId(attempt.getId());
        response.setAttemptNumber(attempt.getAttemptNumber());
        response.setScore(attempt.getScore());
        response.setPassed(attempt.getPassed());
        response.setCompletedAt(attempt.getCompletedAt());
        return response;
    }
}

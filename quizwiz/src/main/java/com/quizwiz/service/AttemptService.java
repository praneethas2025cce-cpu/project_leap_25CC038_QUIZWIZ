package com.quizwiz.service;

import com.quizwiz.entity.Attempt;
import com.quizwiz.entity.Question;
import com.quizwiz.entity.Quiz;
import com.quizwiz.entity.Student;
import com.quizwiz.repository.AttemptRepository;
import com.quizwiz.repository.QuestionRepository;
import com.quizwiz.repository.QuizRepository;
import com.quizwiz.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class AttemptService {

    private final AttemptRepository attemptRepository;
    private final StudentRepository studentRepository;
    private final QuizRepository quizRepository;
    private final QuestionRepository questionRepository;

    public AttemptService(
            AttemptRepository attemptRepository,
            StudentRepository studentRepository,
            QuizRepository quizRepository,
            QuestionRepository questionRepository) {

        this.attemptRepository = attemptRepository;
        this.studentRepository = studentRepository;
        this.quizRepository = quizRepository;
        this.questionRepository = questionRepository;
    }

    public Attempt startAttempt(Long studentId, Long quizId) {

        if (attemptRepository.findByStudentIdAndQuizId(studentId, quizId).isPresent()) {
            throw new RuntimeException(
                    "Student has already attempted this quiz"
            );
        }

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() ->
                        new RuntimeException("Student not found"));

        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() ->
                        new RuntimeException("Quiz not found"));

        Attempt attempt = new Attempt(
                student,
                quiz,
                LocalDateTime.now()
        );

        return attemptRepository.save(attempt);
    }

    public Attempt submitAttempt(
            Long attemptId,
            Map<Long, String> answers) {

        Attempt attempt = attemptRepository.findById(attemptId)
                .orElseThrow(() ->
                        new RuntimeException("Attempt not found"));

        if (attempt.isSubmitted()) {
            throw new RuntimeException(
                    "Quiz has already been submitted"
            );
        }

        LocalDateTime now = LocalDateTime.now();

        long elapsedSeconds = Duration.between(
                attempt.getStartedAt(),
                now
        ).getSeconds();

        int score = 0;

        if (elapsedSeconds <= attempt.getQuiz().getTimeLimit() * 60L) {
            List<Question> questions =
                    questionRepository.findByQuizId(
                            attempt.getQuiz().getId()
                    );

            if (answers != null) {
                for (Question question : questions) {

                    String submittedAnswer =
                            answers.get(question.getId());

                    if (submittedAnswer != null &&
                            submittedAnswer.equalsIgnoreCase(
                                    question.getCorrectOption()
                            )) {

                        score++;
                    }
                }
            }
        }

        attempt.setScore(score);

        if (answers != null) {
            attempt.setAnswers(answers.toString());
        }

        attempt.setSubmittedAt(now);
        attempt.setSubmitted(true);

        return attemptRepository.save(attempt);
    }
    public List<Attempt> getAttemptsByQuizId(Long quizId) {

        quizRepository.findById(quizId)
                .orElseThrow(() -> new RuntimeException("Quiz not found"));

        return attemptRepository.findByQuizId(quizId);
    }
}
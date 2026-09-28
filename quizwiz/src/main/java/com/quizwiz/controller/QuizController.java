package com.quizwiz.controller;

import com.quizwiz.entity.Quiz;
import com.quizwiz.service.QuizService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/quizzes")
public class QuizController {

    private final QuizService quizService;

    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    @PostMapping
    public ResponseEntity<Quiz> createQuiz(
            @jakarta.validation.Valid @RequestBody Quiz quiz) {

        return ResponseEntity.ok(
                quizService.createQuiz(quiz)
        );
    }

    @GetMapping
    public ResponseEntity<List<Quiz>> getAllQuizzes() {

        return ResponseEntity.ok(
                quizService.getAllQuizzes()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Quiz> getQuizById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                quizService.getQuizById(id)
        );
    }
}

package com.quizwiz.controller;

import com.quizwiz.entity.Question;
import com.quizwiz.service.QuestionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/questions")
public class QuestionController {

    private final QuestionService questionService;

    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @PostMapping
    public ResponseEntity<Question> createQuestion(
            @jakarta.validation.Valid @RequestBody Question question) {

        return ResponseEntity.ok(
                questionService.createQuestion(question)
        );
    }

    @GetMapping
    public ResponseEntity<List<Question>> getAllQuestions() {

        return ResponseEntity.ok(
                questionService.getAllQuestions()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Question> getQuestionById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                questionService.getQuestionById(id)
        );
    }

    @GetMapping("/quiz/{quizId}")
    public ResponseEntity<List<Question>> getQuestionsByQuizId(
            @PathVariable Long quizId) {

        return ResponseEntity.ok(
                questionService.getQuestionsByQuizId(quizId)
        );
    }
}
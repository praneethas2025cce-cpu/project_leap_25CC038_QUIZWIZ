package com.quizwiz.controller;

import com.quizwiz.entity.Attempt;
import com.quizwiz.service.AttemptService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/attempts")
public class AttemptController {

    private final AttemptService attemptService;

    public AttemptController(AttemptService attemptService) {
        this.attemptService = attemptService;
    }

    @PostMapping("/start")
    public ResponseEntity<Attempt> startAttempt(
            @RequestParam Long studentId,
            @RequestParam Long quizId) {

        return ResponseEntity.ok(
                attemptService.startAttempt(studentId, quizId)
        );
    }

    @PostMapping("/{attemptId}/submit")
    public ResponseEntity<Attempt> submitAttempt(
            @PathVariable Long attemptId,
            @RequestBody Map<Long, String> answers) {

        return ResponseEntity.ok(
                attemptService.submitAttempt(attemptId, answers)
        );
    }
    @GetMapping("/quiz/{quizId}")
    public ResponseEntity<?> getAttemptsByQuizId(
            @PathVariable Long quizId) {

        return ResponseEntity.ok(
                attemptService.getAttemptsByQuizId(quizId)
        );
    }
}
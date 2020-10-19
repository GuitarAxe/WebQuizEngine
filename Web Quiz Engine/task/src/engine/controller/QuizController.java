package engine.controller;

import engine.entity.Answer;
import engine.entity.Quiz;
import engine.entity.QuizStatus;
import engine.model.User;
import engine.service.QuizService;
import engine.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/quizzes")
public class QuizController {
    private final QuizService quizService;
    private final UserService userService;

    @Autowired
    public QuizController(QuizService quizService, UserService userService) {
        this.quizService = quizService;
        this.userService = userService;
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Quiz> getQuizById(@PathVariable long id) {
        Optional<Quiz> quiz = quizService.getQuizById(id);
        return ResponseEntity.of(quiz);
    }

    @GetMapping
    public ResponseEntity<List<Quiz>> getAll() {
        return ResponseEntity.ok(quizService.getAll());
    }

    @PostMapping(consumes = "application/json")
    public ResponseEntity<Quiz> create(@Valid @RequestBody Quiz quiz) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetails userDetails = (UserDetails) principal;
        String email = userDetails.getUsername();
        User user = userService.findByEmail(email);
        quiz.setUser(user);
        return ResponseEntity.ok(quizService.create(quiz));
    }

    @PostMapping(path = "/{id}/solve")
    public ResponseEntity<QuizStatus> solve(@PathVariable long id, @RequestBody Answer answer) {
        return ResponseEntity.of(quizService.solve(answer, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetails userDetails = (UserDetails) principal;
        String email = userDetails.getUsername();
        User user = userService.findByEmail(email);
        quizService.delete(id, user);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
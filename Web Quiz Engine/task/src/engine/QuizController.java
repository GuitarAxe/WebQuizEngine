package engine;

import org.springframework.web.bind.annotation.*;

@RestController
public class QuizController {
    private ResponseStatus success = new ResponseStatus(true,
            "Congratulations, you're right!");
    private ResponseStatus fail = new ResponseStatus(false,
            "Wrong answer! Please, try again.");

    private String options[] = {"Robot", "Tea leaf", "Cup of coffee", "Bug"};
    private Quiz quiz = new Quiz("The Java Logo",
            "What is depicted on the Java logo?",
            options
    );

    public QuizController() {
    }

    @GetMapping(path = "/api/quiz")
    public Quiz getQuiz() {
        return quiz;
    }

    @PostMapping(path = "/api/quiz")
    public ResponseStatus processAnswer(@RequestParam("answer") int answer) {
        if (answer == 2) {
            return success;
        }
        return fail;
    }
}

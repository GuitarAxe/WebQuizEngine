package engine.service;

import engine.entity.Answer;
import engine.entity.Quiz;
import engine.entity.QuizStatus;
import engine.model.User;
import engine.repository.QuizJpaRepository;
import engine.validation.QuizNotExistsException;
import engine.validation.UserNotAuthorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class QuizService {
    private final QuizJpaRepository quizRepository;

    @Autowired
    public QuizService(QuizJpaRepository quizRepository) {
        this.quizRepository = quizRepository;
    }

    public Quiz create(Quiz quiz) {
        return quizRepository.save(quiz);
    }

    public Optional<Quiz> getQuizById(long id) {
        return quizRepository.findById(id);
    }

    public List<Quiz> getAll() {
        return quizRepository.findAll();
    }

    public Optional<QuizStatus> solve(Answer answer, long quizId) {
        Optional<Quiz> quizOptional = getQuizById(quizId);
        if (quizOptional.isEmpty()) {
            return Optional.empty();
        }
        List<Integer> correctAnswer = new ArrayList<>(quizOptional.get().getAnswer());
        if (answer == null) {
            answer = new Answer();
        }
        List<Integer> userAnswer = answer.getAnswer();
        Collections.sort(correctAnswer);
        Collections.sort(userAnswer);
        return Optional.of(
                new QuizStatus(
                        correctAnswer.equals(userAnswer)
                ));
    }

    public void delete(long id, User user) {
        Quiz quiz = quizRepository.findById(id)
                .orElseThrow(() -> new QuizNotExistsException(id));
        User temp = quiz.getUser();
        if (!temp.equals(user)) {
            throw new UserNotAuthorException(user.getEmail());
        }
        quizRepository.deleteById(id);
    }
}
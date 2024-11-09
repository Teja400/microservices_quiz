package com.telusko.quiz_service.service;


import com.telusko.quiz_service.dao.QuizDao;
import com.telusko.quiz_service.feign.QuizInterface;
import com.telusko.quiz_service.model.QuestionWrapper;
import com.telusko.quiz_service.model.Quiz;
import com.telusko.quiz_service.model.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import java.util.List;


@Service
public class QuizService {
    @Autowired
    QuizDao quizDao;

    @Autowired
    QuizInterface quizInterface;

    public ResponseEntity<String> createQuiz(String categoryName, int numQ, String title) {

//        List<Integer> questions = call the generate url -Rest Template http://localhost:8080/question/generate ;
        List<Integer> questions = quizInterface.getQuestionsForQuiz(categoryName, numQ).getBody();
        Quiz quiz = new Quiz();
        quiz.setTitle(title);
        quiz.setQuestionIds(questions);
        quizDao.save(quiz);
        return new ResponseEntity<>("success", HttpStatus.CREATED);
    }

    public ResponseEntity<List<QuestionWrapper>> getQuizQuestions(Integer id) {
        Quiz quiz =quizDao.findById(id).get();
        List<Integer> questionIds = quiz.getQuestionIds();
        ResponseEntity<List<QuestionWrapper>> questionForUser = quizInterface.getQuestionsFromId(questionIds);

        return questionForUser;
    }

    public ResponseEntity<Integer> calculateResult(Integer id, List<Response> responses) {
       ResponseEntity<Integer> score = quizInterface.calculateResult(responses);
        return score;
    }
}

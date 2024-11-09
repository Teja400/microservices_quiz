package com.telusko.question_service.service;

import com.telusko.question_service.model.Question;
import com.telusko.question_service.dao.QuestionDao;
import com.telusko.question_service.model.QuestionWrapper;
import com.telusko.question_service.model.Response;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionService {

    @Autowired
    QuestionDao questionDao;

    public ResponseEntity<List<Question>> getAllQuestions() {
        try {
            return new ResponseEntity<>(questionDao.findAll(), HttpStatus.OK);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
    }


    public ResponseEntity<List<Question>> getQuestionsByCategory(String category) {
        try {
            return new ResponseEntity<>(questionDao.findByCategory(category), HttpStatus.OK);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<String> addQuestion(Question question) {
        try {
            questionDao.save(question);
            return new ResponseEntity<>("success", HttpStatus.CREATED);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>("failed", HttpStatus.BAD_REQUEST);

    }

    public ResponseEntity<Question> updateQuestion(Integer id, Question updatedQuestion) {
        try {
            Question existingQuestion = questionDao.findById(id).orElse(new Question());
            BeanUtils.copyProperties(updatedQuestion, existingQuestion, "id");
            return new ResponseEntity<>(questionDao.save(existingQuestion), HttpStatus.OK);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(new Question(), HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<String> deleteQuestion(Integer id) {
        try {
            questionDao.deleteById(id);
            return new ResponseEntity<>("deleted", HttpStatus.OK);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>("falied", HttpStatus.BAD_REQUEST);

    }

    public ResponseEntity<List<Integer>> getQuestionsForQuiz(String categoryName, Integer numQ) {
        List<Integer> questions = questionDao.findRandomQuestionsByCategory(categoryName, numQ);
        return new ResponseEntity<>(questions, HttpStatus.OK);
    }

    public ResponseEntity<List<QuestionWrapper>> getQuestionsFromId(List<Integer> questionIds) {
        List<QuestionWrapper> questionWrappers = new ArrayList<>();
        List<Question> questions = new ArrayList<>();

        for(Integer id:questionIds){
             questions.add(questionDao.findById(id).get());

        }

        for(Question q: questions){
            QuestionWrapper questionWrapper = new QuestionWrapper(q.getId(), q.getQuestionTitle(),q.getOption1(),q.getOption2(), q.getOption3(),q.getOption4());
            questionWrappers.add(questionWrapper);
        }

        return new ResponseEntity<>(questionWrappers, HttpStatus.OK);
    }

    public ResponseEntity<Integer> calculateResult(List<Response> responses) {
        int correct=0;
        for(Response response:responses){
            Question question = questionDao.findById(response.getId()).get();
            if(response.getResponse().equals(question.getRightAnswer()))
                correct++;
        }
        return new ResponseEntity<>(correct, HttpStatus.OK);

    }

}

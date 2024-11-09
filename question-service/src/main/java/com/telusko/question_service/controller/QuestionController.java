package com.telusko.question_service.controller;

import com.telusko.question_service.model.Question;
import com.telusko.question_service.model.QuestionWrapper;
import com.telusko.question_service.model.Response;
import com.telusko.question_service.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/question")
public class QuestionController {

    @Autowired
    QuestionService questionService;

    @GetMapping("/getAllQuestions")
    public ResponseEntity<List<Question>> getAllQuestions(){
        return questionService.getAllQuestions();
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<Question>> getQuestionsByCategory(@PathVariable String category){
        return questionService.getQuestionsByCategory(category);
    }

    @PostMapping("/add")
    public ResponseEntity<String> addQuestion(@RequestBody Question question){
        return questionService.addQuestion(question);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Question> updateQuestion(@PathVariable Integer id, @RequestBody Question question){
        return questionService.updateQuestion(id, question);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteQuestion(@PathVariable Integer id){
        return questionService.deleteQuestion(id);
    }
    // generate
    // getQuestions (questionid)
    // getScore

    @GetMapping("/generate")
    public ResponseEntity<List<Integer>> getQuestionsForQuiz(
            @RequestParam String categoryName, @RequestParam Integer numQ
    ){
        return questionService.getQuestionsForQuiz(categoryName, numQ);
    }
    @PostMapping("/getQuestions")
    public ResponseEntity<List<QuestionWrapper>> getQuestionsFromId(@RequestBody List<Integer> questionIds){
        return questionService.getQuestionsFromId(questionIds);
    }

    @PostMapping("/submit")
    public ResponseEntity<Integer> calculateResult(@RequestBody List<Response> responses){
        return questionService.calculateResult(responses);
    }



}

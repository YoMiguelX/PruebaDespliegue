package com.example.demo.Controller;

import com.example.demo.Model.OpenTriviaResponse;
import com.example.demo.Services.OpenTriviaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class QuizController {


    private final OpenTriviaService triviaService;

    public QuizController(OpenTriviaService triviaService) {
        this.triviaService = triviaService;
    }

    @GetMapping("/quiz")
    public String quiz(@RequestParam(defaultValue = "18") int categoria, Model model) {

        OpenTriviaResponse response = triviaService.obtenerPreguntas(categoria);

        model.addAttribute("preguntas", response.getResults());

        return "quiz";
    }
}

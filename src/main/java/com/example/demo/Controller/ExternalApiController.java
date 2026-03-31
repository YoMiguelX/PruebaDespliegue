package com.example.demo.Controller;

import com.example.demo.Model.OpenTriviaResponse;
import com.example.demo.Services.OpenTriviaService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ExternalApiController {

    private final OpenTriviaService openTriviaService;

    public ExternalApiController(OpenTriviaService openTriviaService) {
        this.openTriviaService = openTriviaService;
    }

    @GetMapping("/api/external/trivia")
    public OpenTriviaResponse obtenerPreguntas(
            @RequestParam int categoria,
            @RequestParam(required = false, defaultValue = "easy") String dificultad) {

        return openTriviaService.obtenerPreguntas(categoria, dificultad);
    }
}
package com.example.demo.Services;

import com.example.demo.Model.OpenTriviaResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class OpenTriviaService {

    public OpenTriviaResponse obtenerPreguntas(int categoria, String dificultad) {

        // Open Trivia DB acepta: easy, medium, hard
        String difParam = "";
        if (dificultad != null && !dificultad.isBlank()) {
            difParam = "&difficulty=" + dificultad.trim().toLowerCase();
        }

        String url = "https://opentdb.com/api.php?amount=5"
                + "&category=" + categoria
                + difParam
                + "&type=multiple";

        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(url, OpenTriviaResponse.class);
    }

    // Mantener compatibilidad con cualquier llamada sin dificultad
    public OpenTriviaResponse obtenerPreguntas(int categoria) {
        return obtenerPreguntas(categoria, "easy");
    }
}
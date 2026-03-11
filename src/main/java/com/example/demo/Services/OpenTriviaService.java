package com.example.demo.Services;

import com.example.demo.Model.OpenTriviaResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class OpenTriviaService {

    public OpenTriviaResponse obtenerPreguntas(int categoria) {

        String url = "https://opentdb.com/api.php?amount=5&category="
                + categoria + "&type=multiple";

        RestTemplate restTemplate = new RestTemplate();

        return restTemplate.getForObject(url, OpenTriviaResponse.class);
    }
}
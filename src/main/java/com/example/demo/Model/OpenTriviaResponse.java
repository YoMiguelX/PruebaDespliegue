package com.example.demo.Model;

import com.example.demo.Model.OpenTriviaPregunta;

import java.util.List;

public class OpenTriviaResponse {

    private int response_code;
    private List<OpenTriviaPregunta> results;

    public OpenTriviaResponse() {}

    public int getResponse_code() {
        return response_code;
    }

    public void setResponse_code(int response_code) {
        this.response_code = response_code;
    }

    public List<OpenTriviaPregunta> getResults() {
        return results;
    }

    public void setResults(List<OpenTriviaPregunta> results) {
        this.results = results;
    }
}
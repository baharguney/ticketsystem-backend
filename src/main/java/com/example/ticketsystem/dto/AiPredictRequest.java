package com.example.ticketsystem.dto;

import java.util.List;

public class AiPredictRequest {
    private String text;
    private List<String> categories;

    public AiPredictRequest() {}

    public AiPredictRequest(String text, List<String> categories) {
        this.text = text;
        this.categories = categories;
    }


    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }
}
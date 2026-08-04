package com.example.ticketsystem.dto;

public class AiPredictResponse {
    private String predicted_category;
    private double confidence_score;

    public AiPredictResponse() {}

    public String getPredicted_category() { return predicted_category; }
    public void setPredicted_category(String predicted_category) { this.predicted_category = predicted_category; }

    public double getConfidence_score() { return confidence_score; }
    public void setConfidence_score(double confidence_score) { this.confidence_score = confidence_score; }
}
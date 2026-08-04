package com.example.ticketsystem.dto;

public class AudioPredictionResponse {
    private String transcribed_text;
    private String status;

    public AudioPredictionResponse() {
    }

    public AudioPredictionResponse(String transcribed_text, String status) {
        this.transcribed_text = transcribed_text;
        this.status = status;
    }

    public String getTranscribed_text() {
        return transcribed_text;
    }

    public void setTranscribed_text(String transcribed_text) {
        this.transcribed_text = transcribed_text;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
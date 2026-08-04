package com.example.ticketsystem.service;

import com.example.ticketsystem.model.Ticket;
import com.example.ticketsystem.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private CategoryService categoryService;

    private final RestTemplate restTemplate = new RestTemplate();
    private final String AI_SERVICE_TEXT_URL = "http://localhost:8000/predict";
    private final String AI_SERVICE_AUDIO_URL = "http://localhost:8000/predict-audio";

    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }

    public Ticket createTicket(Ticket ticket) {
        if (ticket == null) {
            ticket = new Ticket();
        }

        String description = (ticket.getDescription() != null) ? ticket.getDescription() : "";

        if (ticket.getTitle() == null || ticket.getTitle().trim().isEmpty()) {
            ticket.setTitle("Destek Talebi");
        }

        String predictedCategory = null;

        if (!description.trim().isEmpty()) {
            try {
                Map<String, String> request = new HashMap<>();
                request.put("text", description);

                Map response = restTemplate.postForObject(AI_SERVICE_TEXT_URL, request, Map.class);
                if (response != null && response.get("category") != null) {
                    predictedCategory = response.get("category").toString();
                }
            } catch (Exception e) {
                System.err.println("8000 portundaki AI servisine ulaşılamadı. Yerel kural motoru devreye giriyor: " + e.getMessage());
            }
        }

        if (predictedCategory == null || predictedCategory.trim().isEmpty() || predictedCategory.equalsIgnoreCase("Diğer")) {
            predictedCategory = categoryService.classifyTicket(description);
        }

        ticket.setDescription(description);
        ticket.setCategoryName(predictedCategory);

        return ticketRepository.save(ticket);
    }

    public Ticket createTicketFromAudio(MultipartFile file) {
        String transcript = "";
        String predictedCategory = null;

        if (file != null && !file.isEmpty()) {
            try {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.MULTIPART_FORM_DATA);

                MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
                ByteArrayResource fileResource = new ByteArrayResource(file.getBytes()) {
                    @Override
                    public String getFilename() {
                        return file.getOriginalFilename() != null ? file.getOriginalFilename() : "audio.wav";
                    }
                };
                body.add("file", fileResource);

                HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
                ResponseEntity<Map> response = restTemplate.postForEntity(AI_SERVICE_AUDIO_URL, requestEntity, Map.class);

                if (response.getBody() != null) {
                    if (response.getBody().get("transcript") != null) {
                        transcript = response.getBody().get("transcript").toString();
                    }
                    if (response.getBody().get("category") != null) {
                        predictedCategory = response.getBody().get("category").toString();
                    }
                }
            } catch (Exception e) {
                System.err.println("Ses AI Servisine ulaşılamadı: " + e.getMessage());
            }
        }

        if (predictedCategory == null || predictedCategory.trim().isEmpty() || predictedCategory.equalsIgnoreCase("Diğer")) {
            predictedCategory = categoryService.classifyTicket(transcript);
        }

        Ticket ticket = new Ticket();
        ticket.setTitle("Sesli Destek Talebi");
        ticket.setDescription(transcript.isEmpty() ? "Ses metne dönüştürülemedi." : transcript);
        ticket.setCategoryName(predictedCategory);

        return ticketRepository.save(ticket);
    }

    public void deleteTicket(Long id) {
        ticketRepository.deleteById(id);
    }
}
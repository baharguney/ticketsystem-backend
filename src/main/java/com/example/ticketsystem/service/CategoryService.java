package com.example.ticketsystem.service;

import com.example.ticketsystem.model.Category;
import com.example.ticketsystem.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;



    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Category createCategory(Category category) {
        return categoryRepository.save(category);
    }




    public String classifyTicket(String text) {
        if (text == null || text.trim().isEmpty()) {
            return getValidCategoryName("Genel");
        }

        String lowerText = text.toLowerCase(Locale.forLanguageTag("tr-TR"));


        if (containsAny(lowerText, "ağ", "wifi", "wi-fi", "internet", "bağlantı", "kopuyor", "zaman aşımı")) {
            return getValidCategoryName("Ağ / İnternet Sorunları");
        }


        if (containsAny(lowerText, "kredi kartı", "ödeme", "fatura", "para", "bakiye", "kart")) {
            return getValidCategoryName("Ödeme ve Fatura");
        }


        if (containsAny(lowerText, "şifre", "parola", "oturum", "giriş yapamıyorum", "hesap")) {
            return getValidCategoryName("Hesap / Giriş Problemleri");
        }


        if (containsAny(lowerText, "hata", "bug", "kapanıyor", "çöküyor", "donuyor", "render")) {
            return getValidCategoryName("Yazılım Hatası (Bug)");
        }


        if (containsAny(lowerText, "güvenlik", "şüpheli", "yetkisiz", "ip")) {
            return getValidCategoryName("Güvenlik");
        }


        if (containsAny(lowerText, "donanım", "barkod", "yazıcı", "cihaz", "usb")) {
            return getValidCategoryName("Donanım Sorunu");
        }


        if (containsAny(lowerText, "satın alma", "lisans", "abonelik", "sipariş")) {
            return getValidCategoryName("Satın Alma / Lisans");
        }


        if (containsAny(lowerText, "istek", "talep", "özellik", "eklensin")) {
            return getValidCategoryName("Talep / İstek");
        }


        if (containsAny(lowerText, "destek", "teknik", "yardım")) {
            return getValidCategoryName("Teknik Destek");
        }


        return getValidCategoryName("Genel");
    }

    private boolean containsAny(String text, String... keywords) {
        for (String keyword : keywords) {
            if (text.contains(keyword)) {
                return true;
            }
        }
        return false;
    }


    private String getValidCategoryName(String preferredName) {
        if (categoryRepository != null) {
            return categoryRepository.findByName(preferredName)
                    .map(Category::getName)
                    .orElseGet(() -> categoryRepository.findAll().stream()
                            .findFirst()
                            .map(Category::getName)
                            .orElse(preferredName));
        }
        return preferredName;
    }
}
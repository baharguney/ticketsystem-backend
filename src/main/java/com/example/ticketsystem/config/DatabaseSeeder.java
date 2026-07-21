package com.example.ticketsystem.config;

import com.example.ticketsystem.model.Category;
import com.example.ticketsystem.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DatabaseSeeder implements CommandLineRunner {

    private final CategoryRepository categoryRepository;

    @Override
    public void run(String... args) throws Exception {

        if (categoryRepository.count() == 0) {
            List<Category> defaultCategories = Arrays.asList(
                    new Category(null, "Teknik Destek"),
                    new Category(null, "Yazılım Hatası (Bug)"),
                    new Category(null, "Donanım Sorunu"),
                    new Category(null, "Hesap / Giriş Problemleri"),
                    new Category(null, "Ağ / İnternet Sorunları"),
                    new Category(null, "Talep / İstek"),
                    new Category(null, "Satın Alma / Lisans"),
                    new Category(null, "Güvenlik"),
                    new Category(null, "Performans Sorunları"),
                    new Category(null, "Diğer")
            );

            categoryRepository.saveAll(defaultCategories);
            System.out.println(">> İstediğin 10 varsayılan kategori başarıyla MySQL'e eklendi!");
        }
    }
}
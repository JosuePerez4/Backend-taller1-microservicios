package com.example.etoms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main application class for ETOMS (Enterprise Task & Office Management System)
 * 
 * The API documentation is available at: http://localhost:8080/swagger-ui.html
 */
@SpringBootApplication
public class EtomsApplication {

    public static void main(String[] args) {
        SpringApplication.run(EtomsApplication.class, args);
    }
}

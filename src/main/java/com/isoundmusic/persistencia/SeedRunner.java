package com.isoundmusic.persistencia;

public class SeedRunner {
    public static void main(String[] args) {
        try {
            System.out.println("[SeedRunner] Iniciando seeding...");
            DataSeeder.seedDemoData();
            System.out.println("[SeedRunner] Seeding completado OK.");
        } catch (Exception e) {
            System.err.println("[SeedRunner] Error en seeding: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

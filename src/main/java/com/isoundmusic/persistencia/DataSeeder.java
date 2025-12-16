package com.isoundmusic.persistencia;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DataSeeder {

    public DataSeeder() {
    }

    public static void seedDemoData() throws SQLException {
        AgenteDB db = AgenteDB.getInstancia();

        // Migración: asegurar tipos correctos (desactivar FKs para drop seguro)
        db.ejecutarUpdate("SET FOREIGN_KEY_CHECKS=0");
        // Eliminar tablas legacy que puedan bloquear (nombres antiguos)
        db.ejecutarUpdate("DROP TABLE IF EXISTS grupo_miembro");
        db.ejecutarUpdate("DROP TABLE IF EXISTS amigos");
        // Eliminar las actuales para recrear con tipos correctos
        db.ejecutarUpdate("DROP TABLE IF EXISTS grupo_usuario");
        db.ejecutarUpdate("DROP TABLE IF EXISTS grupos");
        db.ejecutarUpdate("SET FOREIGN_KEY_CHECKS=1");

        // Crear tablas si no existen (IDs numéricos autoincrementales)
        db.ejecutarUpdate(
                "CREATE TABLE IF NOT EXISTS usuarios (id INT AUTO_INCREMENT PRIMARY KEY, nombre VARCHAR(100) NOT NULL UNIQUE)");
        db.ejecutarUpdate(
                "CREATE TABLE IF NOT EXISTS grupos (id INT AUTO_INCREMENT PRIMARY KEY, nombre VARCHAR(100) NOT NULL)");
        db.ejecutarUpdate(
                "CREATE TABLE IF NOT EXISTS grupo_usuario (grupo_id INT NOT NULL, usuario_id INT NOT NULL, PRIMARY KEY (grupo_id, usuario_id), FOREIGN KEY (grupo_id) REFERENCES grupos(id) ON DELETE CASCADE, FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE)");
        db.ejecutarUpdate(
                "CREATE TABLE IF NOT EXISTS canciones (id INT AUTO_INCREMENT PRIMARY KEY, titulo VARCHAR(150) NOT NULL, artista VARCHAR(120) NOT NULL)");
        db.ejecutarUpdate(
                "CREATE TABLE IF NOT EXISTS playlists (id INT AUTO_INCREMENT PRIMARY KEY, nombre VARCHAR(120) NOT NULL)");
        db.ejecutarUpdate(
                "CREATE TABLE IF NOT EXISTS playlist_cancion (playlist_id INT NOT NULL, cancion_id INT NOT NULL, PRIMARY KEY (playlist_id, cancion_id), FOREIGN KEY (playlist_id) REFERENCES playlists(id) ON DELETE CASCADE, FOREIGN KEY (cancion_id) REFERENCES canciones(id) ON DELETE CASCADE)");

        // Limpieza usando ON DELETE CASCADE
        log.info("Borrando datos previos con cascada");
        db.ejecutarUpdate("DELETE FROM playlists"); // cascada a playlist_cancion
        db.ejecutarUpdate("DELETE FROM grupos"); // cascada a grupo_usuario
        db.ejecutarUpdate("DELETE FROM canciones");
        db.ejecutarUpdate("DELETE FROM usuarios"); // cascada a grupo_usuario

        // Insertar usuarios
        List<String> usuarios = Arrays.asList("Ana", "Luis", "Marta", "Carlos", "Lucía", "Jorge", "Sofía", "Pedro",
                "Elena", "Raúl");
        for (String u : usuarios) {
            db.ejecutarUpdate("INSERT INTO usuarios(nombre) VALUES(?)", u);
        }

        // Insertar canciones (20)
        String[][] canciones = new String[][] {
                { "Sunrise", "Aurora" }, { "Blue Sky", "Horizon" }, { "Midnight Drive", "Neon" },
                { "Ocean Waves", "Seabreeze" }, { "Forest Walk", "Greenlight" },
                { "City Lights", "Downtown" }, { "Firefly", "Glow" }, { "Stardust", "Cosmos" },
                { "Heartbeat", "Pulse" }, { "Thunder Road", "Storm" },
                { "Golden Hour", "Amber" }, { "Moonlit", "Lunar" }, { "Afterglow", "Echo" }, { "Silhouettes", "Shade" },
                { "Daydream", "Clouds" },
                { "Echoes", "Reflex" }, { "Cascade", "Falls" }, { "Voyager", "Nova" }, { "Serenity", "Calm" },
                { "Rush", "Tempo" }
        };
        for (String[] c : canciones) {
            db.ejecutarUpdate("INSERT INTO canciones(titulo, artista) VALUES(?, ?)", c[0], c[1]);
        }

        // Playlists y asignación de canciones
        int favId = insertPlaylist(db, "Favoritas");
        int workoutId = insertPlaylist(db, "Workout");
        int relaxId = insertPlaylist(db, "Relax");

        // Añadir primeras 8 a Favoritas, siguientes 6 a Workout, últimas 6 a Relax
        for (int i = 1; i <= 8; i++)
            db.ejecutarUpdate("INSERT INTO playlist_cancion(playlist_id, cancion_id) VALUES(?, ?)", favId, i);
        for (int i = 9; i <= 14; i++)
            db.ejecutarUpdate("INSERT INTO playlist_cancion(playlist_id, cancion_id) VALUES(?, ?)", workoutId, i);
        for (int i = 15; i <= 20; i++)
            db.ejecutarUpdate("INSERT INTO playlist_cancion(playlist_id, cancion_id) VALUES(?, ?)", relaxId, i);

        // No se mantiene contador; se ciñe al diagrama sin ese campo

        log.info("Datos de prueba insertados");
    }

    private static int insertPlaylist(AgenteDB db, String nombre) throws SQLException {
        // Insert y recuperar ID autogenerado en la misma conexión
        return db.ejecutarInsertReturnId("INSERT INTO playlists(nombre) VALUES(?)", nombre);
    }
}

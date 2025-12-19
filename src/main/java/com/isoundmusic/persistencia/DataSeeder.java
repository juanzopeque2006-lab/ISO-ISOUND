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
                // Borrar todas las tablas del esquema y recrear con datos de prueba
                db.ejecutarUpdate("DROP TABLE IF EXISTS grupo_usuario");
                db.ejecutarUpdate("DROP TABLE IF EXISTS grupos");
                db.ejecutarUpdate("DROP TABLE IF EXISTS usuarios");
                db.ejecutarUpdate("DROP TABLE IF EXISTS playlist_cancion");
                db.ejecutarUpdate("DROP TABLE IF EXISTS canciones");
                db.ejecutarUpdate("DROP TABLE IF EXISTS playlists");
                db.ejecutarUpdate("SET FOREIGN_KEY_CHECKS=1");

                // Crear tablas si no existen (IDs numéricos autoincrementales)
                db.ejecutarUpdate(
                                "CREATE TABLE IF NOT EXISTS usuarios (id INT AUTO_INCREMENT PRIMARY KEY, nombre VARCHAR(100) NOT NULL UNIQUE)");
                db.ejecutarUpdate(
                                "CREATE TABLE IF NOT EXISTS grupos (id INT AUTO_INCREMENT PRIMARY KEY, nombre VARCHAR(100) NOT NULL)");
                db.ejecutarUpdate(
                                "CREATE TABLE IF NOT EXISTS grupo_usuario (grupo_id INT NOT NULL, usuario_id INT NOT NULL, PRIMARY KEY (grupo_id, usuario_id), FOREIGN KEY (grupo_id) REFERENCES grupos(id) ON DELETE CASCADE, FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE)");
                db.ejecutarUpdate(
                                "CREATE TABLE IF NOT EXISTS playlists (id INT AUTO_INCREMENT PRIMARY KEY, nombre VARCHAR(120) NOT NULL)");

                // Limpieza usando ON DELETE CASCADE
                log.info("Borrando datos previos con cascada");
                db.ejecutarUpdate("DELETE FROM playlists");
                db.ejecutarUpdate("DELETE FROM grupos"); // cascada a grupo_usuario
                db.ejecutarUpdate("DELETE FROM usuarios"); // cascada a grupo_usuario

                // Insertar usuarios
                List<String> usuarios = Arrays.asList("Ana", "Luis", "Marta", "Carlos", "Lucía", "Jorge", "Sofía",
                                "Pedro",
                                "Elena", "Raúl");
                for (String u : usuarios) {
                        db.ejecutarUpdate("INSERT INTO usuarios(nombre) VALUES(?)", u);
                }

                // Crear playlists vacías (sin asignar canciones)
                insertPlaylist(db, "Favoritas");
                insertPlaylist(db, "Workout");
                insertPlaylist(db, "Relax");

                // No se asignan canciones a las playlists en la creación

                log.info("Datos de prueba insertados");
        }

        private static int insertPlaylist(AgenteDB db, String nombre) throws SQLException {
                // Insert y recuperar ID autogenerado en la misma conexión
                return db.ejecutarInsertReturnId("INSERT INTO playlists(nombre) VALUES(?)", nombre);
        }
}

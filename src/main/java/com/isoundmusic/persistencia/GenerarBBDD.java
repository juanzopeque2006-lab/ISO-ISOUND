package com.isoundmusic.persistencia;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GenerarBBDD {

        private GenerarBBDD() {
        }

        public static void generarDatosDePrueba() throws Exception {
                AgenteDB db = AgenteDB.getAgente();

                // Migración: asegurar tipos correctos (desactivar FKs para drop seguro)
                db.update("SET FOREIGN_KEY_CHECKS=0");
                // Borrar todas las tablas del esquema y recrear con datos de prueba
                db.update("DROP TABLE IF EXISTS grupo_usuario");
                db.update("DROP TABLE IF EXISTS grupos");
                db.update("DROP TABLE IF EXISTS usuarios");
                db.update("DROP TABLE IF EXISTS playlists");
                db.update("SET FOREIGN_KEY_CHECKS=1");

                // Crear tablas si no existen (IDs numéricos autoincrementales)
                db.update(
                                "CREATE TABLE IF NOT EXISTS usuarios (id INT AUTO_INCREMENT PRIMARY KEY, nombre VARCHAR(100) NOT NULL UNIQUE)");
                db.update(
                                "CREATE TABLE IF NOT EXISTS grupos (id INT AUTO_INCREMENT PRIMARY KEY, nombre VARCHAR(100) NOT NULL)");
                db.update(
                                "CREATE TABLE IF NOT EXISTS grupo_usuario (grupo_id INT NOT NULL, usuario_id INT NOT NULL, PRIMARY KEY (grupo_id, usuario_id), FOREIGN KEY (grupo_id) REFERENCES grupos(id) ON DELETE CASCADE, FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE)");
                db.update(
                                "CREATE TABLE IF NOT EXISTS playlists (id INT AUTO_INCREMENT PRIMARY KEY, nombre VARCHAR(120) NOT NULL)");

                // Limpieza usando ON DELETE CASCADE
                log.info("Borrando datos previos con cascada");
                db.update("DELETE FROM playlists");
                db.update("DELETE FROM grupos"); // cascada a grupo_usuario
                db.update("DELETE FROM usuarios"); // cascada a grupo_usuario

                // Insertar usuarios (sentencias separadas usando insert existente)
                db.insert("INSERT INTO usuarios(nombre) VALUES('Ana')");
                db.insert("INSERT INTO usuarios(nombre) VALUES('Luis')");
                db.insert("INSERT INTO usuarios(nombre) VALUES('Marta')");
                db.insert("INSERT INTO usuarios(nombre) VALUES('Carlos')");
                db.insert("INSERT INTO usuarios(nombre) VALUES('Lucía')");
                db.insert("INSERT INTO usuarios(nombre) VALUES('Jorge')");
                db.insert("INSERT INTO usuarios(nombre) VALUES('Sofía')");
                db.insert("INSERT INTO usuarios(nombre) VALUES('Pedro')");
                db.insert("INSERT INTO usuarios(nombre) VALUES('Elena')");
                db.insert("INSERT INTO usuarios(nombre) VALUES('Raúl')");

                // Crear playlists vacías (sin asignar canciones) usando insert existente
                db.insert("INSERT INTO playlists(nombre) VALUES('Favoritas')");
                db.insert("INSERT INTO playlists(nombre) VALUES('Workout')");
                db.insert("INSERT INTO playlists(nombre) VALUES('Relax')");

                // No se asignan canciones a las playlists en la creación

                log.info("Datos de prueba insertados");
        }

}

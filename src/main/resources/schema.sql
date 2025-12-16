-- Esquema inicial para ISoundMusic (MySQL)

CREATE TABLE IF NOT EXISTS amigos (
  id INT AUTO_INCREMENT PRIMARY KEY,
  nombre VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS grupos (
  id VARCHAR(64) PRIMARY KEY,
  nombre VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS grupo_miembro (
  grupo_id VARCHAR(64) NOT NULL,
  amigo_id INT NOT NULL,
  PRIMARY KEY (grupo_id, amigo_id),
  CONSTRAINT fk_gm_grupo FOREIGN KEY (grupo_id) REFERENCES grupos(id) ON DELETE CASCADE,
  CONSTRAINT fk_gm_amigo FOREIGN KEY (amigo_id) REFERENCES amigos(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS playlists (
  id INT AUTO_INCREMENT PRIMARY KEY,
  nombre VARCHAR(120) NOT NULL,
  numero_canciones INT NOT NULL DEFAULT 0
);

-- Datos de ejemplo
INSERT INTO amigos (nombre) VALUES
  ('Ana'), ('Luis'), ('Marta'), ('Carlos'), ('Lucía')
ON DUPLICATE KEY UPDATE nombre = VALUES(nombre);

INSERT INTO playlists (nombre, numero_canciones) VALUES
  ('Favoritas', 24), ('Workout', 35), ('Relax', 18), ('Rock Hits', 50)
ON DUPLICATE KEY UPDATE numero_canciones = VALUES(numero_canciones);

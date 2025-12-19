CREATE TABLE IF NOT EXISTS usuarios (
  id INT AUTO_INCREMENT PRIMARY KEY,
  nombre VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS grupos (
  id INT AUTO_INCREMENT PRIMARY KEY,
  nombre VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS grupo_usuario (
  grupo_id INT NOT NULL,
  usuario_id INT NOT NULL,
  PRIMARY KEY (grupo_id, usuario_id),
  FOREIGN KEY (grupo_id) REFERENCES grupos(id) ON DELETE CASCADE,
  FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS playlists (
  id INT AUTO_INCREMENT PRIMARY KEY,
  nombre VARCHAR(120) NOT NULL
);

INSERT INTO usuarios (nombre) VALUES
  ('Ana'), ('Luis'), ('Marta'), ('Carlos'), ('Lucía')
ON DUPLICATE KEY UPDATE nombre = VALUES(nombre);

INSERT INTO playlists (nombre) VALUES
  ('Favoritas'), ('Workout'), ('Relax')
ON DUPLICATE KEY UPDATE nombre = VALUES(nombre);

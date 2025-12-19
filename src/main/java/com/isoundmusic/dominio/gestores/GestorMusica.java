package com.isoundmusic.dominio.gestores;

import java.util.ArrayList;
import java.util.List;

import com.isoundmusic.persistencia.AgenteDB;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GestorMusica {
    private final AgenteDB db = AgenteDB.getAgente();

    public List<String[]> listarPlaylists(String filtroNombre) {
        String q = filtroNombre == null ? "" : filtroNombre.trim();
        String like = "%" + q + "%";
        try {
            List<Object[]> rows = db.select(
                    "SELECT id, nombre FROM playlists WHERE nombre LIKE ? ORDER BY id", like);
            List<String[]> out = new ArrayList<>();
            for (Object[] r : rows) {
                out.add(new String[] { String.valueOf(r[0]), String.valueOf(r[1]) });
            }
            return out;
        } catch (Exception e) {
            log.error("Error listando playlists con filtro: {}", q, e);
            return new ArrayList<>();
        }
    }

    public int crearPlaylist(String nombre) throws Exception {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre de la playlist es obligatorio");
        }
        String n = nombre.trim();
        String esc = n.replace("'", "''");
        // Usar métodos existentes del agente: insert(sql) y select(sql, params)
        db.insert("INSERT INTO playlists(nombre) VALUES('" + esc + "')");
        List<Object[]> rows = db.select("SELECT id FROM playlists WHERE nombre = ? ORDER BY id DESC LIMIT 1", n);
        int id = rows.isEmpty() ? -1 : Integer.parseInt(String.valueOf(rows.get(0)[0]));
        log.info("Playlist creada: {} (id={})", nombre, id);
        return id;
    }
}

package com.isoundmusic.dominio.gestores;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.isoundmusic.persistencia.AgenteDB;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GestorMusica {
    private final AgenteDB db = AgenteDB.getInstancia();

    public List<String[]> listarPlaylists(String filtroNombre) {
        String q = filtroNombre == null ? "" : filtroNombre.trim();
        String like = "%" + q + "%";
        try {
            List<Object[]> rows = db.select(
                    "SELECT id, nombre FROM playlists WHERE nombre LIKE ? ORDER BY nombre", like);
            List<String[]> out = new ArrayList<>();
            for (Object[] r : rows) {
                out.add(new String[] { String.valueOf(r[0]), String.valueOf(r[1]) });
            }
            return out;
        } catch (SQLException e) {
            log.error("Error listando playlists con filtro: {}", q, e);
            return new ArrayList<>();
        }
    }
}

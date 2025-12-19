package com.isoundmusic.dominio.gestores;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.isoundmusic.dominio.clases.Grupo;
import com.isoundmusic.persistencia.AgenteDB;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GestorGrupo {
    private final AgenteDB db = AgenteDB.getAgente();

    public List<String[]> getAmigos() {

        try {
            List<Object[]> rows = db.select("SELECT id, nombre FROM usuarios  ORDER BY id");
            List<String[]> out = new ArrayList<>();
            for (Object[] r : rows) {
                out.add(new String[] { String.valueOf(r[0]), String.valueOf(r[1]) });
            }
            return out;
        } catch (SQLException | ClassNotFoundException e) {
            log.error("Error al obtener los amigos");
            return new ArrayList<>();
        }
    }

    public Grupo crearGrupo(String nombre, List<String> miembros) throws Exception {
        if (nombre == null || nombre.isBlank())
            throw new IllegalArgumentException("Nombre requerido");
        if (miembros == null || miembros.isEmpty())
            throw new IllegalArgumentException("Miembros requeridos");

        String n = nombre.trim();
        String escNombre = n.replace("'", "''");
        // Insert del grupo usando el método existente del agente
        db.insert("INSERT INTO grupos(nombre) VALUES('" + escNombre + "')");
        // Recuperar ID del grupo recién creado
        List<Object[]> rows = db.select("SELECT id FROM grupos WHERE nombre = ? ORDER BY id DESC LIMIT 1", n);
        int id = rows.isEmpty() ? -1 : Integer.parseInt(String.valueOf(rows.get(0)[0]));

        // Insertar miembros del grupo en una sola sentencia usando IN
        StringBuilder inClause = new StringBuilder();
        for (String mNombre : miembros) {
            String m = mNombre == null ? "" : mNombre.trim();
            if (m.isEmpty())
                continue;
            String esc = m.replace("'", "''");
            if (inClause.isEmpty())
                inClause.append(", ");
            inClause.append("'").append(esc).append("'");
        }
        if (inClause.isEmpty()) {
            db.insert("INSERT INTO grupo_usuario(grupo_id, usuario_id) SELECT " + id
                    + ", u.id FROM usuarios u WHERE u.nombre IN (" + inClause + ")");
        }
        log.info("Grupo creado: {} con {} miembros", nombre, miembros.size());
        return new Grupo(id, nombre, miembros);
    }
}

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

    public Grupo crearGrupo(String nombre, List<String> miembros) throws SQLException, ClassNotFoundException {
        if (nombre == null || nombre.isBlank())
            throw new IllegalArgumentException("Nombre requerido");
        if (miembros == null || miembros.isEmpty())
            throw new IllegalArgumentException("Miembros requeridos");

        int id = db.ejecutarInsertReturnId("INSERT INTO grupos(nombre) VALUES(?)", nombre);
        for (String mNombre : miembros) {
            db.ejecutarUpdate(
                    "INSERT INTO grupo_usuario(grupo_id, usuario_id) SELECT ?, u.id FROM usuarios u WHERE u.nombre = ?",
                    id, mNombre);
        }
        log.info("Grupo creado: {} con {} miembros", nombre, miembros.size());
        return new Grupo(id, nombre, miembros);
    }
}

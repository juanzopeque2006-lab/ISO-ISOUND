package com.isoundmusic.persistencia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AgenteDB {
    private static AgenteDB instancia;
    private Connection conexion;

    private final String url;
    private final String usuario;
    private final String password;

    private AgenteDB() {
        // Config por defecto: ajustar cuando se tenga la BBDD
        String host = System.getProperty("db.host", "localhost");
        String port = System.getProperty("db.port", "3306");
        String db = System.getProperty("db.name", "isoundmusic");
        this.url = "jdbc:mysql://" + host + ":" + port + "/" + db
                + "?serverTimezone=UTC";
        this.usuario = System.getProperty("db.user", "ISO");
        this.password = System.getProperty("db.pass", "Aula@2020");
    }

    public static synchronized AgenteDB getInstancia() {
        if (instancia == null)
            instancia = new AgenteDB();
        return instancia;
    }

    private void conectar() throws SQLException {
        if (conexion == null || conexion.isClosed()) {
            log.debug("Conectando a BBDD: {}", url);
            conexion = DriverManager.getConnection(url, usuario, password);
            log.debug("Conexión establecida");
        }
    }

    public void desconectar() {
        try {
            if (conexion != null && !conexion.isClosed())
                conexion.close();
        } catch (SQLException ignored) {
        }
    }

    public int ejecutarUpdate(String sql, Object... params) throws SQLException {
        conectar();
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++)
                ps.setObject(i + 1, params[i]);
            log.debug("UPDATE SQL: {} | params: {}", sql, Arrays.toString(params));
            int rows = ps.executeUpdate();
            return rows;
        } finally {
            desconectar();
        }
    }

    // Inserta y devuelve el ID autogenerado (columna auto_increment)
    public int ejecutarInsertReturnId(String sql, Object... params) throws SQLException {
        conectar();
        try (PreparedStatement ps = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            for (int i = 0; i < params.length; i++)
                ps.setObject(i + 1, params[i]);
            log.debug("INSERT SQL: {} | params: {}", sql, Arrays.toString(params));
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
                return -1;
            }
        } finally {
            desconectar();
        }
    }

    public List<Object[]> select(String sql, Object... params) throws SQLException {
        conectar();
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++)
                ps.setObject(i + 1, params[i]);
            log.debug("SELECT SQL: {} | params: {}", sql, Arrays.toString(params));
            try (ResultSet rs = ps.executeQuery()) {
                List<Object[]> out = new ArrayList<>();
                ResultSetMetaData md = rs.getMetaData();
                int cols = md.getColumnCount();
                while (rs.next()) {
                    Object[] row = new Object[cols];
                    for (int c = 1; c <= cols; c++)
                        row[c - 1] = rs.getObject(c);
                    out.add(row);
                }
                return out;
            }
        } finally {
            desconectar();
        }
    }
}

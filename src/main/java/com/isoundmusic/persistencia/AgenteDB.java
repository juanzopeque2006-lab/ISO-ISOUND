package com.isoundmusic.persistencia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AgenteDB {
    private static AgenteDB instancia;
    private Connection conexion;

    private final String url;
    private final String host;
    private final String port;
    private final String db;
    private final String usuario;
    private final String password;
    private boolean databaseEnsured = false;

    private AgenteDB() {
        // Config por defecto: ajustar cuando se tenga la BBDD
        this.host = System.getProperty("db.host", "localhost");
        this.port = System.getProperty("db.port", "3306");
        this.db = System.getProperty("db.name", "isoundmusic");
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
            // Asegurar que la base de datos exista antes de conectar al esquema
            ensureDatabaseExists();
            log.debug("Conectando a BBDD: {}", url);
            conexion = DriverManager.getConnection(url, usuario, password);
            log.debug("Conexión establecida");
        }
    }

    private synchronized void ensureDatabaseExists() throws SQLException {
        if (databaseEnsured)
            return;
        String baseUrl = "jdbc:mysql://" + host + ":" + port + "/?serverTimezone=UTC";
        log.debug("Verificando existencia de la base de datos '{}'", db);
        try (Connection c = DriverManager.getConnection(baseUrl, usuario, password);
                Statement st = c.createStatement()) {
            String create = "CREATE DATABASE IF NOT EXISTS `" + db
                    + "` DEFAULT CHARACTER SET utf8mb4";
            st.executeUpdate(create);
            log.info("Base de datos verificada/creada: {}", db);
            databaseEnsured = true;
        }
    }

    public void desconectar() {
        try {
            if (conexion != null && !conexion.isClosed())
                conexion.close();
        } catch (SQLException ignored) {
            log.warn("Error cerrando conexión", ignored);
        }
    }

    public int ejecutarUpdate(String sql, Object... params) throws SQLException {
        conectar();
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++)
                ps.setObject(i + 1, params[i]);
            log.debug("UPDATE SQL: {} | params: {}", sql, Arrays.toString(params));
            return ps.executeUpdate();
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

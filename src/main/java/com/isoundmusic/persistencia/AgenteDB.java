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
import java.util.Vector;

import com.isoundmusic.dominio.ConstantesDB;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AgenteDB {

    // instancia del agente
    protected static AgenteDB mInstancia = null;

    // Conexion con la base de datos
    protected static Connection mBD;

    // Identificador ODBC de la base de datos
    private static String url = ConstantesDB.CONNECTION_STRING + ConstantesDB.DBNAME;
    // poner puerto correcto, por defecto 3306 y el nombre del esquema de la base de
    // datos MySQL

    // Driver para conectar con bases de datos MySQL
    private static String driver = ConstantesDB.DRIVER;

    // Constructor (conexión diferida)
    private AgenteDB() {
    }

    // Implementacion del patron singleton
    // Este patron de diseño permite implementar clases de las cuales
    // solo existir una instancia
    // http://es.wikipedia.org/wiki/Singleton
    public static AgenteDB getAgente() {
        if (mInstancia == null) {
            mInstancia = new AgenteDB();
        }
        return mInstancia;
    }

    // Metodo para realizar la conexion a la base de datos
    private void conectar() throws SQLException, ClassNotFoundException {
        Class.forName(driver);
        mBD = DriverManager.getConnection(url, ConstantesDB.USER, ConstantesDB.PASS);
    }

    // Método para desconectar de la base de datos
    public void desconectar() throws SQLException {
        mBD.close();
    }

    // Método para realizar una insercion en la base de datos
    public int insert(String sql) throws SQLException, Exception {
        conectar();
        PreparedStatement stmt = mBD.prepareStatement(sql);
        int res = stmt.executeUpdate();
        stmt.close();
        desconectar();
        return res;
    }

    // Método para realizar una eliminacion en la base de datos
    public int delete(String sql) throws SQLException {
        PreparedStatement stmt = mBD.prepareStatement(sql);
        int res = stmt.executeUpdate();
        stmt.close();
        desconectar();

        return res;
    }

    // Método para realizar una eliminacion en la base de datos
    public int update(String sql) throws SQLException, ClassNotFoundException {
        conectar();
        PreparedStatement stmt = mBD.prepareStatement(sql);
        int res = stmt.executeUpdate();
        stmt.close();
        desconectar();
        return res;
    }

    public Vector<Object> select(String sql) throws SQLException, ClassNotFoundException {
        /*
         * * Metodo para realizar una busqueda o seleccion de informacion enla base de
         * datos El método select develve un vector de vectores, donde cada uno de los
         * vectores que contiene el vector principal representa los registros que se
         * recuperan de la base de datos.
         */
        Vector<Object> vectoradevolver = new Vector<Object>();
        conectar();
        Statement stmt = mBD.createStatement();
        ResultSet res = stmt.executeQuery(sql);
        ResultSetMetaData rsmd = res.getMetaData();
        int numCol = rsmd.getColumnCount();
        while (res.next()) {
            Vector<Object> v = new Vector<Object>();
            // nuevo
            for (int i = 1; i <= numCol; i++) {
                v.add(res.getObject(i));
            }
            vectoradevolver.add(v);
        }
        stmt.close();
        return vectoradevolver;
    }

    // Métodos parametrizados utilizados por los gestores
    public int ejecutarUpdate(String sql, Object... params) throws SQLException, ClassNotFoundException {
        conectar();
        try (PreparedStatement ps = mBD.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }
            log.debug("UPDATE SQL: {} | params: {}", sql, Arrays.toString(params));
            return ps.executeUpdate();
        } finally {
            desconectar();
        }
    }

    // Inserta y devuelve el ID autogenerado (columna auto_increment)
    public int ejecutarInsertReturnId(String sql, Object... params) throws SQLException, ClassNotFoundException {
        conectar();
        try (PreparedStatement ps = mBD.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }
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

    public List<Object[]> select(String sql, Object... params) throws SQLException, ClassNotFoundException {
        conectar();
        try (PreparedStatement ps = mBD.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }
            log.debug("SELECT SQL: {} | params: {}", sql, Arrays.toString(params));
            try (ResultSet rs = ps.executeQuery()) {
                List<Object[]> out = new ArrayList<>();
                ResultSetMetaData md = rs.getMetaData();
                int cols = md.getColumnCount();
                while (rs.next()) {
                    Object[] row = new Object[cols];
                    for (int c = 1; c <= cols; c++) {
                        row[c - 1] = rs.getObject(c);
                    }
                    out.add(row);
                }
                return out;
            }
        } finally {
            desconectar();
        }
    }

    /*
     * private AgenteDB() {
     * host = System.getProperty("db.host", ConstantesDB.HOST);
     * port = System.getProperty("db.port", ConstantesDB.PORT);
     * db = System.getProperty("db.name", ConstantesDB.BD);
     * url = "jdbc:mysql://" + host + ":" + port + "/" + db
     * + "?serverTimezone=UTC";
     * this.usuario = System.getProperty("db.user", ConstantesDB.USER);
     * this.password = System.getProperty("db.pass", ConstantesDB.PASS);
     * }
     */

    /*
     * public static synchronized AgenteDB getInstancia() {
     * if (instancia == null) {
     * instancia = new AgenteDB();
     * }
     * return instancia;
     * }
     * 
     * private void conectar() throws SQLException {
     * if (conexion == null || conexion.isClosed()) {
     * // Asegurar que la base de datos exista antes de conectar al esquema
     * ensureDatabaseExists();
     * log.debug("Conectando a BBDD: {}", url);
     * conexion = DriverManager.getConnection(url, usuario, password);
     * log.debug("Conexión establecida");
     * }
     * }
     * 
     * private synchronized void ensureDatabaseExists() throws SQLException {
     * if (databaseEnsured)
     * return;
     * String baseUrl = "jdbc:mysql://" + host + ":" + port +
     * "/?serverTimezone=UTC";
     * log.debug("Verificando existencia de la base de datos '{}'", db);
     * try (Connection c = DriverManager.getConnection(baseUrl, usuario, password);
     * Statement st = c.createStatement()) {
     * String create = "CREATE DATABASE IF NOT EXISTS `" + db
     * + "` DEFAULT CHARACTER SET utf8mb4";
     * st.executeUpdate(create);
     * log.info("Base de datos verificada/creada: {}", db);
     * databaseEnsured = true;
     * }
     * }
     * 
     * public void desconectar() {
     * try {
     * if (conexion != null && !conexion.isClosed())
     * conexion.close();
     * } catch (SQLException ignored) {
     * log.warn("Error cerrando conexión", ignored);
     * }
     * }
     * 
     * public int ejecutarUpdate(String sql, Object... params) throws SQLException {
     * conectar();
     * try (PreparedStatement ps = conexion.prepareStatement(sql)) {
     * for (int i = 0; i < params.length; i++)
     * ps.setObject(i + 1, params[i]);
     * log.debug("UPDATE SQL: {} | params: {}", sql, Arrays.toString(params));
     * return ps.executeUpdate();
     * } finally {
     * desconectar();
     * }
     * }
     * 
     * // Inserta y devuelve el ID autogenerado (columna auto_increment)
     * public int ejecutarInsertReturnId(String sql, Object... params) throws
     * SQLException {
     * conectar();
     * try (PreparedStatement ps = conexion.prepareStatement(sql,
     * Statement.RETURN_GENERATED_KEYS)) {
     * for (int i = 0; i < params.length; i++)
     * ps.setObject(i + 1, params[i]);
     * log.debug("INSERT SQL: {} | params: {}", sql, Arrays.toString(params));
     * ps.executeUpdate();
     * try (ResultSet keys = ps.getGeneratedKeys()) {
     * if (keys.next()) {
     * return keys.getInt(1);
     * }
     * return -1;
     * }
     * } finally {
     * desconectar();
     * }
     * }
     * 
     * public List<Object[]> select(String sql, Object... params) throws
     * SQLException {
     * conectar();
     * try (PreparedStatement ps = conexion.prepareStatement(sql)) {
     * for (int i = 0; i < params.length; i++)
     * ps.setObject(i + 1, params[i]);
     * log.debug("SELECT SQL: {} | params: {}", sql, Arrays.toString(params));
     * try (ResultSet rs = ps.executeQuery()) {
     * List<Object[]> out = new ArrayList<>();
     * ResultSetMetaData md = rs.getMetaData();
     * int cols = md.getColumnCount();
     * while (rs.next()) {
     * Object[] row = new Object[cols];
     * for (int c = 1; c <= cols; c++)
     * row[c - 1] = rs.getObject(c);
     * out.add(row);
     * }
     * return out;
     * }
     * } finally {
     * desconectar();
     * }
     * }
     */
}

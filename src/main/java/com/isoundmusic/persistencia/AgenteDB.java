package com.isoundmusic.persistencia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

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
    public int insert(String sql) {

        int res = -1;
        try {
            conectar();

            PreparedStatement stmt = mBD.prepareStatement(sql);
            res = stmt.executeUpdate();
            stmt.close();
            desconectar();

        } catch (ClassNotFoundException | SQLException e) {
            log.error("Error en insert SQL: {}", sql, e);
        }

        return res;
    }

    // Método para realizar una eliminacion en la base de datos
    public int delete(String sql) {
        PreparedStatement stmt;
        int res = -1;

        try {
            stmt = mBD.prepareStatement(sql);
            res = stmt.executeUpdate();
            stmt.close();
            desconectar();

        } catch (SQLException e) {
            log.error("Error en delete SQL: {}", sql, e);
        }

        return res;
    }

    // Método para realizar una eliminacion en la base de datos
    public int update(String sql) {
        int res = -1;
        try {

            conectar();
            PreparedStatement stmt = mBD.prepareStatement(sql);
            res = stmt.executeUpdate();
            stmt.close();
            desconectar();

        } catch (SQLException | ClassNotFoundException e) {
            log.error("Error en update SQL: {}", sql, e);
        }
        return res;
    }

    public List<Object[]> select(String sql) throws SQLException, ClassNotFoundException {
        // Selección sin parámetros, devuelve filas como arrays de objetos
        conectar();
        try (Statement stmt = mBD.createStatement(); ResultSet res = stmt.executeQuery(sql)) {
            List<Object[]> out = new ArrayList<>();
            ResultSetMetaData md = res.getMetaData();
            int cols = md.getColumnCount();
            while (res.next()) {
                Object[] row = new Object[cols];
                for (int c = 1; c <= cols; c++) {
                    row[c - 1] = res.getObject(c);
                }
                out.add(row);
            }
            return out;
        } finally {
            desconectar();
        }
    }

    public List<Object[]> select(String sql, Object... params) throws SQLException, ClassNotFoundException {
        // Selección parametrizada para evitar inyección y problemas de encoding
        conectar();
        try (PreparedStatement ps = mBD.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }
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
}
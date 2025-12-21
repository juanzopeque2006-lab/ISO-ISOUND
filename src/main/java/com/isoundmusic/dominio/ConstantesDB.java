package com.isoundmusic.dominio;

public class ConstantesDB {

    private ConstantesDB() {
        // Constructor vacío
    }

    public static final String HOST = "localhost";
    public static final String PORT = "3306";

    public static final String USER = "ISO";
    public static final String PASS = "Aula@2020";

    public static final String DBNAME = "isoundmusic";
    public static final String CONNECTION_STRING = "jdbc:mysql://" + HOST + ":" + PORT + "/";
    public static final String DRIVER = "com.mysql.cj.jdbc.Driver";
}

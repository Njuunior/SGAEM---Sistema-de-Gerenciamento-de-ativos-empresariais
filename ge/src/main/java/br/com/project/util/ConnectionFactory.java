package br.com.project.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Factory para obter conexões JDBC com o banco de dados PostgreSQL.
 * Configure a URL, usuário e senha conforme seu ambiente.
 */
public class ConnectionFactory {
    private static final String URL = "jdbc:postgresql://localhost:5432/sistema_estoque";
    private static final String USER = "postgres";
    private static final String PASSWORD = "12345";

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Driver PostgreSQL não encontrado. Adicione o JAR postgresql ao projeto (pasta lib).", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}

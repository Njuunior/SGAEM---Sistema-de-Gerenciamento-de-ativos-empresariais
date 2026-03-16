package br.com.project.controller;

import br.com.project.util.ConnectionFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Servlet para testar a conexão com o banco de dados.
 * Acesse: /testeConexao
 */
@WebServlet("/testeConexao")
public class TesteConexaoServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.println("<!DOCTYPE html><html><head><meta charset='UTF-8'><title>Teste de Conexão - SGAEM</title></head><body>");
        out.println("<h1>Teste de Conexão - SGAEM</h1>");
        try (Connection conn = ConnectionFactory.getConnection()) {
            if (conn != null && !conn.isClosed()) {
                out.println("<p style='color: green;'><strong>Conexão com o banco de dados estabelecida com sucesso!</strong></p>");
                out.println("<p>Banco: " + conn.getCatalog() + "</p>");
            }
        } catch (SQLException e) {
            out.println("<p style='color: red;'><strong>Erro ao conectar:</strong> " + e.getMessage() + "</p>");
            out.println("<p>Verifique se o PostgreSQL está rodando e se o script do banco foi executado.</p>");
        }
        out.println("</body></html>");
    }
}

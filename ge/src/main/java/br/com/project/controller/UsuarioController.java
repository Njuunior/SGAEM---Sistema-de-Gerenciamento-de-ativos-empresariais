package br.com.project.controller;

import br.com.project.dao.UsuarioDAO;
import br.com.project.model.Usuario;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Controller para login e logout.
 */
@WebServlet("/usuario")
public class UsuarioController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String acao = request.getParameter("acao");
        if ("sair".equals(acao)) {
            request.getSession().invalidate();
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }
        response.sendRedirect(request.getContextPath() + "/login.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String login = request.getParameter("login");
        String senha = request.getParameter("senha");
        if (login == null || login.trim().isEmpty() || senha == null || senha.trim().isEmpty()) {
            request.setAttribute("erro", "Login e senha são obrigatórios.");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }
        UsuarioDAO dao = new UsuarioDAO();
        try {
            Usuario usuario = dao.buscarPorLoginSenha(login.trim(), senha);
            if (usuario == null) {
                request.setAttribute("erro", "Login ou senha inválidos.");
                request.getRequestDispatcher("/login.jsp").forward(request, response);
                return;
            }
            HttpSession session = request.getSession(true);
            session.setAttribute("usuarioLogado", usuario);
            response.sendRedirect(request.getContextPath() + "/home");
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("erro", "Erro ao acessar o sistema. Tente novamente.");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        }
    }
}

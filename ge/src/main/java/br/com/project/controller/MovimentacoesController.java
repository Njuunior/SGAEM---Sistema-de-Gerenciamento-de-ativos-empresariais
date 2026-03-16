package br.com.project.controller;

import br.com.project.dao.MaterialDAO;
import br.com.project.dao.MovimentacoesDAO;
import br.com.project.model.Material;
import br.com.project.model.Movimentacoes;
import br.com.project.model.Usuario;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Controller para entrada e saída de materiais.
 */
@WebServlet("/movimentacoes")
public class MovimentacoesController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!verificarLogin(request, response)) return;
        String acao = request.getParameter("acao");
        if ("nova".equals(acao)) {
            MaterialDAO matDao = new MaterialDAO();
            try {
                List<Material> materiais = matDao.listarTodos();
                request.setAttribute("materiais", materiais);
                request.setAttribute("paginaAtiva", "movimentacao-nova");
                request.getRequestDispatcher("/movimentacaoForm.jsp").forward(request, response);
            } catch (Exception e) {
                e.printStackTrace();
                response.sendRedirect(request.getContextPath() + "/home");
            }
            return;
        }
        if ("lista".equals(acao)) {
            MovimentacoesDAO dao = new MovimentacoesDAO();
            try {
                List<Movimentacoes> lista = dao.listarTodas();
                request.setAttribute("movimentacoes", lista);
                request.setAttribute("paginaAtiva", "movimentacoes");
                request.getRequestDispatcher("/movimentacaoLista.jsp").forward(request, response);
            } catch (Exception e) {
                e.printStackTrace();
                request.setAttribute("erro", e.getMessage());
                request.setAttribute("paginaAtiva", "movimentacoes");
                request.getRequestDispatcher("/movimentacaoLista.jsp").forward(request, response);
            }
            return;
        }
        response.sendRedirect(request.getContextPath() + "/home");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!verificarLogin(request, response)) return;
        request.setCharacterEncoding("UTF-8");
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuarioLogado");
        if (usuario == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }
        String idMaterialStr = request.getParameter("material_id");
        String quantidadeStr = request.getParameter("quant_movim");
        String tipo = request.getParameter("tipo_movim");
        String dataStr = request.getParameter("data_movim");
        String observacoes = request.getParameter("observacoes_movim");

        if (idMaterialStr == null || idMaterialStr.isEmpty() || quantidadeStr == null || quantidadeStr.isEmpty()
            || tipo == null || tipo.isEmpty() || dataStr == null || dataStr.isEmpty()) {
            request.setAttribute("erro", "Preencha todos os campos obrigatórios.");
            MaterialDAO matDao = new MaterialDAO();
            try {
                request.setAttribute("materiais", matDao.listarTodos());
            } catch (Exception ignored) {}
            request.setAttribute("paginaAtiva", "movimentacao-nova");
            request.getRequestDispatcher("/movimentacaoForm.jsp").forward(request, response);
            return;
        }

        Movimentacoes mov = new Movimentacoes();
        mov.setUsuarioIdUsuario(usuario.getIdUsuario());
        mov.setMaterialIdMaterial(Integer.parseInt(idMaterialStr));
        mov.setQuantMovim(new BigDecimal(quantidadeStr.trim().replace(",", ".")));
        mov.setTipoMovim(tipo.toUpperCase());
        try {
            mov.setDataMovim(new Date(java.sql.Date.valueOf(dataStr).getTime()));
        } catch (Exception e) {
            mov.setDataMovim(new Date());
        }
        mov.setObservacoesMovim(observacoes != null ? observacoes.trim() : null);

        MovimentacoesDAO dao = new MovimentacoesDAO();
        try {
            dao.registrarMovimentacao(mov);
            request.getSession().setAttribute("msg", "Movimentação registrada com sucesso.");
            response.sendRedirect(request.getContextPath() + "/movimentacoes?acao=lista");
        } catch (IllegalArgumentException e) {
            request.setAttribute("erro", e.getMessage());
            MaterialDAO matDao = new MaterialDAO();
            try {
                request.setAttribute("materiais", matDao.listarTodos());
            } catch (Exception ignored) {}
            request.setAttribute("movimentacao", mov);
            request.setAttribute("paginaAtiva", "movimentacao-nova");
            request.getRequestDispatcher("/movimentacaoForm.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("erro", "Erro ao registrar: " + e.getMessage());
            MaterialDAO matDao = new MaterialDAO();
            try {
                request.setAttribute("materiais", matDao.listarTodos());
            } catch (Exception ignored) {}
            request.setAttribute("paginaAtiva", "movimentacao-nova");
            request.getRequestDispatcher("/movimentacaoForm.jsp").forward(request, response);
        }
    }

    private boolean verificarLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (request.getSession().getAttribute("usuarioLogado") == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return false;
        }
        return true;
    }
}

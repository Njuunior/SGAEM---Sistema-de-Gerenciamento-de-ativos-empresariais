package br.com.project.controller;

import br.com.project.dao.MaterialDAO;
import br.com.project.dao.MovimentacoesDAO;
import br.com.project.model.Material;
import br.com.project.model.Movimentacoes;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller do dashboard inicial com indicadores e últimas movimentações.
 */
@WebServlet("/home")
public class DashboardController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getSession().getAttribute("usuarioLogado") == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }
        try {
            MaterialDAO materialDAO = new MaterialDAO();
            MovimentacoesDAO movDAO = new MovimentacoesDAO();
            List<Material> todosMateriais = materialDAO.listarTodos();
            List<Movimentacoes> todasMov = movDAO.listarTodas();

            int totalMateriais = todosMateriais.size();
            int totalMovimentacoes = todasMov.size();
            List<Material> abaixoMinimo = todosMateriais.stream()
                .filter(m -> m.getQtdTotalMaterial() != null && m.getQtdTotalMaterial().compareTo(BigDecimal.valueOf(m.getMinMaterial())) < 0)
                .collect(Collectors.toList());
            List<Movimentacoes> ultimasMov = todasMov.isEmpty() ? new ArrayList<>() : todasMov.subList(0, Math.min(10, todasMov.size()));

            request.setAttribute("totalMateriais", totalMateriais);
            request.setAttribute("totalMovimentacoes", totalMovimentacoes);
            request.setAttribute("materiaisAbaixoMinimo", abaixoMinimo);
            request.setAttribute("ultimasMovimentacoes", ultimasMov);
            request.getRequestDispatcher("/home.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("erroDashboard", "Não foi possível carregar o painel.");
            request.getRequestDispatcher("/home.jsp").forward(request, response);
        }
    }
}

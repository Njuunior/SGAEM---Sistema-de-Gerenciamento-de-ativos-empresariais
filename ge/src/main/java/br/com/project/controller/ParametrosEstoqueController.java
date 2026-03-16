package br.com.project.controller;

import br.com.project.dao.MaterialDAO;
import br.com.project.dao.MovimentacoesDAO;
import br.com.project.model.ConsumoPeriodo;
import br.com.project.model.Material;
import br.com.project.model.Usuario;
import br.com.project.service.ParametrosEstoqueService;
import br.com.project.service.ParametrosEstoqueService.ResultadoParametros;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

/**
 * Módulo Cálculo de Parâmetros de Estoque (estoque mínimo/máximo sugerido por Poisson).
 * Acesso apenas para usuários ADMIN ou SUPERVISOR. Retorna 403 para outros.
 */
@WebServlet("/parametros-estoque")
public class ParametrosEstoqueController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final String[] TIPOS_PERMITIDOS = { "ADMIN", "SUPERVISOR" };

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getSession().getAttribute("usuarioLogado") == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }
        if (!temPermissao(request)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Acesso negado. Apenas ADMIN ou SUPERVISOR.");
            return;
        }
        String acao = request.getParameter("acao");
        if ("exportar".equals(acao)) {
            exportarExcel(request, response);
            return;
        }
        request.setAttribute("paginaAtiva", "parametros-estoque");
        request.getRequestDispatcher("/parametrosEstoque.jsp").forward(request, response);
    }

    /** Recalcula e gera CSV para download; usa codigo_material e periodo_anos da request. */
    private void exportarExcel(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String codigoStr = request.getParameter("codigo_material");
        String periodoStr = request.getParameter("periodo_anos");
        if (codigoStr == null || codigoStr.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/parametros-estoque");
            return;
        }
        int codigoMaterial;
        try { codigoMaterial = Integer.parseInt(codigoStr.trim()); } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/parametros-estoque");
            return;
        }
        int anos = "3".equals(periodoStr) ? 3 : "5".equals(periodoStr) ? 5 : 1;
        MaterialDAO materialDAO = new MaterialDAO();
        Material material;
        try { material = materialDAO.buscarPorCodigo(codigoMaterial); } catch (SQLException e) {
            response.sendRedirect(request.getContextPath() + "/parametros-estoque");
            return;
        }
        if (material == null) {
            response.sendRedirect(request.getContextPath() + "/parametros-estoque");
            return;
        }
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, -anos);
        java.sql.Date dataInicio = new java.sql.Date(cal.getTimeInMillis());
        long numeroDias = TimeUnit.MILLISECONDS.toDays(System.currentTimeMillis() - dataInicio.getTime());
        if (numeroDias <= 0) numeroDias = 1;
        MovimentacoesDAO movDAO = new MovimentacoesDAO();
        ConsumoPeriodo consumo;
        try { consumo = movDAO.getConsumoSaidaPorPeriodo(material.getIdMaterial(), dataInicio); } catch (SQLException e) {
            response.sendRedirect(request.getContextPath() + "/parametros-estoque");
            return;
        }
        ParametrosEstoqueService service = new ParametrosEstoqueService();
        ResultadoParametros r = service.calcularParametrosEstoque(material, consumo, numeroDias);
        response.setContentType("text/csv; charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=\"parametros_estoque.csv\"");
        PrintWriter out = response.getWriter();
        out.print("\uFEFF");
        out.println("Campo;Valor");
        out.println("Material;" + escapeCsv(r.descricaoMaterial));
        out.println("Consumo médio diário;" + r.getConsumoMedioDiaFormatado());
        out.println("Lead time (dias);" + r.leadTime);
        out.println("Demanda LT;" + r.getDemandaLTFormatado());
        out.println("Desvio padrão;" + r.getDesvioPadraoFormatado());
        out.println("Estoque mínimo atual;" + r.estoqueMinAtual);
        out.println("Estoque máximo atual;" + r.estoqueMaxAtual);
        out.println("Estoque mínimo sugerido;" + r.estoqueMinSugerido);
        out.println("Estoque máximo sugerido;" + r.estoqueMaxSugerido);
        out.flush();
    }

    private static String escapeCsv(String s) {
        if (s == null) return "";
        if (s.contains(";") || s.contains("\"") || s.contains("\n")) return "\"" + s.replace("\"", "\"\"") + "\"";
        return s;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getSession().getAttribute("usuarioLogado") == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }
        if (!temPermissao(request)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Acesso negado. Apenas ADMIN ou SUPERVISOR.");
            return;
        }
        request.setCharacterEncoding("UTF-8");
        String codigoStr = request.getParameter("codigo_material");
        String periodoStr = request.getParameter("periodo_anos");
        request.setAttribute("paginaAtiva", "parametros-estoque");
        if (codigoStr == null || codigoStr.trim().isEmpty()) {
            request.setAttribute("erro", "Informe o código do material.");
            request.getRequestDispatcher("/parametrosEstoque.jsp").forward(request, response);
            return;
        }
        int codigoMaterial;
        try {
            codigoMaterial = Integer.parseInt(codigoStr.trim());
        } catch (NumberFormatException e) {
            request.setAttribute("erro", "Código do material inválido.");
            request.getRequestDispatcher("/parametrosEstoque.jsp").forward(request, response);
            return;
        }
        int anos = 1;
        if (periodoStr != null) {
            switch (periodoStr) {
                case "3": anos = 3; break;
                case "5": anos = 5; break;
                default: anos = 1;
            }
        }
        MaterialDAO materialDAO = new MaterialDAO();
        Material material;
        try {
            material = materialDAO.buscarPorCodigo(codigoMaterial);
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("erro", "Erro ao consultar material.");
            request.getRequestDispatcher("/parametrosEstoque.jsp").forward(request, response);
            return;
        }
        if (material == null) {
            request.setAttribute("erro", "Material não encontrado.");
            request.getRequestDispatcher("/parametrosEstoque.jsp").forward(request, response);
            return;
        }
        java.sql.Date dataFim = new java.sql.Date(System.currentTimeMillis());
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, -anos);
        java.sql.Date dataInicio = new java.sql.Date(cal.getTimeInMillis());
        long numeroDias = TimeUnit.MILLISECONDS.toDays(dataFim.getTime() - dataInicio.getTime());
        if (numeroDias <= 0) numeroDias = 1;

        MovimentacoesDAO movDAO = new MovimentacoesDAO();
        ConsumoPeriodo consumo;
        try {
            consumo = movDAO.getConsumoSaidaPorPeriodo(material.getIdMaterial(), dataInicio);
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("erro", "Erro ao buscar histórico de consumo.");
            request.getRequestDispatcher("/parametrosEstoque.jsp").forward(request, response);
            return;
        }

        ParametrosEstoqueService service = new ParametrosEstoqueService();
        ResultadoParametros resultado = service.calcularParametrosEstoque(material, consumo, numeroDias);
        request.setAttribute("resultado", resultado);
        request.setAttribute("codigo_material", codigoMaterial);
        request.setAttribute("periodo_anos", periodoStr != null ? periodoStr : "1");
        request.getRequestDispatcher("/parametrosEstoque.jsp").forward(request, response);
    }

    private boolean temPermissao(HttpServletRequest request) {
        Usuario u = (Usuario) request.getSession().getAttribute("usuarioLogado");
        if (u == null || u.getTipoUsuario() == null) return false;
        String tipo = u.getTipoUsuario().toUpperCase();
        for (String t : TIPOS_PERMITIDOS) {
            if (t.equals(tipo)) return true;
        }
        return false;
    }
}

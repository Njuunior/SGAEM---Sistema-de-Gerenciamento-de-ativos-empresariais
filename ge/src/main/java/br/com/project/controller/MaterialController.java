package br.com.project.controller;

import br.com.project.dao.MaterialDAO;
import br.com.project.model.Material;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

/**
 * Controller para CRUD de materiais.
 */
@WebServlet("/material")
public class MaterialController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!verificarLogin(request, response)) return;
        String acao = request.getParameter("acao");
        MaterialDAO dao = new MaterialDAO();
        try {
            if ("novo".equals(acao)) {
                request.setAttribute("paginaAtiva", "material-novo");
                request.getRequestDispatcher("/materialForm.jsp").forward(request, response);
                return;
            }
            if ("editar".equals(acao)) {
                String idStr = request.getParameter("id");
                if (idStr != null && !idStr.isEmpty()) {
                    Material m = dao.buscarPorId(Integer.parseInt(idStr));
                    if (m != null) {
                        request.setAttribute("material", m);
                        request.setAttribute("paginaAtiva", "material-novo");
                        request.getRequestDispatcher("/materialForm.jsp").forward(request, response);
                        return;
                    }
                }
                response.sendRedirect(request.getContextPath() + "/material?acao=listar");
                return;
            }
            if ("excluir".equals(acao)) {
                String idStr = request.getParameter("id");
                if (idStr != null && !idStr.isEmpty()) {
                    dao.excluir(Integer.parseInt(idStr));
                }
                response.sendRedirect(request.getContextPath() + "/material?acao=listar");
                return;
            }
            // listar (padrão)
            List<Material> materiais = dao.listarTodos();
            request.setAttribute("materiais", materiais);
            request.setAttribute("paginaAtiva", "materiais");
            request.getRequestDispatcher("/materialLista.jsp").forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("erro", "Erro ao acessar dados: " + e.getMessage());
            request.setAttribute("paginaAtiva", "materiais");
            request.getRequestDispatcher("/materialLista.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!verificarLogin(request, response)) return;
        request.setCharacterEncoding("UTF-8");
        String idStr = request.getParameter("id_material");
        int id = (idStr != null && !idStr.isEmpty()) ? Integer.parseInt(idStr) : 0;
        String codigoStr = request.getParameter("codigo_material");
        String descricao = request.getParameter("descricao_material");
        String unidMedida = request.getParameter("unidMedida_material");
        String precoStr = request.getParameter("preco_material");
        String minStr = request.getParameter("min_material");
        String maxStr = request.getParameter("max_material");
        String posicaoAlmox = request.getParameter("posicao_almoxarifado");
        String qtdStr = request.getParameter("qtd_total_material");

        Material m = new Material();
        if (id > 0) m.setIdMaterial(id);
        if (codigoStr != null && !codigoStr.isEmpty()) m.setCodigoMaterial(Integer.parseInt(codigoStr.trim()));
        m.setDescricaoMaterial(descricao != null ? descricao.trim() : "");
        m.setUnidMedidaMaterial(unidMedida != null ? unidMedida.trim() : "UN");
        m.setPrecoMaterial(precoStr != null && !precoStr.isEmpty() ? new BigDecimal(precoStr.replace(",", ".")) : BigDecimal.ZERO);
        m.setMinMaterial(minStr != null && !minStr.isEmpty() ? Integer.parseInt(minStr.trim()) : 0);
        m.setMaxMaterial(maxStr != null && !maxStr.isEmpty() ? Integer.parseInt(maxStr.trim()) : 0);
        m.setPosicaoAlmoxarifado(posicaoAlmox != null ? posicaoAlmox.trim() : null);
        if (id == 0 && qtdStr != null && !qtdStr.isEmpty()) {
            m.setQtdTotalMaterial(new BigDecimal(qtdStr.replace(",", ".")));
        } else if (id > 0) {
            MaterialDAO dao = new MaterialDAO();
            try {
                Material existente = dao.buscarPorId(id);
                if (existente != null) m.setQtdTotalMaterial(existente.getQtdTotalMaterial());
            } catch (SQLException ignored) {}
        }

        MaterialDAO dao = new MaterialDAO();
        try {
            if (id > 0) {
                dao.atualizar(m);
                request.getSession().setAttribute("msg", "Material atualizado com sucesso.");
            } else {
                dao.inserir(m);
                request.getSession().setAttribute("msg", "Material cadastrado com sucesso.");
            }
            response.sendRedirect(request.getContextPath() + "/material?acao=listar");
        } catch (IllegalArgumentException e) {
            request.setAttribute("erro", e.getMessage());
            request.setAttribute("material", m);
            request.setAttribute("paginaAtiva", "material-novo");
            request.getRequestDispatcher("/materialForm.jsp").forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("erro", "Erro ao salvar: " + e.getMessage());
            request.setAttribute("material", m);
            request.setAttribute("paginaAtiva", "material-novo");
            request.getRequestDispatcher("/materialForm.jsp").forward(request, response);
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

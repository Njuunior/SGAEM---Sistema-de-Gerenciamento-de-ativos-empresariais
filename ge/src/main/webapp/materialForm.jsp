<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    if (session.getAttribute("usuarioLogado") == null) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }
    if (request.getAttribute("paginaAtiva") == null) request.setAttribute("paginaAtiva", "material-novo");
    br.com.project.model.Material m = (br.com.project.model.Material) request.getAttribute("material");
    boolean isEdicao = (m != null && m.getIdMaterial() > 0);
    String erro = (String) request.getAttribute("erro");
    if (m == null) m = new br.com.project.model.Material();
%>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><%= isEdicao ? "Editar" : "Novo" %> material - SGAEM</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/app.css">
</head>
<body>
    <div class="app-layout">
        <jsp:include page="/includes/sidebar.jsp"/>
        <main class="app-main">
            <div class="app-content">
                <header class="page-header">
                    <h1 class="page-title"><%= isEdicao ? "Editar material" : "Novo material" %></h1>
                    <p class="page-subtitle"><%= isEdicao ? "Altere os dados do material" : "Preencha os dados para cadastrar" %></p>
                </header>
                <% if (erro != null) { %><div class="alert alert-error"><%= erro %></div><% } %>
                <div class="form-card">
                    <form action="<%= request.getContextPath() %>/material" method="post">
                        <% if (isEdicao) { %><input type="hidden" name="id_material" value="<%= m.getIdMaterial() %>"><% } %>
                        <div class="form-group">
                            <label>Código do material *</label>
                            <input type="number" name="codigo_material" value="<%= m.getCodigoMaterial() %>" required <%= isEdicao ? "readonly" : "" %>>
                        </div>
                        <div class="form-group">
                            <label>Descrição *</label>
                            <input type="text" name="descricao_material" value="<%= m.getDescricaoMaterial() != null ? m.getDescricaoMaterial() : "" %>" required>
                        </div>
                        <div class="form-row">
                            <div class="form-group">
                                <label>Unidade de medida *</label>
                                <select name="unidMedida_material" required>
                                    <option value="UN" <%= "UN".equals(m.getUnidMedidaMaterial()) ? "selected" : "" %>>UN</option>
                                    <option value="KG" <%= "KG".equals(m.getUnidMedidaMaterial()) ? "selected" : "" %>>KG</option>
                                    <option value="M" <%= "M".equals(m.getUnidMedidaMaterial()) ? "selected" : "" %>>M</option>
                                    <option value="L" <%= "L".equals(m.getUnidMedidaMaterial()) ? "selected" : "" %>>L</option>
                                    <option value="PCT" <%= "PCT".equals(m.getUnidMedidaMaterial()) ? "selected" : "" %>>PCT</option>
                                    <option value="PAR" <%= "PAR".equals(m.getUnidMedidaMaterial()) ? "selected" : "" %>>PAR</option>
                                    <option value="CX" <%= "CX".equals(m.getUnidMedidaMaterial()) ? "selected" : "" %>>CX</option>
                                </select>
                            </div>
                            <div class="form-group">
                                <label>Preço (R$) *</label>
                                <input type="text" name="preco_material" value="<%= m.getPrecoMaterial() != null ? m.getPrecoMaterial().toString().replace(".", ",") : "" %>" required placeholder="0,00">
                            </div>
                        </div>
                        <div class="form-row">
                            <div class="form-group">
                                <label>Quantidade mínima *</label>
                                <input type="number" name="min_material" value="<%= m.getMinMaterial() %>" min="0" required>
                            </div>
                            <div class="form-group">
                                <label>Quantidade máxima *</label>
                                <input type="number" name="max_material" value="<%= m.getMaxMaterial() %>" min="0" required>
                            </div>
                        </div>
                        <% if (!isEdicao) { %>
                        <div class="form-group">
                            <label>Quantidade inicial (estoque)</label>
                            <input type="text" name="qtd_total_material" value="<%= m.getQtdTotalMaterial() != null ? m.getQtdTotalMaterial().toString().replace(".", ",") : "0" %>" placeholder="0">
                        </div>
                        <% } %>
                        <div class="form-group">
                            <label>Posição no almoxarifado</label>
                            <input type="text" name="posicao_almoxarifado" value="<%= m.getPosicaoAlmoxarifado() != null ? m.getPosicaoAlmoxarifado() : "" %>" placeholder="Ex: Prateleira A1">
                        </div>
                        <div class="form-actions">
                            <button type="submit" class="btn btn-primary">Salvar</button>
                            <a href="<%= request.getContextPath() %>/material?acao=listar" class="btn btn-secondary">Cancelar</a>
                        </div>
                    </form>
                </div>
            </div>
        </main>
    </div>
</body>
</html>

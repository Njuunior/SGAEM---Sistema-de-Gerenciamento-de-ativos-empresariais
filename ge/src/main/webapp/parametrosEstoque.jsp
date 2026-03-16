<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="br.com.project.service.ParametrosEstoqueService" %>
<%
    if (session.getAttribute("usuarioLogado") == null) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }
    br.com.project.model.Usuario usuario = (br.com.project.model.Usuario) session.getAttribute("usuarioLogado");
    String tipo = usuario.getTipoUsuario() != null ? usuario.getTipoUsuario().toUpperCase() : "";
    boolean podeAcessar = "ADMIN".equals(tipo) || "SUPERVISOR".equals(tipo);
    if (!podeAcessar) {
        response.sendError(403, "Acesso negado. Apenas ADMIN ou SUPERVISOR.");
        return;
    }
    if (request.getAttribute("paginaAtiva") == null) request.setAttribute("paginaAtiva", "parametros-estoque");
    ParametrosEstoqueService.ResultadoParametros resultado = (ParametrosEstoqueService.ResultadoParametros) request.getAttribute("resultado");
    String erro = (String) request.getAttribute("erro");
    Object codigoMaterial = request.getAttribute("codigo_material");
    String periodoAnos = (String) request.getAttribute("periodo_anos");
    if (periodoAnos == null) periodoAnos = "1";
    String ctx = request.getContextPath();
%>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Parâmetros de Estoque - SGAEM</title>
    <link rel="stylesheet" href="<%= ctx %>/css/app.css">
</head>
<body>
    <div class="app-layout">
        <jsp:include page="/includes/sidebar.jsp"/>
        <main class="app-main">
            <div class="app-content">
                <header class="page-header">
                    <h1 class="page-title">Parâmetros de Estoque</h1>
                    <p class="page-subtitle">Cálculo de estoque mínimo e máximo sugeridos com base no histórico (Poisson)</p>
                </header>
                <% if (erro != null) { %><div class="alert alert-error"><%= erro %></div><% } %>
                <div class="form-card" style="max-width: 480px;">
                    <form action="<%= ctx %>/parametros-estoque" method="post">
                        <div class="form-group">
                            <label for="codigo_material">Código do material *</label>
                            <input type="number" id="codigo_material" name="codigo_material" value="<%= codigoMaterial != null ? codigoMaterial : "" %>" required placeholder="Ex: 1001">
                        </div>
                        <div class="form-group">
                            <label for="periodo_anos">Período de análise *</label>
                            <select id="periodo_anos" name="periodo_anos" required>
                                <option value="1" <%= "1".equals(periodoAnos) ? "selected" : "" %>>1 ano</option>
                                <option value="3" <%= "3".equals(periodoAnos) ? "selected" : "" %>>3 anos</option>
                                <option value="5" <%= "5".equals(periodoAnos) ? "selected" : "" %>>5 anos</option>
                            </select>
                        </div>
                        <div class="form-actions">
                            <button type="submit" class="btn btn-primary">Calcular parâmetros</button>
                        </div>
                    </form>
                </div>
                <% if (resultado != null) { %>
                <div class="dashboard-section" style="margin-top: 2rem;">
                    <h2>Resultado</h2>
                    <div class="table-wrap">
                        <table class="data-table">
                            <tbody>
                                <tr><td><strong>Material</strong></td><td><%= resultado.descricaoMaterial %></td></tr>
                                <tr><td><strong>Consumo médio diário</strong></td><td><%= resultado.getConsumoMedioDiaFormatado() %></td></tr>
                                <tr><td><strong>Lead time (dias)</strong></td><td><%= resultado.leadTime %></td></tr>
                                <tr><td><strong>Demanda LT</strong></td><td><%= resultado.getDemandaLTFormatado() %></td></tr>
                                <tr><td><strong>Desvio padrão</strong></td><td><%= resultado.getDesvioPadraoFormatado() %></td></tr>
                                <tr><td><strong>Estoque mínimo atual</strong></td><td><%= resultado.estoqueMinAtual %></td></tr>
                                <tr><td><strong>Estoque máximo atual</strong></td><td><%= resultado.estoqueMaxAtual %></td></tr>
                                <tr><td><strong>Estoque mínimo sugerido</strong></td><td><%= resultado.estoqueMinSugerido %></td></tr>
                                <tr><td><strong>Estoque máximo sugerido</strong></td><td><%= resultado.estoqueMaxSugerido %></td></tr>
                            </tbody>
                        </table>
                    </div>
                    <div class="form-actions" style="margin-top: 1rem;">
                        <a href="<%= ctx %>/parametros-estoque?acao=exportar&codigo_material=<%= codigoMaterial %>&periodo_anos=<%= periodoAnos %>" class="btn btn-secondary">Exportar Excel</a>
                        <span style="color: var(--text-muted); font-size: 0.9rem;">(Atualizar parâmetros no sistema — em breve)</span>
                    </div>
                </div>
                <% } %>
            </div>
        </main>
    </div>
</body>
</html>

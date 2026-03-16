<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%
    if (session.getAttribute("usuarioLogado") == null) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }
    if (request.getAttribute("totalMateriais") == null && request.getAttribute("erroDashboard") == null) {
        response.sendRedirect(request.getContextPath() + "/home");
        return;
    }
    request.setAttribute("paginaAtiva", "home");
    int totalMateriais = request.getAttribute("totalMateriais") != null ? (Integer) request.getAttribute("totalMateriais") : 0;
    int totalMovimentacoes = request.getAttribute("totalMovimentacoes") != null ? (Integer) request.getAttribute("totalMovimentacoes") : 0;
    List materiaisAbaixoMinimo = (List) request.getAttribute("materiaisAbaixoMinimo");
    List ultimasMovimentacoes = (List) request.getAttribute("ultimasMovimentacoes");
    String erroDashboard = (String) request.getAttribute("erroDashboard");
    if (materiaisAbaixoMinimo == null) materiaisAbaixoMinimo = java.util.Collections.emptyList();
    if (ultimasMovimentacoes == null) ultimasMovimentacoes = java.util.Collections.emptyList();
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
%>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Início - SGAEM</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/app.css">
</head>
<body>
    <div class="app-layout">
        <jsp:include page="/includes/sidebar.jsp"/>
        <main class="app-main">
            <div class="app-content">
                <header class="page-header">
                    <h1 class="page-title">Painel</h1>
                    <p class="page-subtitle">Visão geral do estoque e das movimentações</p>
                </header>
                <% if (erroDashboard != null) { %>
                    <div class="alert alert-error"><%= erroDashboard %></div>
                <% } %>
                <div class="dashboard-cards">
                    <div class="card">
                        <div class="card-num"><%= totalMateriais %></div>
                        <div class="card-label">Materiais cadastrados</div>
                    </div>
                    <div class="card card--accent">
                        <div class="card-num"><%= totalMovimentacoes %></div>
                        <div class="card-label">Movimentações registradas</div>
                    </div>
                    <div class="card card--warning">
                        <div class="card-num"><%= materiaisAbaixoMinimo.size() %></div>
                        <div class="card-label">Abaixo do estoque mínimo</div>
                    </div>
                </div>
                <% if (!materiaisAbaixoMinimo.isEmpty()) { %>
                <section class="dashboard-section">
                    <h2>Materiais abaixo do estoque mínimo</h2>
                    <div class="table-wrap">
                        <table class="data-table">
                            <thead>
                                <tr>
                                    <th>Código</th>
                                    <th>Descrição</th>
                                    <th>Estoque</th>
                                    <th>Mínimo</th>
                                    <th>Unidade</th>
                                </tr>
                            </thead>
                            <tbody>
                                <% for (Object item : materiaisAbaixoMinimo) {
                                    br.com.project.model.Material mat = (br.com.project.model.Material) item; %>
                                <tr>
                                    <td><%= mat.getCodigoMaterial() %></td>
                                    <td><%= mat.getDescricaoMaterial() %></td>
                                    <td><%= mat.getQtdTotalMaterial() %></td>
                                    <td><%= mat.getMinMaterial() %></td>
                                    <td><%= mat.getUnidMedidaMaterial() %></td>
                                </tr>
                                <% } %>
                            </tbody>
                        </table>
                    </div>
                    <p><a href="<%= request.getContextPath() %>/material?acao=listar" class="btn btn-secondary">Ver todos os materiais</a></p>
                </section>
                <% } %>
                <section class="dashboard-section">
                    <h2>Últimas movimentações</h2>
                    <% if (ultimasMovimentacoes.isEmpty()) { %>
                    <div class="table-wrap">
                        <div class="empty-state">Nenhuma movimentação registrada ainda.</div>
                    </div>
                    <% } else { %>
                    <div class="table-wrap">
                        <table class="data-table">
                            <thead>
                                <tr>
                                    <th>Data</th>
                                    <th>Tipo</th>
                                    <th>Material</th>
                                    <th>Quantidade</th>
                                    <th>Observação</th>
                                </tr>
                            </thead>
                            <tbody>
                                <% for (Object item : ultimasMovimentacoes) {
                                    br.com.project.model.Movimentacoes mov = (br.com.project.model.Movimentacoes) item; %>
                                <tr>
                                    <td><%= mov.getDataMovim() != null ? sdf.format(mov.getDataMovim()) : "-" %></td>
                                    <td><span class="<%= "ENTRADA".equals(mov.getTipoMovim()) ? "badge-entrada" : "badge-saida" %>"><%= mov.getTipoMovim() %></span></td>
                                    <td><%= mov.getCodigoMaterialDesc() %> - <%= mov.getDescricaoMaterial() != null ? mov.getDescricaoMaterial() : "" %></td>
                                    <td><%= mov.getQuantMovim() %></td>
                                    <td><%= mov.getObservacoesMovim() != null ? mov.getObservacoesMovim() : "-" %></td>
                                </tr>
                                <% } %>
                            </tbody>
                        </table>
                    </div>
                    <p><a href="<%= request.getContextPath() %>/movimentacoes?acao=lista" class="btn btn-secondary">Ver histórico completo</a></p>
                    <% } %>
                </section>
            </div>
        </main>
    </div>
</body>
</html>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%
    if (session.getAttribute("usuarioLogado") == null) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }
    if (request.getAttribute("paginaAtiva") == null) request.setAttribute("paginaAtiva", "movimentacoes");
    List movimentacoes = (List) request.getAttribute("movimentacoes");
    String msg = (String) session.getAttribute("msg");
    if (msg != null) session.removeAttribute("msg");
    String erro = (String) request.getAttribute("erro");
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
%>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Histórico de movimentações - SGAEM</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/app.css">
</head>
<body>
    <div class="app-layout">
        <jsp:include page="/includes/sidebar.jsp"/>
        <main class="app-main">
            <div class="app-content">
                <header class="page-header">
                    <h1 class="page-title">Histórico de movimentações</h1>
                    <p class="page-subtitle">Entradas e saídas registradas no sistema</p>
                </header>
                <% if (msg != null) { %><div class="alert alert-success"><%= msg %></div><% } %>
                <% if (erro != null) { %><div class="alert alert-error"><%= erro %></div><% } %>
                <p style="margin-bottom: 1rem;">
                    <a href="<%= request.getContextPath() %>/movimentacoes?acao=nova" class="btn btn-primary">Nova movimentação</a>
                </p>
                <% if (movimentacoes != null && !movimentacoes.isEmpty()) { %>
                <div class="table-wrap">
                    <table class="data-table">
                        <thead>
                            <tr>
                                <th>Data</th>
                                <th>Tipo</th>
                                <th>Material</th>
                                <th>Quantidade</th>
                                <th>Usuário</th>
                                <th>Observações</th>
                            </tr>
                        </thead>
                        <tbody>
                            <% for (Object item : movimentacoes) {
                                br.com.project.model.Movimentacoes mov = (br.com.project.model.Movimentacoes) item; %>
                            <tr>
                                <td><%= mov.getDataMovim() != null ? sdf.format(mov.getDataMovim()) : "—" %></td>
                                <td><span class="<%= "ENTRADA".equals(mov.getTipoMovim()) ? "badge-entrada" : "badge-saida" %>"><%= mov.getTipoMovim() %></span></td>
                                <td><%= mov.getCodigoMaterialDesc() %> — <%= mov.getDescricaoMaterial() != null ? mov.getDescricaoMaterial() : "" %></td>
                                <td><%= mov.getQuantMovim() %></td>
                                <td><%= mov.getNomeUsuario() != null ? mov.getNomeUsuario() : "—" %></td>
                                <td><%= mov.getObservacoesMovim() != null ? mov.getObservacoesMovim() : "—" %></td>
                            </tr>
                            <% } %>
                        </tbody>
                    </table>
                </div>
                <% } else { %>
                <div class="table-wrap">
                    <div class="empty-state">Nenhuma movimentação registrada. <a href="<%= request.getContextPath() %>/movimentacoes?acao=nova" class="btn btn-primary" style="margin-top: 0.5rem;">Registrar movimentação</a></div>
                </div>
                <% } %>
            </div>
        </main>
    </div>
</body>
</html>

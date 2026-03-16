<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%
    if (session.getAttribute("usuarioLogado") == null) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }
    if (request.getAttribute("paginaAtiva") == null) request.setAttribute("paginaAtiva", "materiais");
    List materiais = (List) request.getAttribute("materiais");
    String msg = (String) session.getAttribute("msg");
    if (msg != null) session.removeAttribute("msg");
    String erro = (String) request.getAttribute("erro");
%>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Materiais - SGAEM</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/app.css">
</head>
<body>
    <div class="app-layout">
        <jsp:include page="/includes/sidebar.jsp"/>
        <main class="app-main">
            <div class="app-content">
                <header class="page-header">
                    <h1 class="page-title">Materiais</h1>
                    <p class="page-subtitle">Cadastro e consulta de materiais do estoque</p>
                </header>
                <% if (msg != null) { %><div class="alert alert-success"><%= msg %></div><% } %>
                <% if (erro != null) { %><div class="alert alert-error"><%= erro %></div><% } %>
                <p style="margin-bottom: 1rem;">
                    <a href="<%= request.getContextPath() %>/material?acao=novo" class="btn btn-primary">Novo material</a>
                </p>
                <% if (materiais != null && !materiais.isEmpty()) { %>
                <div class="table-wrap">
                    <table class="data-table">
                        <thead>
                            <tr>
                                <th>Código</th>
                                <th>Descrição</th>
                                <th>Unidade</th>
                                <th>Preço</th>
                                <th>Min / Max</th>
                                <th>Estoque</th>
                                <th>Posição</th>
                                <th>Ações</th>
                            </tr>
                        </thead>
                        <tbody>
                            <% for (Object item : materiais) {
                                br.com.project.model.Material m = (br.com.project.model.Material) item; %>
                            <tr>
                                <td><%= m.getCodigoMaterial() %></td>
                                <td><%= m.getDescricaoMaterial() %></td>
                                <td><%= m.getUnidMedidaMaterial() %></td>
                                <td>R$ <%= String.format("%.2f", m.getPrecoMaterial()) %></td>
                                <td><%= m.getMinMaterial() %> / <%= m.getMaxMaterial() %></td>
                                <td><%= m.getQtdTotalMaterial() != null ? m.getQtdTotalMaterial() : "0" %></td>
                                <td><%= m.getPosicaoAlmoxarifado() != null ? m.getPosicaoAlmoxarifado() : "—" %></td>
                                <td class="acoes">
                                    <a href="<%= request.getContextPath() %>/material?acao=editar&id=<%= m.getIdMaterial() %>" class="btn btn-outline">Editar</a>
                                    <a href="<%= request.getContextPath() %>/material?acao=excluir&id=<%= m.getIdMaterial() %>" class="btn btn-outline btn-excluir excluir" onclick="return confirm('Excluir este material?');">Excluir</a>
                                </td>
                            </tr>
                            <% } %>
                        </tbody>
                    </table>
                </div>
                <% } else { %>
                <div class="table-wrap">
                    <div class="empty-state">Nenhum material cadastrado. <a href="<%= request.getContextPath() %>/material?acao=novo" class="btn btn-primary" style="margin-top: 0.5rem;">Cadastrar primeiro material</a></div>
                </div>
                <% } %>
            </div>
        </main>
    </div>
</body>
</html>

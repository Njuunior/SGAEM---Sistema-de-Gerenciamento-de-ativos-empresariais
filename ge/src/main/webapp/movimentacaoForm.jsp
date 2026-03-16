<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%
    if (session.getAttribute("usuarioLogado") == null) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }
    if (request.getAttribute("paginaAtiva") == null) request.setAttribute("paginaAtiva", "movimentacao-nova");
    List materiais = (List) request.getAttribute("materiais");
    br.com.project.model.Movimentacoes mov = (br.com.project.model.Movimentacoes) request.getAttribute("movimentacao");
    String erro = (String) request.getAttribute("erro");
    if (materiais == null) materiais = java.util.Collections.emptyList();
%>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Nova movimentação - SGAEM</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/app.css">
</head>
<body>
    <div class="app-layout">
        <jsp:include page="/includes/sidebar.jsp"/>
        <main class="app-main">
            <div class="app-content">
                <header class="page-header">
                    <h1 class="page-title">Nova movimentação</h1>
                    <p class="page-subtitle">Registre entrada ou saída de material</p>
                </header>
                <% if (erro != null) { %><div class="alert alert-error"><%= erro %></div><% } %>
                <div class="form-card">
                    <form action="<%= request.getContextPath() %>/movimentacoes" method="post">
                        <div class="form-group">
                            <label>Material *</label>
                            <select name="material_id" required>
                                <option value="">Selecione o material</option>
                                <% for (Object item : materiais) {
                                    br.com.project.model.Material mat = (br.com.project.model.Material) item; %>
                                <option value="<%= mat.getIdMaterial() %>" <%= (mov != null && mov.getMaterialIdMaterial() == mat.getIdMaterial()) ? "selected" : "" %>>
                                    <%= mat.getCodigoMaterial() %> — <%= mat.getDescricaoMaterial() %> (Estoque: <%= mat.getQtdTotalMaterial() %> <%= mat.getUnidMedidaMaterial() %>)
                                </option>
                                <% } %>
                            </select>
                        </div>
                        <div class="form-group">
                            <label>Tipo *</label>
                            <select name="tipo_movim" required>
                                <option value="ENTRADA" <%= (mov != null && "ENTRADA".equals(mov.getTipoMovim())) ? "selected" : "" %>>Entrada</option>
                                <option value="SAIDA" <%= (mov != null && "SAIDA".equals(mov.getTipoMovim())) ? "selected" : "" %>>Saída</option>
                            </select>
                        </div>
                        <div class="form-row">
                            <div class="form-group">
                                <label>Quantidade *</label>
                                <input type="text" name="quant_movim" value="<%= (mov != null && mov.getQuantMovim() != null) ? mov.getQuantMovim() : "" %>" required placeholder="Ex: 10 ou 2,5">
                            </div>
                            <div class="form-group">
                                <label>Data *</label>
                                <input type="date" name="data_movim" value="<%= (mov != null && mov.getDataMovim() != null) ? new java.text.SimpleDateFormat("yyyy-MM-dd").format(mov.getDataMovim()) : new java.text.SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date()) %>" required>
                            </div>
                        </div>
                        <div class="form-group">
                            <label>Observações</label>
                            <input type="text" name="observacoes_movim" value="<%= (mov != null && mov.getObservacoesMovim() != null) ? mov.getObservacoesMovim() : "" %>" placeholder="Opcional">
                        </div>
                        <div class="form-actions">
                            <button type="submit" class="btn btn-primary">Registrar movimentação</button>
                            <a href="<%= request.getContextPath() %>/movimentacoes?acao=lista" class="btn btn-secondary">Ver histórico</a>
                        </div>
                    </form>
                    <p style="margin-top: 1rem; font-size: 0.85rem; color: var(--text-muted);">Entrada não pode ultrapassar o estoque máximo do material. Saída não é permitida para estoque zerado.</p>
                </div>
            </div>
        </main>
    </div>
</body>
</html>

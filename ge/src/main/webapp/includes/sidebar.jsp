<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    String ctx = request.getContextPath();
    String ativo = (String) request.getAttribute("paginaAtiva");
    if (ativo == null) ativo = "";
    br.com.project.model.Usuario usuarioSidebar = (br.com.project.model.Usuario) session.getAttribute("usuarioLogado");
    String tipoUsuario = usuarioSidebar != null && usuarioSidebar.getTipoUsuario() != null ? usuarioSidebar.getTipoUsuario().toUpperCase() : "";
    boolean showParametros = "ADMIN".equals(tipoUsuario) || "SUPERVISOR".equals(tipoUsuario);
%>
<div class="sidebar-overlay" id="sidebarOverlay" aria-hidden="true"></div>
<aside class="app-sidebar" id="appSidebar">
    <div class="app-sidebar-brand">
        <div class="brand-inner">
            <span class="brand-logo">SGAEM</span>
            <span class="brand-desc">Sistema de gerenciamento de ativos empresariais</span>
        </div>
    </div>
    <nav class="app-sidebar-nav">
        <a href="<%= ctx %>/home" class="<%= "home".equals(ativo) ? "ativo" : "" %>"><span class="nav-icon"><svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M3 9l9-7 9 7v11a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2z"/><polyline points="9 22 9 12 15 12 15 22"/></svg></span><span class="nav-text">Início</span></a>
        <a href="<%= ctx %>/material?acao=listar" class="<%= "materiais".equals(ativo) ? "ativo" : "" %>"><span class="nav-icon"><svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M21 16V8a2 2 0 0 0-1-1.73l-7-4a2 2 0 0 0-2 0l-7 4A2 2 0 0 0 3 8v8a2 2 0 0 0 1 1.73l7 4a2 2 0 0 0 2 0l7-4A2 2 0 0 0 21 16z"/><polyline points="3.27 6.96 12 12.01 20.73 6.96"/><line x1="12" y1="22.08" x2="12" y2="12"/></svg></span><span class="nav-text">Materiais</span></a>
        <a href="<%= ctx %>/material?acao=novo" class="<%= "material-novo".equals(ativo) ? "ativo" : "" %>"><span class="nav-icon"><svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><line x1="12" y1="5" x2="12" y2="19"/><line x1="5" y1="12" x2="19" y2="12"/></svg></span><span class="nav-text">Novo material</span></a>
        <a href="<%= ctx %>/movimentacoes?acao=nova" class="<%= "movimentacao-nova".equals(ativo) ? "ativo" : "" %>"><span class="nav-icon"><svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M7 16V4m0 0L3 8m4-4l4 4"/><path d="M17 8v12m0 0l4-4m-4 4l-4-4"/></svg></span><span class="nav-text">Nova movimentação</span></a>
        <a href="<%= ctx %>/movimentacoes?acao=lista" class="<%= "movimentacoes".equals(ativo) ? "ativo" : "" %>"><span class="nav-icon"><svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><line x1="8" y1="6" x2="21" y2="6"/><line x1="8" y1="12" x2="21" y2="12"/><line x1="8" y1="18" x2="21" y2="18"/><line x1="3" y1="6" x2="3.01" y2="6"/><line x1="3" y1="12" x2="3.01" y2="12"/><line x1="3" y1="18" x2="3.01" y2="18"/></svg></span><span class="nav-text">Histórico</span></a>
        <% if (showParametros) { %>
        <a href="<%= ctx %>/parametros-estoque" class="<%= "parametros-estoque".equals(ativo) ? "ativo" : "" %>"><span class="nav-icon"><svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><circle cx="12" cy="12" r="3"/><path d="M19.4 15a1.65 1.65 0 0 0 .33 1.82l.06.06a2 2 0 0 1 0 2.83 2 2 0 0 1-2.83 0l-.06-.06a1.65 1.65 0 0 0-1.82-.33 1.65 1.65 0 0 0-1 1.51V21a2 2 0 0 1-2 2 2 2 0 0 1-2-2v-.09A1.65 1.65 0 0 0 9 19.4a1.65 1.65 0 0 0-1.82.33l-.06.06a2 2 0 0 1-2.83 0 2 2 0 0 1 0-2.83l.06-.06a1.65 1.65 0 0 0 .33-1.82 1.65 1.65 0 0 0-1.51-1H3a2 2 0 0 1-2-2 2 2 0 0 1 2-2h.09A1.65 1.65 0 0 0 4.6 9a1.65 1.65 0 0 0-.33-1.82l-.06-.06a2 2 0 0 1 0-2.83 2 2 0 0 1 2.83 0l.06.06a1.65 1.65 0 0 0 1.82.33H9a1.65 1.65 0 0 0 1-1.51V3a2 2 0 0 1 2-2 2 2 0 0 1 2 2v.09a1.65 1.65 0 0 0 1 1.51 1.65 1.65 0 0 0 1.82-.33l.06-.06a2 2 0 0 1 2.83 0 2 2 0 0 1 0 2.83l-.06.06a1.65 1.65 0 0 0-.33 1.82V9a1.65 1.65 0 0 0 1.51 1H21a2 2 0 0 1 2 2 2 2 0 0 1-2 2h-.09a1.65 1.65 0 0 0-1.51 1z"/></svg></span><span class="nav-text">Parâmetros de Estoque</span></a>
        <% } %>
    </nav>
    <div class="app-sidebar-footer">
        <a href="<%= ctx %>/usuario?acao=sair" class="btn-logout"><span class="nav-icon"><svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M9 21H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h4"/><polyline points="16 17 21 12 16 7"/><line x1="21" y1="12" x2="9" y2="12"/></svg></span><span class="nav-text">Sair do sistema</span></a>
    </div>
</aside>
<script>
(function() {
    var SIDEBAR_W = 280;
    var SIDEBAR_W_COLLAPSED = 80;
    function getLayout() {
        var sidebar = document.getElementById('appSidebar');
        return sidebar ? sidebar.closest('.app-layout') : document.querySelector('.app-layout');
    }
    function toggleSidebar() {
        var layout = getLayout();
        if (!layout) return;
        layout.classList.toggle('sidebar-collapsed');
        var collapsed = layout.classList.contains('sidebar-collapsed');
        try { localStorage.setItem('sgaem-sidebar-collapsed', collapsed ? 'true' : 'false'); } catch (e) {}
        var btn = document.getElementById('sidebarHamburgerBtn');
        if (btn) btn.setAttribute('aria-label', collapsed ? 'Abrir menu' : 'Fechar menu');
    }
    function openSidebarOverlay() {
        var sb = document.getElementById('appSidebar');
        var ov = document.getElementById('sidebarOverlay');
        if (sb) sb.classList.add('aberto');
        if (ov) { ov.classList.add('ativo'); ov.setAttribute('aria-hidden', 'false'); }
    }
    function closeSidebarOverlay() {
        var sb = document.getElementById('appSidebar');
        var ov = document.getElementById('sidebarOverlay');
        if (sb) sb.classList.remove('aberto');
        if (ov) { ov.classList.remove('ativo'); ov.setAttribute('aria-hidden', 'true'); }
    }
    window.toggleSidebarCollapse = toggleSidebar;
    window.toggleSidebar = function() {
        var w = window.innerWidth || document.documentElement.clientWidth;
        if (w <= 992) {
            var sb = document.getElementById('appSidebar');
            if (sb && sb.classList.contains('aberto')) closeSidebarOverlay();
            else openSidebarOverlay();
        } else {
            toggleSidebar();
        }
    };
    document.addEventListener('DOMContentLoaded', function() {
        var btn = document.getElementById('sidebarHamburgerBtn');
        var overlay = document.getElementById('sidebarOverlay');
        if (btn) {
            btn.addEventListener('click', function(e) {
                e.preventDefault();
                e.stopPropagation();
                var w = window.innerWidth || document.documentElement.clientWidth;
                if (w <= 992) {
                    var sb = document.getElementById('appSidebar');
                    if (sb && sb.classList.contains('aberto')) closeSidebarOverlay();
                    else openSidebarOverlay();
                } else {
                    toggleSidebar();
                }
            });
        }
        if (overlay) overlay.addEventListener('click', function() { closeSidebarOverlay(); });
        try {
            var collapsed = localStorage.getItem('sgaem-sidebar-collapsed') === 'true';
            var layout = getLayout();
            if (layout && collapsed) layout.classList.add('sidebar-collapsed');
        } catch (e) {}
    });
})();
</script>

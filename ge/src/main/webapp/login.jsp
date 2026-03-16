<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    String ctx = request.getContextPath();
%>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Entrar - SGAEM</title>
    <link rel="stylesheet" href="<%= ctx %>/css/app.css?v=2">
    <style>
        /* Estilos do login embutidos para garantir que o modal e o fundo apareçam */
        .login-page {
            min-height: 100vh;
            min-height: 100dvh;
            display: flex !important;
            align-items: center !important;
            justify-content: center !important;
            padding: 2rem;
            position: relative;
            background-color: #0c2d4a;
            background-image: url('https://www.gestaoeplanejamento.com.br/wp-content/uploads/2025/02/Gestao-de-ativos-empresariais.jpg');
            background-size: cover;
            background-position: center;
            background-repeat: no-repeat;
        }
        .login-page__overlay {
            position: absolute;
            top: 0; left: 0; right: 0; bottom: 0;
            background: rgba(12, 45, 74, 0.7);
            backdrop-filter: blur(10px);
            -webkit-backdrop-filter: blur(10px);
            z-index: 0;
        }
        .login-modal {
            position: relative;
            z-index: 1;
            width: 100%;
            max-width: 420px;
            background: #fff;
            border-radius: 16px;
            box-shadow: 0 24px 56px rgba(0, 0, 0, 0.4);
            overflow: hidden;
        }
        .login-modal__header {
            background: linear-gradient(135deg, #0c2d4a 0%, #0f3460 100%);
            padding: 1.75rem 2rem;
            text-align: center;
        }
        .login-modal__title {
            margin: 0 0 0.35rem 0;
            font-size: 1.75rem;
            font-weight: 800;
            color: #fff;
            letter-spacing: -0.03em;
        }
        .login-modal__subtitle {
            margin: 0;
            font-size: 0.8rem;
            color: rgba(255, 255, 255, 0.8);
            line-height: 1.4;
        }
        .login-modal__body {
            padding: 2rem 2rem 2.25rem;
        }
        .login-modal__form-title {
            margin: 0 0 0.35rem 0;
            font-size: 1.35rem;
            font-weight: 700;
            color: #0f172a;
        }
        .login-modal__form-desc {
            margin: 0 0 1.5rem 0;
            font-size: 0.9rem;
            color: #64748b;
        }
        .login-form .form-group {
            margin-bottom: 1.25rem;
        }
        .login-form .form-group label {
            display: block;
            margin-bottom: 0.4rem;
            font-weight: 600;
            color: #0f172a;
            font-size: 0.9rem;
        }
        .login-form .form-group input {
            width: 100%;
            padding: 0.85rem 1rem;
            border: 1px solid #e2e8f0;
            border-radius: 8px;
            font-size: 1rem;
            box-sizing: border-box;
        }
        .login-form .form-group input:focus {
            outline: none;
            border-color: #0d9488;
            box-shadow: 0 0 0 4px rgba(13, 148, 136, 0.15);
        }
        .login-form__submit {
            width: 100%;
            padding: 1rem 1.5rem;
            margin-top: 0.25rem;
            font-size: 1rem;
            font-weight: 600;
            background: #0d9488;
            color: #fff;
            border: none;
            border-radius: 8px;
            cursor: pointer;
        }
        .login-form__submit:hover {
            background: #0f766e;
        }
        .login-modal .alert-error {
            margin-bottom: 1.25rem;
            padding: 1rem 1.25rem;
            border-radius: 8px;
            background: #fee2e2;
            color: #991b1b;
            font-size: 0.95rem;
        }
    </style>
</head>
<body>
    <div class="login-page">
        <div class="login-page__overlay"></div>
        <div class="login-modal">
            <div class="login-modal__header">
                <h1 class="login-modal__title">SGAEM</h1>
                <p class="login-modal__subtitle">Sistema de Gerenciamento de Ativos Empresariais</p>
            </div>
            <div class="login-modal__body">
                <h2 class="login-modal__form-title">Entrar</h2>
                <p class="login-modal__form-desc">Use suas credenciais para acessar o sistema</p>
                <% if (request.getAttribute("erro") != null) { %>
                    <div class="alert alert-error"><%= request.getAttribute("erro") %></div>
                <% } %>
                <form action="<%= ctx %>/usuario" method="post" class="login-form">
                    <div class="form-group">
                        <label for="login">Usuário</label>
                        <input type="text" id="login" name="login" required autofocus placeholder="Seu usuário">
                    </div>
                    <div class="form-group">
                        <label for="senha">Senha</label>
                        <input type="password" id="senha" name="senha" required placeholder="Sua senha">
                    </div>
                    <button type="submit" class="btn btn-primary login-form__submit">Entrar</button>
                </form>
            </div>
        </div>
    </div>
</body>
</html>

package br.com.project.dao;

import br.com.project.model.Usuario;
import br.com.project.util.ConnectionFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para operações de usuário no banco de dados.
 */
public class UsuarioDAO {

    public Usuario buscarPorLoginSenha(String login, String senha) throws SQLException {
        String sql = "SELECT id_usuario, nome_usuario, login_usuario, senha_usuario, tipo_usuario FROM usuario WHERE login_usuario = ? AND senha_usuario = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, login);
            stmt.setString(2, senha);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToUsuario(rs);
                }
            }
        }
        return null;
    }

    public Usuario buscarPorId(int id) throws SQLException {
        String sql = "SELECT id_usuario, nome_usuario, login_usuario, senha_usuario, tipo_usuario FROM usuario WHERE id_usuario = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToUsuario(rs);
                }
            }
        }
        return null;
    }

    public List<Usuario> listarTodos() throws SQLException {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT id_usuario, nome_usuario, login_usuario, senha_usuario, tipo_usuario FROM usuario ORDER BY nome_usuario";
        try (Connection conn = ConnectionFactory.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(mapResultSetToUsuario(rs));
            }
        }
        return lista;
    }

    private Usuario mapResultSetToUsuario(ResultSet rs) throws SQLException {
        Usuario u = new Usuario();
        u.setIdUsuario(rs.getInt("id_usuario"));
        u.setNomeUsuario(rs.getString("nome_usuario"));
        u.setLoginUsuario(rs.getString("login_usuario"));
        u.setSenhaUsuario(rs.getString("senha_usuario"));
        u.setTipoUsuario(rs.getString("tipo_usuario"));
        return u;
    }
}

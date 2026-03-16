package br.com.project.dao;

import br.com.project.model.Material;
import br.com.project.util.ConnectionFactory;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para operações de material no banco de dados.
 * Inclui validação: material já existe (por código).
 */
public class MaterialDAO {

    /**
     * Verifica se já existe um material com o código informado.
     * Se idMaterial > 0, exclui esse id da verificação (para edição).
     */
    public boolean existeMaterialComCodigo(int codigoMaterial, int idMaterialParaExcluir) throws SQLException {
        String sql = idMaterialParaExcluir > 0
            ? "SELECT 1 FROM material WHERE codigo_material = ? AND id_material != ? LIMIT 1"
            : "SELECT 1 FROM material WHERE codigo_material = ? LIMIT 1";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, codigoMaterial);
            if (idMaterialParaExcluir > 0) {
                stmt.setInt(2, idMaterialParaExcluir);
            }
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    public void inserir(Material m) throws SQLException {
        if (existeMaterialComCodigo(m.getCodigoMaterial(), 0)) {
            throw new IllegalArgumentException("Já existe um material cadastrado com o código " + m.getCodigoMaterial() + ".");
        }
        String sql = "INSERT INTO material (codigo_material, descricao_material, unidmedida_material, preco_material, min_material, max_material, qtd_total_material, posicao_almoxarifado) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, m.getCodigoMaterial());
            stmt.setString(2, m.getDescricaoMaterial());
            stmt.setString(3, m.getUnidMedidaMaterial());
            stmt.setBigDecimal(4, m.getPrecoMaterial());
            stmt.setInt(5, m.getMinMaterial());
            stmt.setInt(6, m.getMaxMaterial());
            stmt.setBigDecimal(7, m.getQtdTotalMaterial() != null ? m.getQtdTotalMaterial() : BigDecimal.ZERO);
            stmt.setString(8, m.getPosicaoAlmoxarifado());
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    m.setIdMaterial(rs.getInt(1));
                }
            }
        }
    }

    public void atualizar(Material m) throws SQLException {
        if (existeMaterialComCodigo(m.getCodigoMaterial(), m.getIdMaterial())) {
            throw new IllegalArgumentException("Já existe outro material cadastrado com o código " + m.getCodigoMaterial() + ".");
        }
        String sql = "UPDATE material SET codigo_material = ?, descricao_material = ?, unidmedida_material = ?, preco_material = ?, min_material = ?, max_material = ?, posicao_almoxarifado = ? WHERE id_material = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, m.getCodigoMaterial());
            stmt.setString(2, m.getDescricaoMaterial());
            stmt.setString(3, m.getUnidMedidaMaterial());
            stmt.setBigDecimal(4, m.getPrecoMaterial());
            stmt.setInt(5, m.getMinMaterial());
            stmt.setInt(6, m.getMaxMaterial());
            stmt.setString(7, m.getPosicaoAlmoxarifado());
            stmt.setInt(8, m.getIdMaterial());
            stmt.executeUpdate();
        }
    }

    public void atualizarQuantidade(int idMaterial, BigDecimal novaQuantidade) throws SQLException {
        try (Connection conn = ConnectionFactory.getConnection()) {
            atualizarQuantidade(conn, idMaterial, novaQuantidade);
        }
    }

    /** Atualiza quantidade usando a conexão fornecida (para uso em transação). */
    public void atualizarQuantidade(Connection conn, int idMaterial, BigDecimal novaQuantidade) throws SQLException {
        String sql = "UPDATE material SET qtd_total_material = ? WHERE id_material = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setBigDecimal(1, novaQuantidade);
            stmt.setInt(2, idMaterial);
            stmt.executeUpdate();
        }
    }

    public Material buscarPorId(int id) throws SQLException {
        String sql = "SELECT id_material, codigo_material, descricao_material, unidmedida_material, preco_material, min_material, max_material, qtd_total_material, posicao_almoxarifado FROM material WHERE id_material = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToMaterial(rs);
                }
            }
        }
        return null;
    }

    public Material buscarPorCodigo(int codigo) throws SQLException {
        String sql = "SELECT id_material, codigo_material, descricao_material, unidmedida_material, preco_material, min_material, max_material, qtd_total_material, posicao_almoxarifado FROM material WHERE codigo_material = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, codigo);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToMaterial(rs);
                }
            }
        }
        return null;
    }

    public List<Material> listarTodos() throws SQLException {
        List<Material> lista = new ArrayList<>();
        String sql = "SELECT id_material, codigo_material, descricao_material, unidmedida_material, preco_material, min_material, max_material, qtd_total_material, posicao_almoxarifado FROM material ORDER BY codigo_material";
        try (Connection conn = ConnectionFactory.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(mapResultSetToMaterial(rs));
            }
        }
        return lista;
    }

    public void excluir(int id) throws SQLException {
        String sql = "DELETE FROM material WHERE id_material = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    private Material mapResultSetToMaterial(ResultSet rs) throws SQLException {
        Material m = new Material();
        m.setIdMaterial(rs.getInt("id_material"));
        m.setCodigoMaterial(rs.getInt("codigo_material"));
        m.setDescricaoMaterial(rs.getString("descricao_material"));
        m.setUnidMedidaMaterial(rs.getString("unidmedida_material"));
        m.setPrecoMaterial(rs.getBigDecimal("preco_material"));
        m.setMinMaterial(rs.getInt("min_material"));
        m.setMaxMaterial(rs.getInt("max_material"));
        m.setQtdTotalMaterial(rs.getBigDecimal("qtd_total_material"));
        m.setPosicaoAlmoxarifado(rs.getString("posicao_almoxarifado"));
        return m;
    }
}

package br.com.project.dao;

import br.com.project.model.ConsumoPeriodo;
import br.com.project.model.Movimentacoes;
import br.com.project.util.ConnectionFactory;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para movimentações (entrada e saída).
 * Validações:
 * - Entrada: quantidade atual + entrada não pode ultrapassar max_material.
 * - Saída: material deve ter quantidade > 0 e quantidade da saída não pode ser maior que o estoque.
 */
public class MovimentacoesDAO {

    /**
     * Registra uma movimentação e atualiza o estoque do material.
     * Entrada: valida se (qtd_atual + quantidade) <= max_material.
     * Saída: valida se material tem estoque > 0 e se quantidade <= qtd_atual.
     */
    public void registrarMovimentacao(Movimentacoes mov) throws SQLException {
        MaterialDAO dao = new MaterialDAO();
        br.com.project.model.Material material = dao.buscarPorId(mov.getMaterialIdMaterial());
        if (material == null) {
            throw new IllegalArgumentException("Material não encontrado.");
        }
        BigDecimal qtdAtual = material.getQtdTotalMaterial() != null ? material.getQtdTotalMaterial() : BigDecimal.ZERO;

        if (Movimentacoes.TIPO_SAIDA.equalsIgnoreCase(mov.getTipoMovim())) {
            if (qtdAtual.compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("Não é permitida saída para material com estoque zerado.");
            }
            if (mov.getQuantMovim().compareTo(qtdAtual) > 0) {
                throw new IllegalArgumentException("Quantidade da saída (" + mov.getQuantMovim() + ") é maior que o estoque disponível (" + qtdAtual + ").");
            }
            if (mov.getQuantMovim().compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("Quantidade da saída deve ser maior que zero.");
            }
            BigDecimal novaQtd = qtdAtual.subtract(mov.getQuantMovim());
            try (Connection conn = ConnectionFactory.getConnection()) {
                conn.setAutoCommit(false);
                try {
                    inserirMovimentacao(conn, mov);
                    dao.atualizarQuantidade(conn, mov.getMaterialIdMaterial(), novaQtd);
                    conn.commit();
                } catch (Exception e) {
                    conn.rollback();
                    throw e;
                } finally {
                    conn.setAutoCommit(true);
                }
            }
        } else if (Movimentacoes.TIPO_ENTRADA.equalsIgnoreCase(mov.getTipoMovim())) {
            if (mov.getQuantMovim().compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("Quantidade da entrada deve ser maior que zero.");
            }
            BigDecimal novaQtd = qtdAtual.add(mov.getQuantMovim());
            int max = material.getMaxMaterial();
            if (novaQtd.compareTo(BigDecimal.valueOf(max)) > 0) {
                throw new IllegalArgumentException("A entrada resultaria em estoque (" + novaQtd + ") maior que o máximo permitido (" + max + ") para este material.");
            }
            try (Connection conn = ConnectionFactory.getConnection()) {
                conn.setAutoCommit(false);
                try {
                    inserirMovimentacao(conn, mov);
                    dao.atualizarQuantidade(conn, mov.getMaterialIdMaterial(), novaQtd);
                    conn.commit();
                } catch (Exception e) {
                    conn.rollback();
                    throw e;
                } finally {
                    conn.setAutoCommit(true);
                }
            }
        } else {
            throw new IllegalArgumentException("Tipo de movimentação inválido. Use ENTRADA ou SAIDA.");
        }
    }

    private void inserirMovimentacao(Connection conn, Movimentacoes mov) throws SQLException {
        String sql = "INSERT INTO movimentacoes (usuario_id_usuario, material_id_material, quant_movim, tipo_movim, data_movim, observacoes_movim) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, mov.getUsuarioIdUsuario());
            stmt.setInt(2, mov.getMaterialIdMaterial());
            stmt.setBigDecimal(3, mov.getQuantMovim());
            stmt.setString(4, mov.getTipoMovim());
            stmt.setDate(5, new java.sql.Date(mov.getDataMovim().getTime()));
            stmt.setString(6, mov.getObservacoesMovim());
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    mov.setIdMovim(rs.getInt(1));
                }
            }
        }
    }

    public List<Movimentacoes> listarTodas() throws SQLException {
        List<Movimentacoes> lista = new ArrayList<>();
        String sql = "SELECT m.id_movim, m.usuario_id_usuario, m.material_id_material, m.quant_movim, m.tipo_movim, m.data_movim, m.observacoes_movim, " +
                     "u.nome_usuario, mat.codigo_material, mat.descricao_material " +
                     "FROM movimentacoes m " +
                     "INNER JOIN usuario u ON m.usuario_id_usuario = u.id_usuario " +
                     "INNER JOIN material mat ON m.material_id_material = mat.id_material " +
                     "ORDER BY m.data_movim DESC, m.id_movim DESC";
        try (Connection conn = ConnectionFactory.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Movimentacoes mov = new Movimentacoes();
                mov.setIdMovim(rs.getInt("id_movim"));
                mov.setUsuarioIdUsuario(rs.getInt("usuario_id_usuario"));
                mov.setMaterialIdMaterial(rs.getInt("material_id_material"));
                mov.setQuantMovim(rs.getBigDecimal("quant_movim"));
                mov.setTipoMovim(rs.getString("tipo_movim"));
                mov.setDataMovim(rs.getDate("data_movim"));
                mov.setObservacoesMovim(rs.getString("observacoes_movim"));
                mov.setNomeUsuario(rs.getString("nome_usuario"));
                mov.setCodigoMaterialDesc(String.valueOf(rs.getInt("codigo_material")));
                mov.setDescricaoMaterial(rs.getString("descricao_material"));
                lista.add(mov);
            }
        }
        return lista;
    }

    public List<Movimentacoes> listarPorMaterial(int idMaterial) throws SQLException {
        List<Movimentacoes> lista = new ArrayList<>();
        String sql = "SELECT m.id_movim, m.usuario_id_usuario, m.material_id_material, m.quant_movim, m.tipo_movim, m.data_movim, m.observacoes_movim, u.nome_usuario, mat.codigo_material, mat.descricao_material " +
                     "FROM movimentacoes m INNER JOIN usuario u ON m.usuario_id_usuario = u.id_usuario " +
                     "INNER JOIN material mat ON m.material_id_material = mat.id_material WHERE m.material_id_material = ? ORDER BY m.data_movim DESC";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idMaterial);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Movimentacoes mov = new Movimentacoes();
                    mov.setIdMovim(rs.getInt("id_movim"));
                    mov.setUsuarioIdUsuario(rs.getInt("usuario_id_usuario"));
                    mov.setMaterialIdMaterial(rs.getInt("material_id_material"));
                    mov.setQuantMovim(rs.getBigDecimal("quant_movim"));
                    mov.setTipoMovim(rs.getString("tipo_movim"));
                    mov.setDataMovim(rs.getDate("data_movim"));
                    mov.setObservacoesMovim(rs.getString("observacoes_movim"));
                    mov.setNomeUsuario(rs.getString("nome_usuario"));
                    mov.setCodigoMaterialDesc(String.valueOf(rs.getInt("codigo_material")));
                    mov.setDescricaoMaterial(rs.getString("descricao_material"));
                    lista.add(mov);
                }
            }
        }
        return lista;
    }

    /**
     * Retorna o consumo total (soma de quant_movim) e a quantidade de movimentações
     * de SAÍDA do material a partir de dataInicio (inclusive).
     */
    public ConsumoPeriodo getConsumoSaidaPorPeriodo(int idMaterial, java.sql.Date dataInicio) throws SQLException {
        String sql = "SELECT COALESCE(SUM(quant_movim), 0) AS consumo_total, COUNT(*) AS num_movimentos " +
                     "FROM movimentacoes WHERE material_id_material = ? AND tipo_movim = 'SAIDA' AND data_movim >= ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idMaterial);
            stmt.setDate(2, dataInicio);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new ConsumoPeriodo(rs.getDouble("consumo_total"), rs.getLong("num_movimentos"));
                }
            }
        }
        return new ConsumoPeriodo(0, 0);
    }
}

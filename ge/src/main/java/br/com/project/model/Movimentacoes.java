package br.com.project.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Modelo que representa uma movimentação (entrada ou saída) de material.
 */
public class Movimentacoes implements Serializable {
    private static final long serialVersionUID = 1L;

    public static final String TIPO_ENTRADA = "ENTRADA";
    public static final String TIPO_SAIDA = "SAIDA";

    private int idMovim;
    private int usuarioIdUsuario;
    private int materialIdMaterial;
    private BigDecimal quantMovim;
    private String tipoMovim;
    private Date dataMovim;
    private String observacoesMovim;

    // Dados auxiliares para exibição (não persistidos)
    private String nomeUsuario;
    private String descricaoMaterial;
    private String codigoMaterialDesc;

    public Movimentacoes() {
    }

    public int getIdMovim() {
        return idMovim;
    }

    public void setIdMovim(int idMovim) {
        this.idMovim = idMovim;
    }

    public int getUsuarioIdUsuario() {
        return usuarioIdUsuario;
    }

    public void setUsuarioIdUsuario(int usuarioIdUsuario) {
        this.usuarioIdUsuario = usuarioIdUsuario;
    }

    public int getMaterialIdMaterial() {
        return materialIdMaterial;
    }

    public void setMaterialIdMaterial(int materialIdMaterial) {
        this.materialIdMaterial = materialIdMaterial;
    }

    public BigDecimal getQuantMovim() {
        return quantMovim;
    }

    public void setQuantMovim(BigDecimal quantMovim) {
        this.quantMovim = quantMovim;
    }

    public String getTipoMovim() {
        return tipoMovim;
    }

    public void setTipoMovim(String tipoMovim) {
        this.tipoMovim = tipoMovim;
    }

    public Date getDataMovim() {
        return dataMovim;
    }

    public void setDataMovim(Date dataMovim) {
        this.dataMovim = dataMovim;
    }

    public String getObservacoesMovim() {
        return observacoesMovim;
    }

    public void setObservacoesMovim(String observacoesMovim) {
        this.observacoesMovim = observacoesMovim;
    }

    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public void setNomeUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
    }

    public String getDescricaoMaterial() {
        return descricaoMaterial;
    }

    public void setDescricaoMaterial(String descricaoMaterial) {
        this.descricaoMaterial = descricaoMaterial;
    }

    public String getCodigoMaterialDesc() {
        return codigoMaterialDesc;
    }

    public void setCodigoMaterialDesc(String codigoMaterialDesc) {
        this.codigoMaterialDesc = codigoMaterialDesc;
    }
}

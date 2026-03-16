package br.com.project.model;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Modelo que representa um material no estoque.
 */
public class Material implements Serializable {
    private static final long serialVersionUID = 1L;

    private int idMaterial;
    private int codigoMaterial;
    private String descricaoMaterial;
    private String unidMedidaMaterial;
    private BigDecimal precoMaterial;
    private int minMaterial;
    private int maxMaterial;
    private BigDecimal qtdTotalMaterial;
    private String posicaoAlmoxarifado;

    public Material() {
        this.qtdTotalMaterial = BigDecimal.ZERO;
    }

    public int getIdMaterial() {
        return idMaterial;
    }

    public void setIdMaterial(int idMaterial) {
        this.idMaterial = idMaterial;
    }

    public int getCodigoMaterial() {
        return codigoMaterial;
    }

    public void setCodigoMaterial(int codigoMaterial) {
        this.codigoMaterial = codigoMaterial;
    }

    public String getDescricaoMaterial() {
        return descricaoMaterial;
    }

    public void setDescricaoMaterial(String descricaoMaterial) {
        this.descricaoMaterial = descricaoMaterial;
    }

    public String getUnidMedidaMaterial() {
        return unidMedidaMaterial;
    }

    public void setUnidMedidaMaterial(String unidMedidaMaterial) {
        this.unidMedidaMaterial = unidMedidaMaterial;
    }

    public BigDecimal getPrecoMaterial() {
        return precoMaterial;
    }

    public void setPrecoMaterial(BigDecimal precoMaterial) {
        this.precoMaterial = precoMaterial;
    }

    public int getMinMaterial() {
        return minMaterial;
    }

    public void setMinMaterial(int minMaterial) {
        this.minMaterial = minMaterial;
    }

    public int getMaxMaterial() {
        return maxMaterial;
    }

    public void setMaxMaterial(int maxMaterial) {
        this.maxMaterial = maxMaterial;
    }

    public BigDecimal getQtdTotalMaterial() {
        return qtdTotalMaterial;
    }

    public void setQtdTotalMaterial(BigDecimal qtdTotalMaterial) {
        this.qtdTotalMaterial = qtdTotalMaterial;
    }

    public String getPosicaoAlmoxarifado() {
        return posicaoAlmoxarifado;
    }

    public void setPosicaoAlmoxarifado(String posicaoAlmoxarifado) {
        this.posicaoAlmoxarifado = posicaoAlmoxarifado;
    }
}

package br.com.project.service;

import br.com.project.model.ConsumoPeriodo;
import br.com.project.model.Material;

import java.math.BigDecimal;
import java.util.Locale;

/**
 * Serviço para cálculo de parâmetros de estoque (mínimo e máximo sugeridos)
 * utilizando distribuição de Poisson com base no histórico de saídas.
 */
public class ParametrosEstoqueService {

    /** Lead time padrão em dias (configurável no futuro). */
    public static final int LEAD_TIME_DIAS = 30;

    /** Nível de serviço para itens normais (95%). */
    public static final double NIVEL_SERVICO = 0.95;

    /**
     * Consumo médio diário = consumo_total / número de dias no período.
     */
    public double calcularConsumoMedioDia(double consumoTotal, long numeroDiasPeriodo) {
        if (numeroDiasPeriodo <= 0) return 0;
        return consumoTotal / numeroDiasPeriodo;
    }

    /**
     * Demanda durante o lead time (consumo médio diário * lead time).
     */
    public double calcularDemandaLT(double consumoMedioDia) {
        return consumoMedioDia * LEAD_TIME_DIAS;
    }

    /**
     * Desvio padrão assumindo distribuição de Poisson: sqrt(demanda_lt).
     */
    public double calcularDesvioPadrao(double demandaLT) {
        return Math.sqrt(Math.max(0, demandaLT));
    }

    /**
     * Encontra o menor inteiro x tal que P(X <= x) >= nivelServico para X ~ Poisson(lambda).
     * Retorna esse x (estoque mínimo pela Poisson).
     */
    public int calcularEstoqueMinPoisson(double lambda) {
        if (lambda <= 0) return 0;
        double acum = 0;
        int k = 0;
        while (acum < NIVEL_SERVICO && k <= 1000) {
            acum += probabilidadePoisson(lambda, k);
            if (acum >= NIVEL_SERVICO) return k;
            k++;
        }
        return Math.max(0, k - 1);
    }

    /** P(X = k) para X ~ Poisson(lambda). */
    private double probabilidadePoisson(double lambda, int k) {
        if (k < 0) return 0;
        if (lambda == 0) return k == 0 ? 1 : 0;
        double termo = Math.exp(-lambda);
        for (int i = 1; i <= k; i++) {
            termo *= lambda / i;
        }
        return termo;
    }

    /**
     * Estoque mínimo do sistema = estoque_min_poisson + 1.
     * Regras de segurança: nunca 0; se consumo muito baixo, limitar.
     */
    public int calcularEstoqueMinSistema(int estoqueMinPoisson, double consumoMedioDia) {
        int min = estoqueMinPoisson + 1;
        if (min < 1) min = 1;
        if (consumoMedioDia >= 0 && consumoMedioDia <= 0.5 && min > 2) {
            min = 2;
        }
        return min;
    }

    /**
     * Estoque máximo sugerido = estoque_min_sistema + demanda_lt (arredondado).
     * Regras: se mínimo for 1, máximo só pode ser 1 ou 2.
     */
    public int calcularEstoqueMaxSugerido(int estoqueMinSistema, double demandaLT) {
        int max = estoqueMinSistema + (int) Math.round(demandaLT);
        if (estoqueMinSistema == 1 && max > 2) max = 2;
        if (max < estoqueMinSistema) max = estoqueMinSistema;
        return max;
    }

    /**
     * Resultado completo do cálculo para exibição.
     */
    public static class ResultadoParametros {
        public String descricaoMaterial;
        public double consumoMedioDia;
        public int leadTime;
        public double demandaLT;
        public double desvioPadrao;
        public int estoqueMinAtual;
        public int estoqueMaxAtual;
        public int estoqueMinSugerido;
        public int estoqueMaxSugerido;

        public String getConsumoMedioDiaFormatado() {
            return String.format(Locale.ROOT, "%.4f", consumoMedioDia);
        }
        public String getDemandaLTFormatado() {
            return String.format(Locale.ROOT, "%.2f", demandaLT);
        }
        public String getDesvioPadraoFormatado() {
            return String.format(Locale.ROOT, "%.2f", desvioPadrao);
        }
    }

    /**
     * Calcula todos os parâmetros de estoque para o material e período dados.
     */
    public ResultadoParametros calcularParametrosEstoque(Material material, ConsumoPeriodo consumo, long numeroDiasPeriodo) {
        ResultadoParametros r = new ResultadoParametros();
        r.descricaoMaterial = material.getDescricaoMaterial() != null ? material.getDescricaoMaterial() : "";
        r.leadTime = LEAD_TIME_DIAS;

        double consumoTotal = consumo.getConsumoTotal();
        r.consumoMedioDia = calcularConsumoMedioDia(consumoTotal, numeroDiasPeriodo);
        r.demandaLT = calcularDemandaLT(r.consumoMedioDia);
        r.desvioPadrao = calcularDesvioPadrao(r.demandaLT);

        r.estoqueMinAtual = material.getMinMaterial();
        r.estoqueMaxAtual = material.getMaxMaterial();

        int minPoisson = calcularEstoqueMinPoisson(r.demandaLT);
        r.estoqueMinSugerido = calcularEstoqueMinSistema(minPoisson, r.consumoMedioDia);
        r.estoqueMaxSugerido = calcularEstoqueMaxSugerido(r.estoqueMinSugerido, r.demandaLT);

        return r;
    }
}

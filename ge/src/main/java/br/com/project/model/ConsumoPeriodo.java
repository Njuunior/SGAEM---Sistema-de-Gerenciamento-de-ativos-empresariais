package br.com.project.model;

/**
 * DTO com resultado de consumo (saídas) em um período.
 */
public class ConsumoPeriodo {
    private double consumoTotal;
    private long numMovimentos;

    public ConsumoPeriodo() {}

    public ConsumoPeriodo(double consumoTotal, long numMovimentos) {
        this.consumoTotal = consumoTotal;
        this.numMovimentos = numMovimentos;
    }

    public double getConsumoTotal() { return consumoTotal; }
    public void setConsumoTotal(double consumoTotal) { this.consumoTotal = consumoTotal; }
    public long getNumMovimentos() { return numMovimentos; }
    public void setNumMovimentos(long numMovimentos) { this.numMovimentos = numMovimentos; }
}

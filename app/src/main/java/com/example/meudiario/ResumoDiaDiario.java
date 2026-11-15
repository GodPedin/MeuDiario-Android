package com.example.meudiario;

/**
 * Representa um dia exibido na Tela Inicial em que o usuário anotou algo no diário.
 */
public class ResumoDiaDiario {

    private String dataDia;             // Ex: "14/11/2026"
    private String dataComDiaDaSemana;  // Ex: "14/11/2026 (Sábado)"
    private String diaNumero;           // Ex: "14"
    private String mesAbreviado;        // Ex: "NOV"
    private int quantidadePaginas;      // Quantas páginas foram criadas neste dia
    private String resumoAnotacoes;     // Prévia do que foi anotado neste dia
    private long idPrimeiraPaginaDoDia; // ID da página para abrir quando o usuário tocar no dia

    public ResumoDiaDiario(String dataDia, String dataComDiaDaSemana,
                           String diaNumero, String mesAbreviado,
                           int quantidadePaginas, String resumoAnotacoes,
                           long idPrimeiraPaginaDoDia) {
        this.dataDia = dataDia;
        this.dataComDiaDaSemana = dataComDiaDaSemana;
        this.diaNumero = diaNumero;
        this.mesAbreviado = mesAbreviado;
        this.quantidadePaginas = quantidadePaginas;
        this.resumoAnotacoes = resumoAnotacoes;
        this.idPrimeiraPaginaDoDia = idPrimeiraPaginaDoDia;
    }

    public String getDataDia() {
        return dataDia;
    }

    public String getDataComDiaDaSemana() {
        return dataComDiaDaSemana;
    }

    public String getDiaNumero() {
        return diaNumero;
    }

    public String getMesAbreviado() {
        return mesAbreviado;
    }

    public int getQuantidadePaginas() {
        return quantidadePaginas;
    }

    public String getResumoAnotacoes() {
        return resumoAnotacoes;
    }

    public long getIdPrimeiraPaginaDoDia() {
        return idPrimeiraPaginaDoDia;
    }
}

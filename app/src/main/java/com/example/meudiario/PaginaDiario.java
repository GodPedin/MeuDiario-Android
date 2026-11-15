package com.example.meudiario;

/**
 * Representa uma página individual de anotação no diário.
 * Cada página guarda o dia em que foi criada, título, conteúdo e humor.
 */
public class PaginaDiario {

    private long id;
    private String dataDia;             // Ex: "14/11/2026" (usado para agrupar os dias na tela inicial)
    private String dataCriacaoCompleta; // Ex: "14/11/2026 (Sábado) às 21:15" (exibido no topo da página)
    private String diaNumero;           // Ex: "14"
    private String mesAbreviado;        // Ex: "NOV"
    private String titulo;
    private String conteudo;
    private String humor;

    public PaginaDiario() {
    }

    public PaginaDiario(long id, String dataDia, String dataCriacaoCompleta,
                        String diaNumero, String mesAbreviado,
                        String titulo, String conteudo, String humor) {
        this.id = id;
        this.dataDia = dataDia;
        this.dataCriacaoCompleta = dataCriacaoCompleta;
        this.diaNumero = diaNumero;
        this.mesAbreviado = mesAbreviado;
        this.titulo = titulo;
        this.conteudo = conteudo;
        this.humor = humor;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDataDia() {
        return dataDia;
    }

    public void setDataDia(String dataDia) {
        this.dataDia = dataDia;
    }

    public String getDataCriacaoCompleta() {
        return dataCriacaoCompleta;
    }

    public void setDataCriacaoCompleta(String dataCriacaoCompleta) {
        this.dataCriacaoCompleta = dataCriacaoCompleta;
    }

    public String getDiaNumero() {
        return diaNumero;
    }

    public void setDiaNumero(String diaNumero) {
        this.diaNumero = diaNumero;
    }

    public String getMesAbreviado() {
        return mesAbreviado;
    }

    public void setMesAbreviado(String mesAbreviado) {
        this.mesAbreviado = mesAbreviado;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getConteudo() {
        return conteudo;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }

    public String getHumor() {
        return humor;
    }

    public void setHumor(String humor) {
        this.humor = humor;
    }
}

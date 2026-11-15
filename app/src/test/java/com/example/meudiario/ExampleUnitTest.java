package com.example.meudiario;

import org.junit.Test;
import static org.junit.Assert.*;

public class ExampleUnitTest {
    @Test
    public void criacaoPaginaComDataDeHoje_preencheCamposDeData() {
        PaginaDiario pagina = BancoDadosDiario.criarPaginaComDataDeHoje();
        assertNotNull(pagina.getDataDia());
        assertFalse(pagina.getDataDia().isEmpty());
        assertNotNull(pagina.getDataCriacaoCompleta());
        assertTrue(pagina.getDataCriacaoCompleta().contains(pagina.getDataDia()));
    }
}

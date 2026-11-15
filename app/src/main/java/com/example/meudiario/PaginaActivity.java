package com.example.meudiario;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * TELA DE PÁGINA DO DIÁRIO:
 * Exibe a folha de anotação mostrando no topo o dia em que a página foi criada,
 * permite escrever e salvar anotações, e permite folhear entre páginas (Anterior / Próxima).
 */
public class PaginaActivity extends AppCompatActivity {

    public static final String EXTRA_ID_PAGINA_INICIAL = "extra_id_pagina_inicial";
    public static final String EXTRA_CRIAR_NOVA_PAGINA = "extra_criar_nova_pagina";

    private BancoDadosDiario bancoDados;
    private final List<PaginaDiario> listaPaginas = new ArrayList<>();
    private int indicePaginaAtual = 0;

    // Componentes visuais do XML (res/layout/activity_pagina.xml)
    private TextView textViewNumeroPagina;
    private TextView textViewDataCriacaoPagina;
    private TextView textViewIndiceFolha;
    private RadioGroup radioGroupHumor;
    private RadioButton radioHumorFeliz;
    private RadioButton radioHumorTranquilo;
    private RadioButton radioHumorPensativo;
    private RadioButton radioHumorGrato;
    private EditText editTextTituloPagina;
    private EditText editTextConteudoPagina;
    private Button buttonPaginaAnterior;
    private Button buttonProximaPagina;
    private Button buttonExcluirPagina;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pagina);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.paginaRoot), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        bancoDados = new BancoDadosDiario(this);

        // Inicializa componentes do layout XML
        Button buttonVoltarInicio = findViewById(R.id.buttonVoltarInicio);
        Button buttonNovaPaginaTopo = findViewById(R.id.buttonNovaPaginaTopo);
        textViewNumeroPagina = findViewById(R.id.textViewNumeroPagina);
        textViewDataCriacaoPagina = findViewById(R.id.textViewDataCriacaoPagina);
        textViewIndiceFolha = findViewById(R.id.textViewIndiceFolha);
        radioGroupHumor = findViewById(R.id.radioGroupHumor);
        radioHumorFeliz = findViewById(R.id.radioHumorFeliz);
        radioHumorTranquilo = findViewById(R.id.radioHumorTranquilo);
        radioHumorPensativo = findViewById(R.id.radioHumorPensativo);
        radioHumorGrato = findViewById(R.id.radioHumorGrato);
        editTextTituloPagina = findViewById(R.id.editTextTituloPagina);
        editTextConteudoPagina = findViewById(R.id.editTextConteudoPagina);
        buttonPaginaAnterior = findViewById(R.id.buttonPaginaAnterior);
        buttonProximaPagina = findViewById(R.id.buttonProximaPagina);
        buttonExcluirPagina = findViewById(R.id.buttonExcluirPagina);
        Button buttonSalvarPagina = findViewById(R.id.buttonSalvarPagina);

        // Carrega todas as páginas salvas no diário
        listaPaginas.addAll(bancoDados.listarTodasPaginas());

        boolean criarNova = getIntent().getBooleanExtra(EXTRA_CRIAR_NOVA_PAGINA, false);
        long idPaginaInicial = getIntent().getLongExtra(EXTRA_ID_PAGINA_INICIAL, -1);

        if (criarNova || listaPaginas.isEmpty()) {
            adicionarNovaPaginaEmBranco();
        } else {
            indicePaginaAtual = encontrarIndicePorId(idPaginaInicial);
            exibirPaginaAtual();
        }

        // Botão para voltar à Tela Inicial (lista de dias anotados)
        buttonVoltarInicio.setOnClickListener(v -> finish());

        // Botão no topo para criar mais uma página no diário
        buttonNovaPaginaTopo.setOnClickListener(v -> {
            salvarSilenciosamenteSePreenchido();
            adicionarNovaPaginaEmBranco();
            Toast.makeText(this, "Nova página criada com a data de hoje!", Toast.LENGTH_SHORT).show();
        });

        // Navegação: Página Anterior
        buttonPaginaAnterior.setOnClickListener(v -> {
            if (indicePaginaAtual > 0) {
                salvarSilenciosamenteSePreenchido();
                indicePaginaAtual--;
                exibirPaginaAtual();
            }
        });

        // Navegação: Próxima Página
        buttonProximaPagina.setOnClickListener(v -> {
            if (indicePaginaAtual < listaPaginas.size() - 1) {
                salvarSilenciosamenteSePreenchido();
                indicePaginaAtual++;
                exibirPaginaAtual();
            }
        });

        // Botão Salvar Página
        buttonSalvarPagina.setOnClickListener(v -> salvarPaginaAtualComAviso());

        // Botão Excluir Página
        buttonExcluirPagina.setOnClickListener(v -> confirmarExclusaoPaginaAtual());
    }

    /**
     * Cria uma nova página carimbada com o dia atual e posiciona o diário nela.
     */
    private void adicionarNovaPaginaEmBranco() {
        PaginaDiario novaPagina = BancoDadosDiario.criarPaginaComDataDeHoje();
        listaPaginas.add(novaPagina);
        indicePaginaAtual = listaPaginas.size() - 1;
        exibirPaginaAtual();
        editTextConteudoPagina.requestFocus();
    }

    /**
     * Procura na lista de páginas o índice correspondente ao ID clicado na Tela Inicial.
     */
    private int encontrarIndicePorId(long idProcurado) {
        if (idProcurado == -1) {
            return listaPaginas.size() - 1;
        }
        for (int i = 0; i < listaPaginas.size(); i++) {
            if (listaPaginas.get(i).getId() == idProcurado) {
                return i;
            }
        }
        return listaPaginas.size() - 1;
    }

    /**
     * Atualiza a interface XML com os dados da página atual (incluindo o dia em que foi criada).
     */
    private void exibirPaginaAtual() {
        PaginaDiario pagina = listaPaginas.get(indicePaginaAtual);
        int numeroFolha = indicePaginaAtual + 1;
        int totalFolhas = listaPaginas.size();

        textViewNumeroPagina.setText("Página " + numeroFolha + " de " + totalFolhas);
        textViewIndiceFolha.setText("Folha " + numeroFolha);

        // Mostra claramente o dia em que esta página foi criada
        textViewDataCriacaoPagina.setText(pagina.getDataCriacaoCompleta());

        editTextTituloPagina.setText(pagina.getTitulo());
        editTextConteudoPagina.setText(pagina.getConteudo());
        selecionarRadioHumor(pagina.getHumor());

        // Habilita/desabilita botões de folhear conforme a página atual
        buttonPaginaAnterior.setEnabled(indicePaginaAtual > 0);
        buttonProximaPagina.setEnabled(indicePaginaAtual < listaPaginas.size() - 1);
    }

    /**
     * Salva a anotação da página atual no banco de dados e informa o usuário.
     */
    private void salvarPaginaAtualComAviso() {
        String titulo = editTextTituloPagina.getText().toString().trim();
        String conteudo = editTextConteudoPagina.getText().toString().trim();

        if (conteudo.isEmpty() && titulo.isEmpty()) {
            Toast.makeText(this, "Escreva algo na página antes de salvar!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (conteudo.isEmpty()) {
            conteudo = titulo;
        }

        PaginaDiario pagina = listaPaginas.get(indicePaginaAtual);
        pagina.setTitulo(titulo);
        pagina.setConteudo(conteudo);
        pagina.setHumor(obterHumorSelecionado());

        if (pagina.getId() == -1) {
            bancoDados.inserirPagina(pagina);
        } else {
            bancoDados.atualizarPagina(pagina);
        }

        Toast.makeText(this,
                "Página salva! Dia registrado: " + pagina.getDataDia(),
                Toast.LENGTH_SHORT).show();
    }

    /**
     * Se o usuário escreveu algo e trocou de página, salva automaticamente para não perder o texto.
     */
    private void salvarSilenciosamenteSePreenchido() {
        if (listaPaginas.isEmpty() || indicePaginaAtual < 0 || indicePaginaAtual >= listaPaginas.size()) {
            return;
        }
        String titulo = editTextTituloPagina.getText().toString().trim();
        String conteudo = editTextConteudoPagina.getText().toString().trim();

        if (titulo.isEmpty() && conteudo.isEmpty()) {
            return;
        }
        if (conteudo.isEmpty()) {
            conteudo = titulo;
        }

        PaginaDiario pagina = listaPaginas.get(indicePaginaAtual);
        pagina.setTitulo(titulo);
        pagina.setConteudo(conteudo);
        pagina.setHumor(obterHumorSelecionado());

        if (pagina.getId() == -1) {
            bancoDados.inserirPagina(pagina);
        } else {
            bancoDados.atualizarPagina(pagina);
        }
    }

    /**
     * Pergunta ao usuário se deseja excluir a página atual do diário.
     */
    private void confirmarExclusaoPaginaAtual() {
        PaginaDiario pagina = listaPaginas.get(indicePaginaAtual);

        new AlertDialog.Builder(this)
                .setTitle("Excluir Página")
                .setMessage("Deseja excluir esta página criada em " + pagina.getDataDia() + "?")
                .setPositiveButton("Excluir", (dialog, which) -> {
                    if (pagina.getId() != -1) {
                        bancoDados.excluirPagina(pagina.getId());
                    }
                    listaPaginas.remove(indicePaginaAtual);
                    Toast.makeText(this, "Página excluída.", Toast.LENGTH_SHORT).show();

                    if (listaPaginas.isEmpty()) {
                        finish();
                    } else {
                        if (indicePaginaAtual >= listaPaginas.size()) {
                            indicePaginaAtual = listaPaginas.size() - 1;
                        }
                        exibirPaginaAtual();
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private String obterHumorSelecionado() {
        int idSelecionado = radioGroupHumor.getCheckedRadioButtonId();
        if (idSelecionado == R.id.radioHumorTranquilo) {
            return "Calmo";
        } else if (idSelecionado == R.id.radioHumorPensativo) {
            return "Pensativo";
        } else if (idSelecionado == R.id.radioHumorGrato) {
            return "Grato";
        }
        return "Feliz";
    }

    private void selecionarRadioHumor(String humor) {
        if ("Calmo".equalsIgnoreCase(humor)) {
            radioHumorTranquilo.setChecked(true);
        } else if ("Pensativo".equalsIgnoreCase(humor)) {
            radioHumorPensativo.setChecked(true);
        } else if ("Grato".equalsIgnoreCase(humor)) {
            radioHumorGrato.setChecked(true);
        } else {
            radioHumorFeliz.setChecked(true);
        }
    }
}

package com.example.meudiario;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * TELA INICIAL DO DIÁRIO:
 * Exibe os dias em que o usuário anotou algo no diário, o resumo de dias/páginas
 * e permite abrir as páginas de qualquer dia ou criar uma nova página.
 */
public class MainActivity extends AppCompatActivity {

    private BancoDadosDiario bancoDados;
    private RecyclerView recyclerViewDiasAnotados;
    private LinearLayout layoutEstadoVazio;
    private TextView textViewTotalDias;
    private TextView textViewTotalPaginas;
    private TextView textViewDataHoje;

    private final List<ResumoDiaDiario> listaDiasAnotados = new ArrayList<>();
    private DiasAdapter diasAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        bancoDados = new BancoDadosDiario(this);

        // Referências aos componentes do layout XML (res/layout/activity_main.xml)
        textViewTotalDias = findViewById(R.id.textViewTotalDias);
        textViewTotalPaginas = findViewById(R.id.textViewTotalPaginas);
        textViewDataHoje = findViewById(R.id.textViewDataHoje);
        recyclerViewDiasAnotados = findViewById(R.id.recyclerViewDiasAnotados);
        layoutEstadoVazio = findViewById(R.id.layoutEstadoVazio);
        Button buttonNovaPagina = findViewById(R.id.buttonNovaPagina);

        // Mostra a data de hoje no subtítulo da lista
        String hoje = new SimpleDateFormat("dd/MM/yyyy", new Locale("pt", "BR")).format(new Date());
        textViewDataHoje.setText("Hoje: " + hoje);

        // Configura o RecyclerView que mostra os dias com anotações
        recyclerViewDiasAnotados.setLayoutManager(new LinearLayoutManager(this));
        diasAdapter = new DiasAdapter(listaDiasAnotados, diaSelecionado -> {
            // Ao tocar em um dia da lista, abre a tela de páginas na página daquele dia
            Intent intent = new Intent(MainActivity.this, PaginaActivity.class);
            intent.putExtra(PaginaActivity.EXTRA_ID_PAGINA_INICIAL, diaSelecionado.getIdPrimeiraPaginaDoDia());
            intent.putExtra(PaginaActivity.EXTRA_CRIAR_NOVA_PAGINA, false);
            startActivity(intent);
        });
        recyclerViewDiasAnotados.setAdapter(diasAdapter);

        // Botão para criar uma nova página no diário
        buttonNovaPagina.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, PaginaActivity.class);
            intent.putExtra(PaginaActivity.EXTRA_CRIAR_NOVA_PAGINA, true);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Atualiza a lista de dias sempre que o usuário volta para a Tela Inicial
        carregarDiasComAnotacoes();
    }

    private void carregarDiasComAnotacoes() {
        List<PaginaDiario> todasPaginas = bancoDados.listarTodasPaginas();
        List<ResumoDiaDiario> dias = bancoDados.listarDiasComAnotacoes();

        listaDiasAnotados.clear();
        listaDiasAnotados.addAll(dias);
        diasAdapter.notifyDataSetChanged();

        // Atualiza os contadores no painel superior
        textViewTotalDias.setText(String.valueOf(dias.size()));
        textViewTotalPaginas.setText(String.valueOf(todasPaginas.size()));

        // Alterna entre a lista de dias e a mensagem de diário vazio
        if (listaDiasAnotados.isEmpty()) {
            recyclerViewDiasAnotados.setVisibility(View.GONE);
            layoutEstadoVazio.setVisibility(View.VISIBLE);
        } else {
            recyclerViewDiasAnotados.setVisibility(View.VISIBLE);
            layoutEstadoVazio.setVisibility(View.GONE);
        }
    }
}

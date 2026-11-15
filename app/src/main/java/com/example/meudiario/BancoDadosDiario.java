package com.example.meudiario;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Banco de dados SQLite local para salvar as páginas do diário e consultar
 * os dias em que o usuário anotou algo.
 */
public class BancoDadosDiario extends SQLiteOpenHelper {

    private static final String NOME_BANCO = "meu_diario.db";
    private static final int VERSAO_BANCO = 1;

    private static final String TABELA_PAGINAS = "paginas_diario";
    private static final String COL_ID = "id";
    private static final String COL_DATA_DIA = "data_dia";
    private static final String COL_DATA_CRIACAO_COMPLETA = "data_criacao_completa";
    private static final String COL_DIA_NUMERO = "dia_numero";
    private static final String COL_MES_ABREVIADO = "mes_abreviado";
    private static final String COL_TITULO = "titulo";
    private static final String COL_CONTEUDO = "conteudo";
    private static final String COL_HUMOR = "humor";

    public BancoDadosDiario(Context context) {
        super(context, NOME_BANCO, null, VERSAO_BANCO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlCriarTabela = "CREATE TABLE " + TABELA_PAGINAS + " ("
                + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_DATA_DIA + " TEXT NOT NULL, "
                + COL_DATA_CRIACAO_COMPLETA + " TEXT NOT NULL, "
                + COL_DIA_NUMERO + " TEXT NOT NULL, "
                + COL_MES_ABREVIADO + " TEXT NOT NULL, "
                + COL_TITULO + " TEXT, "
                + COL_CONTEUDO + " TEXT NOT NULL, "
                + COL_HUMOR + " TEXT"
                + ")";
        db.execSQL(sqlCriarTabela);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABELA_PAGINAS);
        onCreate(db);
    }

    /**
     * Cria uma nova instância de PaginaDiario já preenchida com a data de criação de hoje.
     */
    public static PaginaDiario criarPaginaComDataDeHoje() {
        Locale localeBrasil = new Locale("pt", "BR");
        Date agora = new Date();

        String dataDia = new SimpleDateFormat("dd/MM/yyyy", localeBrasil).format(agora);
        String diaSemana = new SimpleDateFormat("EEEE", localeBrasil).format(agora);
        // Capitaliza a primeira letra do dia da semana
        if (!diaSemana.isEmpty()) {
            diaSemana = diaSemana.substring(0, 1).toUpperCase(localeBrasil) + diaSemana.substring(1);
        }
        String horaMinuto = new SimpleDateFormat("HH:mm", localeBrasil).format(agora);
        String dataCriacaoCompleta = dataDia + " (" + diaSemana + ") às " + horaMinuto;

        String diaNumero = new SimpleDateFormat("dd", localeBrasil).format(agora);
        String mesAbreviado = new SimpleDateFormat("MMM", localeBrasil).format(agora)
                .replace(".", "")
                .toUpperCase(localeBrasil);

        PaginaDiario novaPagina = new PaginaDiario();
        novaPagina.setId(-1);
        novaPagina.setDataDia(dataDia);
        novaPagina.setDataCriacaoCompleta(dataCriacaoCompleta);
        novaPagina.setDiaNumero(diaNumero);
        novaPagina.setMesAbreviado(mesAbreviado);
        novaPagina.setTitulo("");
        novaPagina.setConteudo("");
        novaPagina.setHumor("Feliz");
        return novaPagina;
    }

    /**
     * Insere uma nova página no diário e retorna o ID gerado.
     */
    public long inserirPagina(PaginaDiario pagina) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put(COL_DATA_DIA, pagina.getDataDia());
        valores.put(COL_DATA_CRIACAO_COMPLETA, pagina.getDataCriacaoCompleta());
        valores.put(COL_DIA_NUMERO, pagina.getDiaNumero());
        valores.put(COL_MES_ABREVIADO, pagina.getMesAbreviado());
        valores.put(COL_TITULO, pagina.getTitulo());
        valores.put(COL_CONTEUDO, pagina.getConteudo());
        valores.put(COL_HUMOR, pagina.getHumor());

        long idGerado = db.insert(TABELA_PAGINAS, null, valores);
        pagina.setId(idGerado);
        return idGerado;
    }

    /**
     * Atualiza o título, conteúdo e humor de uma página existente (mantendo o dia em que foi criada).
     */
    public void atualizarPagina(PaginaDiario pagina) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put(COL_TITULO, pagina.getTitulo());
        valores.put(COL_CONTEUDO, pagina.getConteudo());
        valores.put(COL_HUMOR, pagina.getHumor());

        db.update(TABELA_PAGINAS, valores, COL_ID + " = ?",
                new String[]{String.valueOf(pagina.getId())});
    }

    /**
     * Exclui uma página do diário pelo seu ID.
     */
    public void excluirPagina(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABELA_PAGINAS, COL_ID + " = ?", new String[]{String.valueOf(id)});
    }

    /**
     * Retorna todas as páginas do diário em ordem de criação (Página 1, Página 2, ...).
     */
    public List<PaginaDiario> listarTodasPaginas() {
        List<PaginaDiario> lista = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                TABELA_PAGINAS,
                null,
                null,
                null,
                null,
                null,
                COL_ID + " ASC"
        );

        if (cursor != null) {
            while (cursor.moveToNext()) {
                PaginaDiario pagina = new PaginaDiario(
                        cursor.getLong(cursor.getColumnIndexOrThrow(COL_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_DATA_DIA)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_DATA_CRIACAO_COMPLETA)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_DIA_NUMERO)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_MES_ABREVIADO)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_TITULO)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_CONTEUDO)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_HUMOR))
                );
                lista.add(pagina);
            }
            cursor.close();
        }
        return lista;
    }

    /**
     * Retorna a lista de dias em que o usuário anotou algo no diário (para exibir na Tela Inicial).
     * Os dias mais recentes aparecem primeiro.
     */
    public List<ResumoDiaDiario> listarDiasComAnotacoes() {
        List<PaginaDiario> todasPaginas = listarTodasPaginas();
        Map<String, List<PaginaDiario>> paginasPorDia = new LinkedHashMap<>();

        // Percorre da página mais recente para a mais antiga para listar os dias mais recentes no topo
        for (int i = todasPaginas.size() - 1; i >= 0; i--) {
            PaginaDiario p = todasPaginas.get(i);
            List<PaginaDiario> listaDoDia = paginasPorDia.get(p.getDataDia());
            if (listaDoDia == null) {
                listaDoDia = new ArrayList<>();
                paginasPorDia.put(p.getDataDia(), listaDoDia);
            }
            listaDoDia.add(p);
        }

        List<ResumoDiaDiario> diasAnotados = new ArrayList<>();
        for (Map.Entry<String, List<PaginaDiario>> entry : paginasPorDia.entrySet()) {
            List<PaginaDiario> paginasDoDia = entry.getValue();
            PaginaDiario primeiraMaisRecente = paginasDoDia.get(0);
            // A primeira página criada naquele dia está no final da lista reversa daquele dia
            PaginaDiario primeiraCriadaNoDia = paginasDoDia.get(paginasDoDia.size() - 1);

            // Extrai a data com dia da semana (antes do " às ")
            String dataCompleta = primeiraMaisRecente.getDataCriacaoCompleta();
            String dataComDiaSemana = dataCompleta;
            int idxAs = dataCompleta.indexOf(" às ");
            if (idxAs > 0) {
                dataComDiaSemana = dataCompleta.substring(0, idxAs);
            }

            // Monta um resumo amigável do que foi anotado naquele dia
            String titulo = primeiraMaisRecente.getTitulo();
            String conteudo = primeiraMaisRecente.getConteudo();
            String resumo;
            if (titulo != null && !titulo.trim().isEmpty()) {
                resumo = titulo.trim() + " — " + conteudo.trim();
            } else {
                resumo = conteudo.trim();
            }

            diasAnotados.add(new ResumoDiaDiario(
                    entry.getKey(),
                    dataComDiaSemana,
                    primeiraMaisRecente.getDiaNumero(),
                    primeiraMaisRecente.getMesAbreviado(),
                    paginasDoDia.size(),
                    resumo,
                    primeiraCriadaNoDia.getId()
            ));
        }

        return diasAnotados;
    }
}

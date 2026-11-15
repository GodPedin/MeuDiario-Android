package com.example.meudiario;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * Adapter que exibe cada dia em que o usuário anotou algo no diário na Tela Inicial.
 * Utiliza o layout XML res/layout/item_dia_diario.xml.
 */
public class DiasAdapter extends RecyclerView.Adapter<DiasAdapter.DiaViewHolder> {

    public interface AoClicarNoDiaListener {
        void aoClicarNoDia(ResumoDiaDiario diaSelecionado);
    }

    private final List<ResumoDiaDiario> listaDias;
    private final AoClicarNoDiaListener listener;

    public DiasAdapter(List<ResumoDiaDiario> listaDias, AoClicarNoDiaListener listener) {
        this.listaDias = listaDias;
        this.listener = listener;
    }

    @NonNull
    @Override
    public DiaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_dia_diario, parent, false);
        return new DiaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DiaViewHolder holder, int position) {
        ResumoDiaDiario dia = listaDias.get(position);

        holder.textViewDiaNumero.setText(dia.getDiaNumero());
        holder.textViewMesAbreviado.setText(dia.getMesAbreviado());
        holder.textViewDataCompletaDia.setText(dia.getDataComDiaDaSemana());

        int qtd = dia.getQuantidadePaginas();
        String textoQtd = (qtd == 1) ? "1 página" : qtd + " páginas";
        holder.textViewQuantidadePaginasDia.setText(textoQtd);

        holder.textViewResumoAnotacaoDia.setText(dia.getResumoAnotacoes());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.aoClicarNoDia(dia);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaDias.size();
    }

    static class DiaViewHolder extends RecyclerView.ViewHolder {
        final TextView textViewDiaNumero;
        final TextView textViewMesAbreviado;
        final TextView textViewDataCompletaDia;
        final TextView textViewQuantidadePaginasDia;
        final TextView textViewResumoAnotacaoDia;

        DiaViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewDiaNumero = itemView.findViewById(R.id.textViewDiaNumero);
            textViewMesAbreviado = itemView.findViewById(R.id.textViewMesAbreviado);
            textViewDataCompletaDia = itemView.findViewById(R.id.textViewDataCompletaDia);
            textViewQuantidadePaginasDia = itemView.findViewById(R.id.textViewQuantidadePaginasDia);
            textViewResumoAnotacaoDia = itemView.findViewById(R.id.textViewResumoAnotacaoDia);
        }
    }
}

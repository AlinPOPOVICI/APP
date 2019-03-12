/*package com.example.alin.app1.View_Models;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.alin.app1.DB.Aplicatie;
import com.example.alin.app1.R;

import java.util.Collections;
import java.util.List;

public class AplicatieListAdapter extends RecyclerView.Adapter<AplicatieListAdapter.AplicatieViewHolder> {


    class WordViewHolder extends RecyclerView.ViewHolder {
        private final TextView wordItemView;
        private WordViewHolder(View itemView) {
            super(itemView);

            wordItemView = itemView.findViewById(R.id.words);

        }

    }


    private final LayoutInflater mInflater;
    private List<Aplicatie> mWords = Collections.emptyList(); // Cached copy of words

    AplicatieListAdapter(Context context) {

        mInflater = LayoutInflater.from(context);
    }

    @Override
    public AplicatieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.recyclerview_item, parent, false);

        return new AplicatieViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(AplicatieViewHolder holder, int position) {

        Aplicatie current = mWords.get(position);
        holder.wordItemView.setText(current.getAplicatie());
    }

    void setAplicatie(List<Aplicatie> words) {
        mWords = words;
        notifyDataSetChanged();

    }


    @Override
    public int getItemCount() {
        return mWords.size();

    }

}*/
package com.example.alin.app1.DB;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.alin.app1.R;

import java.util.List;

public class MyDataAdapter  extends RecyclerView.Adapter<MyDataAdapter.MyViewHolder> {
        private List<String> moviesList;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView title;

            public MyViewHolder(View view) {
                super(view);
                title = (TextView) view.findViewById(R.id.title);
            }
        }

        public MyDataAdapter(List<String> moviesList) {
            this.moviesList = moviesList;
        }

        @Override
        public MyDataAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.aplicatie_list_row, parent, false);

            return new MyDataAdapter.MyViewHolder(itemView);
        }


    @Override
        public void onBindViewHolder(MyDataAdapter.MyViewHolder holder, int position) {
            String data = moviesList.get(position);
            holder.title.setText(data);

        }

        @Override
        public int getItemCount() {
            return moviesList.size();
        }
    }

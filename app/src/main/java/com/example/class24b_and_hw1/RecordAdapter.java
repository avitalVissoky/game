package com.example.class24b_and_hw1;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;

public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.RecordViewHolder> {

    private ArrayList<Record> records;
    private OnItemClickListener onItemClickListener;

    public RecordAdapter(ArrayList<Record> records,OnItemClickListener onItemClickListener) {
        this.records=records;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public RecordAdapter.RecordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_item,parent,false);

        return new RecordViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecordAdapter.RecordViewHolder holder, int position) {
        Record record = getItem(position);
        Log.d("RecordAdapter", "Binding record " + position + ": " + record.getName() + ", Score: " + record.getScore());
        holder.txt_place_view.setText(String.valueOf(position+1)+".");
       holder.txt_name_view.setText(record.getName());

        holder.txt_score_view.setText(String.valueOf(record.getScore()));

        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(record);
            }
        });

    }

    @Override
    public int getItemCount() {
        return records==null? 0:records.size();
    }

    public Record getItem(int position){
        return records.get(position);
    }

    public class RecordViewHolder extends RecyclerView.ViewHolder{


        private  final MaterialTextView txt_place_view;
        private final MaterialTextView txt_score_view;
        private final MaterialTextView txt_name_view;

        public RecordViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_place_view = itemView.findViewById(R.id.txt_place_view);
            txt_score_view = itemView.findViewById(R.id.txt_score_view);
            txt_name_view = itemView.findViewById(R.id.txt_name_view);

        }
    }
    public interface OnItemClickListener {
        void onItemClick(Record record);
    }

}

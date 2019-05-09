package com.n8ulm.aquariumkeeper.ui.editList;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.n8ulm.aquariumkeeper.R;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public class ParamListAdapter extends RecyclerView.Adapter<ListItemViewHolder> {

    private List<String> keys;
    private List<String> values;

    public ParamListAdapter(List<String> keys, List<String> values){
       this.keys = keys;
       this.values = values;
    }

    @NonNull
    @Override
    public ListItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ListItemViewHolder(inflater.inflate(R.layout.list_item_parameters, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ListItemViewHolder holder, int position) {
        holder.setDate(keys.get(position));
        holder.setResult(values.get(position));
        holder.getUpdate().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        holder.getDelete().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return keys.size();
    }
}

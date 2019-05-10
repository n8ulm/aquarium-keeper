package com.n8ulm.aquariumkeeper.ui.editList;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.n8ulm.aquariumkeeper.R;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class ParamListAdapter extends RecyclerView.Adapter<ListItemViewHolder> {

    private List<String> keys;
    private List<String> values;
    private String aquarium;
    private String parameter;
    private DatabaseReference mDatabase;

    public ParamListAdapter(List<String> keys, List<String> values, DatabaseReference database){
       this.keys = keys;
       this.values = values;
       this.aquarium = aquarium;
       this.parameter = parameter;
       this.mDatabase = database;
    }

    @NonNull
    @Override
    public ListItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ListItemViewHolder(inflater.inflate(R.layout.list_item_parameters, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ListItemViewHolder holder, final int position) {
        final String key = keys.get(position);
        final String value = values.get(position);

        Date date = new Date(Float.valueOf(keys.get(position)).longValue());

        holder.setDate(new SimpleDateFormat("MMM dd", Locale.US).format(date));
        holder.setResult(value);
        holder.getUpdate().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabase.child(key).setValue(value);
            }
        });

        holder.getDelete().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabase.child(key).removeValue();
            }
        });
    }

    @Override
    public int getItemCount() {
        return keys.size();
    }
}

package com.n8ulm.aquariumkeeper.ui.editList;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.n8ulm.aquariumkeeper.R;

public class ListItemViewHolder extends RecyclerView.ViewHolder {

    private TextView date;
    private EditText result;
    private Button update;
    private Button delete;

    public ListItemViewHolder(@NonNull View itemView) {
        super(itemView);
        this.date = itemView.findViewById(R.id.list_result_date);
        this.result = itemView.findViewById(R.id.list_result_edit);
        this.update = itemView.findViewById(R.id.update_result);
        this.delete = itemView.findViewById(R.id.remove_result);
    }

    public void setDate(String date) {
        this.date.setText(date);
    }

    public void setResult(String result) {
        this.result.setText(result);
    }


    public TextView getDate() {
        return date;
    }

    public EditText getResult() {
        return result;
    }

    public Button getUpdate() {
        return update;
    }

    public Button getDelete() {
        return delete;
    }
}

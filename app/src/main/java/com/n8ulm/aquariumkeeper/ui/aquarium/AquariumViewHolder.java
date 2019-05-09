package com.n8ulm.aquariumkeeper.ui.aquarium;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.n8ulm.aquariumkeeper.R;


class AquariumViewHolder extends RecyclerView.ViewHolder {


    TextView aqTitle;
    TextView aqType;
    TextView aqVolume;
    Button aqLogButton;

    public AquariumViewHolder(@NonNull View itemView) {
        super(itemView);
        aqTitle = itemView.findViewById(R.id.aquarium_title_textview);
        aqType = itemView.findViewById(R.id.type_textview);
        aqVolume = itemView.findViewById(R.id.size_textview);
        aqLogButton = itemView.findViewById(R.id.view_aqurium_log);
    }



    public void setTitle(String title) {
        this.aqTitle.setText(title);
    }

    public void setType(String type) {
        this.aqType.setText(type);
    }

    public void setVolume(String volume) {
        aqVolume.setText(String.valueOf(volume));
    }

    public void setAqLogButton(Button aqLogButton) {
        this.aqLogButton = aqLogButton;
    }
}

package com.n8ulm.aquariumkeeper.ui.aquarium;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.n8ulm.aquariumkeeper.R;


class AquariumViewHolder extends RecyclerView.ViewHolder {

    TextView aqTitle;
    TextView aqType;
    TextView aqVolume;

    public AquariumViewHolder(@NonNull View itemView) {
        super(itemView);
        aqTitle = itemView.findViewById(R.id.aquarium_title_textview);
        aqType = itemView.findViewById(R.id.type_textview);
        aqVolume = itemView.findViewById(R.id.size_textview);
    }



    public void setTitle(String title) {
        this.aqTitle.setText(title);
    }

    public void setType(String type) {
        this.aqType.setText(type);
    }

    public void setVolume(int volume) {
        aqVolume.setText(String.valueOf(volume));
    }

}

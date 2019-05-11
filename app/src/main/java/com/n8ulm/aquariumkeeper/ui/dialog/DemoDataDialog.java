package com.n8ulm.aquariumkeeper.ui.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.database.DatabaseReference;
import com.n8ulm.aquariumkeeper.R;

public class DemoDataDialog extends DialogFragment {

    private DatabaseReference mDatabase;
    private Button addData;
    private Button cancel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_demodate, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        addData = view.findViewById(R.id.add_data_button);
        cancel = view.findViewById(R.id.cancel_button);
    }


}

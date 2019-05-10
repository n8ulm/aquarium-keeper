package com.n8ulm.aquariumkeeper.ui.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.n8ulm.aquariumkeeper.R;
import com.n8ulm.aquariumkeeper.data.Parameter;

public class RemoveAquariumDialog extends DialogFragment {

    private DatabaseReference mDatabase;
    private String mAquarium;
    private Button mCancel;
    private Button mRemove;

    public RemoveAquariumDialog(DatabaseReference databaseReference, String aquarium){
        this.mDatabase = databaseReference;
        this.mAquarium = aquarium;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_removeaquarium, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mCancel = view.findViewById(R.id.cancel_remove_button);
        mRemove = view.findViewById(R.id.remove_button);

        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        mRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabase.child("aquariums").child(mAquarium).removeValue();
                mDatabase.child("parameters").child(mAquarium).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getContext(),
                                 "Aquarium Removed",
                                Toast.LENGTH_LONG).show();
                        dismiss();
                    }
                });
            }
        });
    }
}


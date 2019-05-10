package com.n8ulm.aquariumkeeper.ui.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.n8ulm.aquariumkeeper.R;

import java.util.HashMap;
import java.util.Map;

public class AquariumPropertiesDialog extends DialogFragment
        implements AdapterView.OnItemSelectedListener {

    private DatabaseReference mDatabase;
    private EditText mAquariumTitle;
    private EditText mAquariumVolume;
    private Button mSaveAquariumButton;
    private Button mCancelAquariumButton;
    private String mAquariumId;
    private Spinner mUnits;
    private Spinner mType;

    public AquariumPropertiesDialog(DatabaseReference databaseReference, String aquarium){
        this.mDatabase = databaseReference;
        this.mAquariumId = aquarium;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_aquariumproperties, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAquariumTitle = view.findViewById(R.id.aquarium_title_input);
        mAquariumVolume = view.findViewById(R.id.aquarium_volume_input);
        mSaveAquariumButton = view.findViewById(R.id.save_aquarium_button);
        mCancelAquariumButton = view.findViewById(R.id.cancel_aquarium_button);

        mType = (Spinner) view.findViewById(R.id.type_spinner);

        if (mType != null) {
            mType.setOnItemSelectedListener(this);
        }

        ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.types_array, R.layout.support_simple_spinner_dropdown_item);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        if (mType != null) {
            mType.setAdapter(typeAdapter);
        }

        mUnits = (Spinner) view.findViewById(R.id.unit_spinner);
        if (mUnits != null) {
            mUnits.setOnItemSelectedListener(this);
        }

        ArrayAdapter<CharSequence> unitAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.units_array, R.layout.support_simple_spinner_dropdown_item);
        unitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        if (mUnits != null) {
            mUnits.setAdapter(unitAdapter);
        }

        mDatabase.child("aquariums").child(mAquariumId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mAquariumTitle.setText((String) dataSnapshot.child("title").getValue());
                mAquariumVolume.setText((String) dataSnapshot.child("size").getValue());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mCancelAquariumButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        mSaveAquariumButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> children = new HashMap<>();
                children.put("title", mAquariumTitle.getText().toString());
                children.put("size", mAquariumVolume.getText().toString());
                children.put("type", mType.getSelectedItem().toString());
                children.put("units", mUnits.getSelectedItem().toString());

                mDatabase.child("aquariums").child(mAquariumId).updateChildren(children)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getContext(),
                                "Aquarium Properties Saved",
                                Toast.LENGTH_LONG).show();
                        dismiss();
                    }
                });
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}

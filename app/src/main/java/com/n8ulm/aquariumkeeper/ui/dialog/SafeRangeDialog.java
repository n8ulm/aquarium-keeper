package com.n8ulm.aquariumkeeper.ui.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.n8ulm.aquariumkeeper.R;
import com.n8ulm.aquariumkeeper.data.Parameter;


public class SafeRangeDialog extends DialogFragment {

    private DatabaseReference mDatabase;
    private String mTitle;
    private String mUnits;
    private EditText mMin;
    private EditText mMax;
    private TextView mMinUnits;
    private TextView mMaxUnits;
    private TextView mTitleView;
    private Button mSet;
    private Button mCancel;

    public SafeRangeDialog(DatabaseReference databaseReference, String title) {
        this.mDatabase = databaseReference;
        this.mTitle = title;
        this.mUnits = Parameter.getUnits(title);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_saferange, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mTitleView = view.findViewById(R.id.saferange_title);
        mTitleView.setText("Set " + Parameter.capitalizeString(mTitle) + " Safe Range");

        mMin = view.findViewById(R.id.saferange_min_input);
        mMinUnits = view.findViewById(R.id.min_unit_label);
        mMinUnits.setText(mUnits);

        mMax = view.findViewById(R.id.saferange_max_input);
        mMaxUnits = view.findViewById(R.id.max_unit_label);
        mMaxUnits.setText(mUnits);

        mSet = view.findViewById(R.id.set_range_button);
        mCancel = view.findViewById(R.id.cancel_range_button);

        mSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String min = mMin.getText().toString();
                String max = mMax.getText().toString();

                mDatabase.child(mTitle).child("saferange")
                        .setValue(min + " - " + max + " " +
                                Parameter.getUnits(mTitle)).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getContext(), "Safe Range Set", Toast.LENGTH_LONG).show();
                        dismiss();
                    }
                });

            }
        });

        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}

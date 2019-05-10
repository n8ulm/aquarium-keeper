package com.n8ulm.aquariumkeeper.ui.dialog;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.n8ulm.aquariumkeeper.R;
import com.n8ulm.aquariumkeeper.data.Parameter;
import com.n8ulm.aquariumkeeper.ui.DatePickerFragment;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AddResultDialog extends DialogFragment
        implements DatePickerFragment.OnDateSelectedListener{

    private DatabaseReference mDatabase;
    private String mTitle;
    private String mUnits;
    private String mMSDate;
    private TextView mTitleView;
    private EditText mResult;
    private EditText mDate;
    private TextView mUnitLabel;
    private ImageButton mDateButton;
    private Button mSaveResultButton;




    public AddResultDialog(DatabaseReference databaseReference, String title){
        this.mDatabase = databaseReference;
        this.mTitle = Parameter.capitalizeString(title);
        this.mUnits = Parameter.getUnits(title);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_addresult, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mTitleView = view.findViewById(R.id.title_textView);
        mTitleView.setText("New " + mTitle + " Result");

        mResult = view.findViewById(R.id.result_input);
        mDate = view.findViewById(R.id.result_date_input);

        mUnitLabel = view.findViewById(R.id.unit_label);
        mUnitLabel.setText(mUnits);

        mDateButton = view.findViewById(R.id.date_picker_button);
        mSaveResultButton = view.findViewById(R.id.save_result_button);

        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.setTargetFragment(AddResultDialog.this, 300);
                newFragment.show(fm, "datePicker");
            }
        });

        mSaveResultButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mResult.getText().equals("")) {

                    writeNewResult(mMSDate, new Double(String.valueOf(mResult.getText())));

                } else {
                    Toast.makeText(getActivity(),
                            "Please Enter a Test Result", Toast.LENGTH_LONG).show();
                }
            }
        });


    }

    public void writeNewResult(String date, Double result) {
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put(date, result);

        mDatabase.child(mTitle).child("results").updateChildren(childUpdates)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getActivity(),
                        "Result Added", Toast.LENGTH_LONG).show();
                dismiss();
            }
        });
    }


    public void updateDate(int year, int month, int day) {
        String year_str = Integer.toString(year);
        String month_str = Integer.toString(month + 1);
        String day_str = Integer.toString(day);

        Calendar date = Calendar.getInstance();
        date.set(year, month, day);


        mMSDate = Long.toString(date.getTimeInMillis());
        Log.d("toMIL", mMSDate);

        mDate.setText(month_str + "/" + day_str + "/" + year_str);
    }

    @Override
    public void onDateSelected(int year, int month, int day) {
        updateDate(year, month, day);
    }
}

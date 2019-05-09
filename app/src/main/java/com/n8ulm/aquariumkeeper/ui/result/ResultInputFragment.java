package com.n8ulm.aquariumkeeper.ui.result;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.n8ulm.aquariumkeeper.R;
import com.n8ulm.aquariumkeeper.data.Parameter;
import com.n8ulm.aquariumkeeper.ui.DatePickerFragment;
import com.n8ulm.aquariumkeeper.ui.dashboard.DashboardFragmentDirections;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ResultInputFragment extends Fragment
		implements AdapterView.OnItemSelectedListener,
					DatePickerFragment.OnDateSelectedListener {

	private static final String AQUARIUM_ARG = "mCurrentAquarium";

	private String mCurrentAquarium = "aquarium1";


	// Memeber Variable
	private OnFragmentInteractionListener mListener;
	private EditText mTestResult;
	private EditText mTestDate;
	private TextView mUnitLabel;
	private ImageButton mDateButton;
	private String mMSDate;

	private Spinner paramSpinner;
	private Spinner aquariumSpinner;

	private Button mSaveResultButton;
	private Button mViewLogButton;

	private Map<String, String> mAquariumTitles;

	private DatabaseReference mDatabase;
	private DatabaseReference mAquariumRef;
	private FirebaseUser mUser;


	public ResultInputFragment() {
		// Required empty public constructor
	}



	public static ResultInputFragment newInstance(String currentAquarium) {
		ResultInputFragment fragment = new ResultInputFragment();
		Bundle args = new Bundle();
		args.putString(AQUARIUM_ARG, currentAquarium);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			mCurrentAquarium = savedInstanceState.getString(AQUARIUM_ARG);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.fragment_result_input, container, false);

		mUser = FirebaseAuth.getInstance().getCurrentUser();
		mDatabase = FirebaseDatabase.getInstance().getReference();
		mAquariumRef = mDatabase.child("users").child(mUser.getUid()).child("aquariums");

		paramSpinner = (Spinner) view.findViewById(R.id.parameter_spinner);
		if (paramSpinner != null){
			paramSpinner.setOnItemSelectedListener(this);
		}

		ArrayAdapter<CharSequence> paramAdapter = ArrayAdapter.createFromResource(getActivity(),
				R.array.parameters_array, R.layout.support_simple_spinner_dropdown_item);
		paramAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		if (paramSpinner != null) {
			paramSpinner.setAdapter(paramAdapter);
		}

		aquariumSpinner = (Spinner) view.findViewById(R.id.aquarium_spinner);

		if (aquariumSpinner != null) {
			aquariumSpinner.setOnItemSelectedListener(this);
		}

		final List<String> aquariums = new ArrayList<>();

		mAquariumTitles = new HashMap<>();

		mAquariumRef.addValueEventListener(new ValueEventListener() {
			@Override
			public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
				for (DataSnapshot aquariumsSnapshot : dataSnapshot.getChildren()) {
					String aquariumName = (String) aquariumsSnapshot.child("title").getValue();
					aquariums.add(aquariumName);
					mAquariumTitles.put(aquariumName, (String) aquariumsSnapshot.getKey());
				}

				ArrayAdapter<String> aquariumsAdapter =
						new ArrayAdapter<String>(getActivity(),
								android.R.layout.simple_spinner_dropdown_item,
								aquariums);
				aquariumsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				aquariumSpinner.setAdapter(aquariumsAdapter);
			}

			@Override
			public void onCancelled(@NonNull DatabaseError databaseError) {

			}
		});


		mTestDate = view.findViewById(R.id.result_date_input);

		mTestResult = view.findViewById(R.id.result_input);

		mUnitLabel = view.findViewById(R.id.unit_label);

		final Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);
		int day = cal.get(Calendar.DAY_OF_MONTH);

		updateDate(year, month, day);

		mSaveResultButton = (Button) view.findViewById(R.id.save_result_button);
		mSaveResultButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				if (!mTestResult.getText().equals("")) {
					String title = paramSpinner.getSelectedItem().toString().toLowerCase();

					writeNewResult(mUser.getUid(), title, mMSDate, new Double(String.valueOf(mTestResult.getText())));
					Toast.makeText(getActivity(),
							"Result Added", Toast.LENGTH_LONG).show();

				} else {
					 Toast.makeText(getActivity(),
							"Please Enter a Test Result", Toast.LENGTH_LONG).show();
				}

			}
		});

		mViewLogButton = (Button) view.findViewById(R.id.view_log_button);
		mViewLogButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				DashboardFragmentDirections.ActionDashboardFragmentToLogFragment action =
                        DashboardFragmentDirections.actionDashboardFragmentToLogFragment();
				action.setIdArg(mCurrentAquarium);
				Navigation.findNavController(getActivity(), R.id.my_nav_host_fragment).navigate(action);
			}
		});

		mDateButton = (ImageButton) view.findViewById(R.id.date_picker_button);
		mDateButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showDatePicker(view);
			}
		});

		return view;
	}

	public void showDatePicker(View view) {
		FragmentManager fm = getFragmentManager();
		DialogFragment newFragment = new DatePickerFragment();
		newFragment.setTargetFragment(ResultInputFragment.this, 300);
		newFragment.show(fm, "datePicker");
	}

	// TODO: Rename method, update argument and hook method into UI event
	public void onButtonPressed(Uri uri) {
		if (mListener != null) {
			mListener.onFragmentInteraction(uri);
		}
	}

	public void updateDate(int year, int month, int day) {
		String year_str = Integer.toString(year);
		String month_str = Integer.toString(month + 1);
		String day_str = Integer.toString(day);

		Calendar date = Calendar.getInstance();
		date.set(year, month, day);


		mMSDate = Long.toString(date.getTimeInMillis());
		Log.d("toMIL", mMSDate);

		mTestDate.setText(month_str + "/" + day_str + "/" + year_str);
	}

	public void writeNewResult(String uid, String title, String date, Double result) {
		Map<String, Object> childUpdates = new HashMap<>();
		childUpdates.put(date, result);


		mDatabase.child("users").child(uid).child("parameters").child(mCurrentAquarium).child(title).child("results").updateChildren(childUpdates);

	}

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		if (context instanceof OnFragmentInteractionListener) {
			mListener = (OnFragmentInteractionListener) context;
		} else {
			throw new RuntimeException(context.toString()
					+ " must implement OnFragmentInteractionListener");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mListener = null;
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		if (parent.getId() == aquariumSpinner.getId()) {
			mCurrentAquarium = mAquariumTitles.get(parent.getItemAtPosition(position));
		} else if (parent.getId() == paramSpinner.getId()) {
			mUnitLabel.setText(Parameter.getUnits((String) parent.getItemAtPosition(position)));
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {

	}

	@Override
	public void onDateSelected(int year, int month, int day) {
		updateDate(year, month, day);
	}


	/**
	 * This interface must be implemented by activities that contain this
	 * fragment to allow an interaction in this fragment to be communicated
	 * to the activity and potentially other fragments contained in that
	 * activity.
	 * <p>
	 * See the Android Training lesson <a href=
	 * "http://developer.android.com/training/basics/fragments/communicating.html"
	 * >Communicating with Other Fragments</a> for more information.
	 */
	public interface OnFragmentInteractionListener {
		// TODO: Update argument type and name
		void onFragmentInteraction(Uri uri);
	}

}

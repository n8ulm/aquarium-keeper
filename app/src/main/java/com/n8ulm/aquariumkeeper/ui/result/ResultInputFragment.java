package com.n8ulm.aquariumkeeper.ui.result;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.n8ulm.aquariumkeeper.R;
import com.n8ulm.aquariumkeeper.data.Parameter;
import com.n8ulm.aquariumkeeper.ui.DatePickerFragment;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class ResultInputFragment extends Fragment
		implements AdapterView.OnItemSelectedListener,
					DatePickerFragment.OnDateSelectedListener {

	private static final String AQUARIUM_ARG = "currentAquarium";

	private String currentAquarium = "aquarium1";


	// Memeber Variable
	private OnFragmentInteractionListener mListener;
	private EditText mTestResult;
	private EditText mTestDate;
	private TextView mUnitLabel;
	private ImageButton mDateButton;
	private String mMSDate;

	private Button mSaveResultButton;
	private Button mViewLogButton;

	private DatabaseReference mDatabaase;
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
			currentAquarium = savedInstanceState.getString(AQUARIUM_ARG);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.fragment_result_input, container, false);

		final Spinner spinner = (Spinner) view.findViewById(R.id.parameter_spinner);
		if (spinner != null){
			spinner.setOnItemSelectedListener(this);
		}

		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
				R.array.parameters_array, R.layout.support_simple_spinner_dropdown_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		if (spinner != null) {
			spinner.setAdapter(adapter);
		}

		mTestDate = view.findViewById(R.id.result_date_input);

		mTestResult = view.findViewById(R.id.result_input);

		mUnitLabel = view.findViewById(R.id.unit_label);

		final Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);
		int day = cal.get(Calendar.DAY_OF_MONTH);

		updateDate(year, month, day);

		mDatabaase = FirebaseDatabase.getInstance().getReference();
		mUser = FirebaseAuth.getInstance().getCurrentUser();

		mSaveResultButton = (Button) view.findViewById(R.id.save_result_button);
		mSaveResultButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				if (!mTestResult.getText().equals("")) {
					String title = spinner.getSelectedItem().toString().toLowerCase();

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

				NavController navController =
						Navigation.findNavController(getActivity(), R.id.my_nav_host_fragment);
				navController.navigate(R.id.action_dashboardFragment_to_logFragment);
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


		mDatabaase.child("users").child(uid).child("parameters").child(currentAquarium).child(title).child("results").updateChildren(childUpdates);

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

		mUnitLabel.setText(Parameter.getUnits((String) parent.getItemAtPosition(position)));

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

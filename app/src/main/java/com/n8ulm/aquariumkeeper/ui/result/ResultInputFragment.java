package com.n8ulm.aquariumkeeper.ui.result;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.provider.ContactsContract;
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

import com.anychart.scales.DateTime;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.n8ulm.aquariumkeeper.R;
import com.n8ulm.aquariumkeeper.ui.DatePickerFragment;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;


public class ResultInputFragment extends Fragment
		implements AdapterView.OnItemSelectedListener,
					DatePickerFragment.OnDateSelectedListener{


	private static final String ARG_YEAR = "year";
	private static final String ARG_MONTH = "month";
	private static final String ARG_DAY = "day";



	// Memeber Variable
	private OnFragmentInteractionListener mListener;
	private EditText mTestResult;
	private EditText mTestDate;
	private ImageButton mDateButton;
	private String mMSDate;

	private Button mSaveResultButton;
	private Button mCancelResultButton;

	private DatabaseReference mDatabaase;
	private FirebaseUser mUser;

	public ResultInputFragment() {
		// Required empty public constructor
	}



	public static ResultInputFragment newInstance(int year, int month, int day) {
		ResultInputFragment fragment = new ResultInputFragment();
		Bundle args = new Bundle();
		args.putInt(ARG_YEAR, year);
		args.putInt(ARG_MONTH, month);
		args.putInt(ARG_DAY, day);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {

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
				R.array.labels_array, R.layout.support_simple_spinner_dropdown_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		if (spinner != null) {
			spinner.setAdapter(adapter);
		}

		mTestDate = view.findViewById(R.id.result_date_input);

		mTestResult = view.findViewById(R.id.result_input);

		final Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);
		int day = cal.get(Calendar.DAY_OF_MONTH);

		updateDate(year, month, day);

		if (savedInstanceState != null){
			updateDate(savedInstanceState.getInt(ARG_YEAR),
					savedInstanceState.getInt(ARG_MONTH),
					savedInstanceState.getInt(ARG_DAY));
		}

		mDatabaase = FirebaseDatabase.getInstance().getReference();
		mUser = FirebaseAuth.getInstance().getCurrentUser();

		mSaveResultButton = (Button) view.findViewById(R.id.save_result_button);
		mSaveResultButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String title = spinner.getSelectedItem().toString().toLowerCase();

				writeNewResult(mUser.getUid(), title, mMSDate, new Double(String.valueOf(mTestResult.getText())));

				NavController navController =
						Navigation.findNavController(getActivity(), R.id.my_nav_host_fragment);
				navController.navigate(R.id.action_resultInputFragment_to_logFragment);



			}
		});

		mCancelResultButton = (Button) view.findViewById(R.id.cancel_result_button);
		mCancelResultButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				NavController navController =
						Navigation.findNavController(getActivity(), R.id.my_nav_host_fragment);
				navController.navigate(R.id.action_resultInputFragment_to_logFragment);
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

		mDatabaase.child("users").child(uid).child("parameters").child(title).updateChildren(childUpdates);

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

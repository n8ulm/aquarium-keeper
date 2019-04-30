package com.n8ulm.aquariumkeeper.ui.result;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.n8ulm.aquariumkeeper.R;

import java.util.HashMap;
import java.util.Map;


public class ResultInputFragment extends Fragment implements AdapterView.OnItemSelectedListener {

	// Memeber Variable
	private OnFragmentInteractionListener mListener;
	private EditText mTestResult;
	private EditText mTestDate;

	private Button mSaveResultButton;
	private Button mCancelResultButton;

	private DatabaseReference mDatabaase;
	private FirebaseUser mUser;

	public ResultInputFragment() {
		// Required empty public constructor
	}



	public static ResultInputFragment newInstance(String param1, String param2) {
		ResultInputFragment fragment = new ResultInputFragment();
		Bundle args = new Bundle();
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

		mDatabaase = FirebaseDatabase.getInstance().getReference();
		mUser = FirebaseAuth.getInstance().getCurrentUser();

		mSaveResultButton = (Button) view.findViewById(R.id.save_result_button);
		mSaveResultButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String title = spinner.getSelectedItem().toString().toLowerCase();

				writeNewResult(mUser.getUid(), title, mTestDate.getText().toString(), new Double(String.valueOf(mTestResult.getText())));

				NavController navController =
						Navigation.findNavController(getActivity(), R.id.my_nav_host_fragment);
				navController.navigate(R.id.action_resultInputFragment_to_logFragment);



			}
		});

		return view;
	}

	// TODO: Rename method, update argument and hook method into UI event
	public void onButtonPressed(Uri uri) {
		if (mListener != null) {
			mListener.onFragmentInteraction(uri);
		}
	}

	public void writeNewResult(String uid, String title, String date, double result) {
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

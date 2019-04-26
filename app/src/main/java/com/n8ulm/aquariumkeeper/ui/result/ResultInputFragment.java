package com.n8ulm.aquariumkeeper.ui.result;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.n8ulm.aquariumkeeper.R;


public class ResultInputFragment extends Fragment implements AdapterView.OnItemSelectedListener {
	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private static final String ARG_PARAM1 = "param1";
	private static final String ARG_PARAM2 = "param2";

	// TODO: Rename and change types of parameters
	private String mParam1;
	private String mParam2;

	private OnFragmentInteractionListener mListener;

	// Instance Variable
	EditText mTestResult;
	EditText mTestDate;

	Button mSaveResultButton;
	Button mCancelResultButton;

	public ResultInputFragment() {
		// Required empty public constructor
	}



	public static ResultInputFragment newInstance(String param1, String param2) {
		ResultInputFragment fragment = new ResultInputFragment();
		Bundle args = new Bundle();
		args.putString(ARG_PARAM1, param1);
		args.putString(ARG_PARAM2, param2);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			mParam1 = getArguments().getString(ARG_PARAM1);
			mParam2 = getArguments().getString(ARG_PARAM2);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.dialogue_test_input, container, false);

		final Spinner spinner = (Spinner) view.findViewById(R.id.test_spinner);
		if (spinner != null){
			spinner.setOnItemSelectedListener(this);
		}

		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
				R.array.labels_array, R.layout.support_simple_spinner_dropdown_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		if (spinner != null) {
			spinner.setAdapter(adapter);
		}

		mSaveResultButton = (Button) view.findViewById(R.id.save_result_button);
		mSaveResultButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String parameterTitel = spinner.getSelectedItem().toString();
				DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("users").push();

			}
		});

		ResultInputViewModel viewModel = ViewModelProviders.of(getActivity()).get(ResultInputViewModel.class);

		LiveData<DataSnapshot> liveData = viewModel.getDataSnapshotLiveData();

		liveData.observe(getActivity(), new Observer<DataSnapshot>() {
			@Override
			public void onChanged(DataSnapshot dataSnapshot) {
				if (dataSnapshot != null) {

				}
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

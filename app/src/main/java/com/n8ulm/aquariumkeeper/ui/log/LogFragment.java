package com.n8ulm.aquariumkeeper.ui.log;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.solver.widgets.Snapshot;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.n8ulm.aquariumkeeper.R;
import com.n8ulm.aquariumkeeper.data.Parameter;

import java.util.ArrayList;
import java.util.List;


public class LogFragment extends Fragment {

	// Member variables.
	private RecyclerView mRecyclerView;
	private DatabaseReference mFirebaseDatabaseReference;
	private FirebaseUser mFirebaseUser;
	private FirebaseRecyclerAdapter<Parameter, ParameterViewHolder> mFirebaseAdapter;


	private OnFragmentInteractionListener mListener;
	private final String TAG = LogFragment.class.getSimpleName();

	private String mCurrentAquarium = "aquarium1";

	public LogFragment() {
		// Required empty public constructor
	}

	public static LogFragment newInstance(String param1, String param2) {
		LogFragment fragment = new LogFragment();
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

		final View view = inflater.inflate(R.layout.fragment_log, container, false);


		return view;
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		mRecyclerView = (RecyclerView) view.findViewById(R.id.parameter_log_list);

		mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();

		mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();

		mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
		fetch();

		// Inflate the layout for this fragment
	}



	private void fetch() {
		Query query = mFirebaseDatabaseReference
				.child("users").child(mFirebaseUser.getUid())
				.child("parameters")
				.child(mCurrentAquarium);

		FirebaseRecyclerOptions<Parameter> options =
				new FirebaseRecyclerOptions.Builder<Parameter>()
						.setQuery(query, new SnapshotParser<Parameter>() {
							@NonNull
							@Override
							public Parameter parseSnapshot(@NonNull DataSnapshot snapshot) {
								String title = snapshot.getKey();
								Log.d(TAG, title);

								String safeRange = "Safe Range: " + (String) snapshot.child("saferange").getValue();

								List<Entry> results = new ArrayList<>();

								for (DataSnapshot child : snapshot.child("results").getChildren()) {
									float x = toFloat(child.getKey());
									float y = toFloat(child.getValue());

									results.add(new Entry(x, y));
								}

								float lastDate = results.get(results.size()-1).getX();
								float lastResult = results.get(results.size()-1).getY();

								return new Parameter(title, results, lastDate, lastResult, safeRange);
							}
						})
						.build();

		mFirebaseAdapter = new FirebaseRecyclerAdapter<Parameter, ParameterViewHolder>(options) {
			@NonNull
			@Override
			public ParameterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
				LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
				return new ParameterViewHolder(inflater.inflate(R.layout.list_item_parameter_log,
						viewGroup, false));
			}

			@Override
			protected void onBindViewHolder(@NonNull ParameterViewHolder viewHolder, int i, @NonNull Parameter parameter) {
				if (parameter.getParamTitle() != null) {
					String title = capitalizeString(parameter.getParamTitle());
					viewHolder.setTitle(title);
					viewHolder.setLastResult(parameter.getParamDate(), parameter.getParamResult());
					viewHolder.setSafeRange(parameter.getParamSafeRange());


					LineDataSet dataSet = new LineDataSet(parameter.getResults(), "Label");
					LineData lineData = new LineData(dataSet);
					viewHolder.parameterChart.setData(lineData);
					viewHolder.parameterChart.invalidate();
				}
			}
		};

		mFirebaseAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
			@Override
			public void onItemRangeInserted(int positionStart, int itemCount) {
				super.onItemRangeInserted(positionStart, itemCount);
				int parameterCount = mFirebaseAdapter.getItemCount();

			}
		});

		mRecyclerView.setAdapter(mFirebaseAdapter);
	}

	private String capitalizeString(String string) {
		StringBuilder result = new StringBuilder(string);
		result.replace(0,1, string.substring(0,1).toUpperCase());
		result.replace(1, string.length(), string.substring(1, string.length()).toLowerCase());

		return result.toString();
	}

	private float toFloat(Object object) {
		String input = object.toString();

		Log.d("FLOAT", String.valueOf(Float.parseFloat(input)));

		return Float.parseFloat(input);

	}


	@Override
	public void postponeEnterTransition() {
		super.postponeEnterTransition();
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

	public interface OnFragmentInteractionListener {
		// TODO: Update argument type and name
		void onFragmentInteraction(Uri uri);
	}

	@Override
	public void onStart() {
		super.onStart();
		mFirebaseAdapter.startListening();
	}

	@Override
	public void onStop() {
		super.onStop();
		mFirebaseAdapter.stopListening();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		mFirebaseAdapter.startListening();
	}

	@Override
	public void onAttachFragment(@NonNull Fragment childFragment) {
		super.onAttachFragment(childFragment);
	}

}

package com.n8ulm.aquariumkeeper.ui.log;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Line;
import com.anychart.data.Mapping;
import com.anychart.data.Set;
import com.anychart.enums.Anchor;
import com.anychart.enums.MarkerType;
import com.anychart.enums.TooltipPositionMode;
import com.anychart.graphics.vector.Stroke;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.n8ulm.aquariumkeeper.R;
import com.n8ulm.aquariumkeeper.data.Parameter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class LogFragment extends Fragment {

	// Member variables.
	private RecyclerView mRecyclerView;
	private DatabaseReference mFirebaseDatabaseReference;
	private FirebaseUser mFirebaseUser;
	private FirebaseRecyclerAdapter<Parameter, ParameterViewHolder> mFirebaseAdapter;


	private OnFragmentInteractionListener mListener;
	private final String TAG = LogFragment.class.getSimpleName();

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
		Query query = FirebaseDatabase.getInstance().getReference()
				.child("users").child(mFirebaseUser.getUid())
				.child("parameters");

		FirebaseRecyclerOptions<Parameter> options =
				new FirebaseRecyclerOptions.Builder<Parameter>()
						.setQuery(query, new SnapshotParser<Parameter>() {
							@NonNull
							@Override
							public Parameter parseSnapshot(@NonNull DataSnapshot snapshot) {
								String title = snapshot.getKey();
								Log.d(TAG, title);
								List<DataEntry> results = new ArrayList<>();


								for (DataSnapshot child : snapshot.getChildren()) {
									results.add(new ValueDataEntry(child.getKey(), new Double(String.valueOf(child.getValue()))));
									Log.d(TAG, child.getKey().toString() + " : "+ child.getValue().toString());
								}

								return new Parameter(title, results);
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

					viewHolder.paramTitle.setText(parameter.getParamTitle());

					Cartesian cartesian = AnyChart.line();

					cartesian.animation(true);

					cartesian.padding(10d, 20d, 5d, 20d);

					cartesian.crosshair()
							.yLabel(true)
							.yStroke((Stroke) null, null, null, (String) null, (String) null);

					cartesian.tooltip().positionMode(TooltipPositionMode.POINT);

					cartesian.yAxis(0).title("ppm/(mg/L)");
					cartesian.xAxis(0).labels().padding(5d, 5d, 5d, 5d);

					Set set = Set.instantiate();
					set.data(parameter.getResults());
					Mapping seriesMapping = set.mapAs("{x: 'x', value: 'value'}");

					Line series = cartesian.line(seriesMapping);
					series.hovered().markers().enabled(true);
					series.hovered().markers()
							.type(MarkerType.CIRCLE)
							.size(4d);
					series.tooltip()
							.position("right")
							.anchor(Anchor.LEFT_CENTER)
							.offsetX(5d)
							.offsetY(5d);

					cartesian.legend().enabled(true);
					cartesian.legend().fontSize(13d);
					cartesian.legend().padding(0d, 0d, 10d, 0d);

					viewHolder.parameterChart.setChart(cartesian);
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
}
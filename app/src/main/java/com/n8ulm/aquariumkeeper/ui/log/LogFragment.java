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

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.n8ulm.aquariumkeeper.R;
import com.n8ulm.aquariumkeeper.data.Parameter;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LogFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LogFragment extends Fragment {

	private final String PARAMS_CHILD = "parameters";

	// Member variables.
	private RecyclerView mRecyclerView;
	private DatabaseReference mFirebaseDatabaseReference;
	private FirebaseUser mFirebaseUser;
	private FirebaseRecyclerAdapter<Parameter, ParameterViewHolder> mFirebaseAdapter;

	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private static final String ARG_PARAM1 = "param1";
	private static final String ARG_PARAM2 = "param2";

	// TODO: Rename and change types of parameters
	private String mParam1;
	private String mParam2;

	private OnFragmentInteractionListener mListener;
	private final String TAG = LogFragment.class.getSimpleName();

	public LogFragment() {
		// Required empty public constructor
	}

	public static LogFragment newInstance(String param1, String param2) {
		LogFragment fragment = new LogFragment();
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

		final View view = inflater.inflate(R.layout.fragment_log, container, false);


		return view;
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		mRecyclerView = (RecyclerView) view.findViewById(R.id.parameter_log_list);

		mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();

		mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
		SnapshotParser<Parameter> parser = new SnapshotParser<Parameter>() {
			@NonNull
			@Override
			public Parameter parseSnapshot(@NonNull DataSnapshot snapshot) {
				Parameter parameter = snapshot.getValue(Parameter.class);
				if (parameter != null) {
					parameter.setId(snapshot.getKey());
				}
				return parameter;
			}
		};

		DatabaseReference parametersRef = mFirebaseDatabaseReference
				.child("users")
				.child(mFirebaseUser.getUid())
				.child("parameters");

		FirebaseRecyclerOptions<Parameter> options =
				new FirebaseRecyclerOptions.Builder<Parameter>()
						.setQuery(parametersRef, parser)
						.build();

//		LogFragmentViewModel viewModel = ViewModelProviders.of(getActivity()).get(LogFragmentViewModel.class);
//
//		LiveData<DataSnapshot> liveData = viewModel.getDataSnapshotLiveData();
//
//		liveData.observe(getActivity(), new Observer<DataSnapshot>() {
//			@Override
//			public void onChanged(DataSnapshot dataSnapshot) {
//				if (dataSnapshot != null);
//			}
//		});

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
					Log.d(TAG, "In onBindViewHolder, Parameter title is not null");
					viewHolder.paramTitle.setText(parameter.getParamTitle());
				}
				Log.d(TAG, "In onBindViewHolder, Parameter title is null");
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
		mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

		// Inflate the layout for this fragment
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

	private void fetch() {

	}
}

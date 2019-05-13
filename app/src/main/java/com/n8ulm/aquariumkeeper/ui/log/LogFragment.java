package com.n8ulm.aquariumkeeper.ui.log;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.n8ulm.aquariumkeeper.R;
import com.n8ulm.aquariumkeeper.data.Parameter;
import com.n8ulm.aquariumkeeper.ui.dialog.AddResultDialog;
import com.n8ulm.aquariumkeeper.ui.dialog.RemoveChartDialog;
import com.n8ulm.aquariumkeeper.ui.dialog.SafeRangeDialog;

import java.util.ArrayList;
import java.util.List;


public class LogFragment extends Fragment {

	public static final String ID_ARG = "currentAquarium";

	// Member variables.
	private RecyclerView mRecyclerView;
	private DatabaseReference mDatabase;
	private FirebaseUser mFirebaseUser;
	private FirebaseRecyclerAdapter<Parameter, ParameterViewHolder> mFirebaseAdapter;
	private OnFragmentInteractionListener mListener;
	private final String TAG = LogFragment.class.getSimpleName();

	private String mCurrentAquarium = "aquarium1";

	public LogFragment() {
		// Required empty public constructor
	}

	public static LogFragment newInstance(String currentAquarium) {
		LogFragment fragment = new LogFragment();
		Bundle args = new Bundle();
		args.putString(ID_ARG, currentAquarium);
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

		mCurrentAquarium = LogFragmentArgs.fromBundle(getArguments()).getIdArg();

		mRecyclerView = (RecyclerView) view.findViewById(R.id.parameter_log_list);

		mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();

		mDatabase = FirebaseDatabase.getInstance().getReference()
				.child("users").child(mFirebaseUser.getUid())
				.child("parameters")
				.child(mCurrentAquarium);;

		mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
		fetch();

		// Inflate the layout for this fragment
	}



	private void fetch() {
		Query query = mDatabase;

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
			private Context mContext;
			@NonNull
			@Override
			public ParameterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
				mContext = viewGroup.getContext();
				LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
				return new ParameterViewHolder(inflater.inflate(R.layout.list_item_parameter_log,
						viewGroup, false));
			}

			@Override
			protected void onBindViewHolder(@NonNull ParameterViewHolder viewHolder, int i, @NonNull final Parameter parameter) {
				if (parameter.getParamTitle() != null) {
					String title = Parameter.capitalizeString(parameter.getParamTitle());
					viewHolder.setTitle(title);
					viewHolder.setLastResult(parameter.getParamDate(), parameter.getParamResult());
					if (parameter.getParamSafeRange() != null) {
						viewHolder.setSafeRange(parameter.getParamSafeRange());
					} else {
						viewHolder.paramSafeRange.setVisibility(View.INVISIBLE);
					}


					LineDataSet dataSet = new LineDataSet(parameter.getResults(), Parameter.capitalizeString(parameter.getParamTitle()));
					LineData lineData = new LineData(dataSet);
					viewHolder.parameterChart.setData(lineData);
					viewHolder.parameterChart.setDescription(null);
					viewHolder.parameterChart.invalidate();

					viewHolder.editResults.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							editResults(mCurrentAquarium, parameter.getParamTitle());
						}
					});

					viewHolder.addResult.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							addResult(mDatabase, parameter.getParamTitle());
						}
					});

					final PopupMenu popupMenu = new PopupMenu(mContext, viewHolder.overflowMenu);
					popupMenu.inflate(R.menu.popup_menu);
					popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
						@Override
						public boolean onMenuItemClick(MenuItem item) {
							switch (item.getItemId()) {
								case R.id.menu_set_saferange:
									setSafeRange(mDatabase, parameter.getParamTitle());
									return true;
								case R.id.menu_edit_results:
									editResults(mCurrentAquarium, parameter.getParamTitle());
									return true;
								case R.id.menu_remove_chart:
									removeChart(mDatabase, parameter.getParamTitle());
									return true;
								default:
									return true;
							}
						}
					});
					viewHolder.overflowMenu.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							popupMenu.show();
						}
					});
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

	private void removeChart(DatabaseReference databaseReference, String title) {
		FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
		Fragment prev = getFragmentManager().findFragmentByTag("dialog");
		if (prev != null){
			fragmentTransaction.remove(prev);
		}
		fragmentTransaction.addToBackStack(null);
		DialogFragment dialogFragment = new RemoveChartDialog(databaseReference, title);
		dialogFragment.show(fragmentTransaction, "dialog");
	}

	private void addResult(DatabaseReference databaseReference, String paramTitle) {
		FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
		Fragment prev = getFragmentManager().findFragmentByTag("dialog");
		if (prev != null) {
			fragmentTransaction.remove(prev);
		}
		fragmentTransaction.addToBackStack(null);

		DialogFragment dialogFragment = new AddResultDialog(databaseReference, paramTitle);
		dialogFragment.show(fragmentTransaction, "dialog");
	}

	private void setSafeRange(DatabaseReference databaseReference, String title) {
		FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
		Fragment prev = getFragmentManager().findFragmentByTag("dialog");
		if (prev != null) {
			fragmentTransaction.remove(prev);
		}
		fragmentTransaction.addToBackStack(null);

		DialogFragment dialogFragment = new SafeRangeDialog(databaseReference, title);
		dialogFragment.show(fragmentTransaction, "dialog");
	}

	private void editResults(String aquarium, String parameter) {
		LogFragmentDirections.ActionLogFragmentToEditListFragment action =
				LogFragmentDirections.actionLogFragmentToEditListFragment();
		action.setIdArg(aquarium);
		action.setParamArg(parameter);

		Navigation.findNavController(getActivity(), R.id.my_nav_host_fragment)
				.navigate(action);
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

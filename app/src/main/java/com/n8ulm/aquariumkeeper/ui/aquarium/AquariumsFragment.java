package com.n8ulm.aquariumkeeper.ui.aquarium;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import com.n8ulm.aquariumkeeper.data.Aquarium;


public class AquariumsFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = AquariumsFragment.class.getSimpleName();

    private String mParam1;
    private String mParam2;

    // Member variables.
    private RecyclerView mRecyclerView;
    private DatabaseReference mFirebaseDatabaseReference;
    private FirebaseUser mFirebaseUser;
    private FirebaseRecyclerAdapter<Aquarium, AquariumViewHolder> mFirebaseAdapter;
    private OnFragmentInteractionListener mListener;

    public AquariumsFragment() {
        // Required empty public constructor
    }

    public static AquariumsFragment newInstance(String param1, String param2) {
        AquariumsFragment fragment = new AquariumsFragment();
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_aquariums, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.aquarium_list);
        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        fetch();
    }

    private void fetch() {
        Query query = mFirebaseDatabaseReference
                .child("users").child(mFirebaseUser.getUid())
                .child("aquariums");

        FirebaseRecyclerOptions<Aquarium> options =
                new FirebaseRecyclerOptions.Builder<Aquarium>()
                    .setQuery(query, new SnapshotParser<Aquarium>() {
                        @NonNull
                        @Override
                        public Aquarium parseSnapshot(@NonNull DataSnapshot snapshot) {
                            String aquariumID = snapshot.getKey();
                            String title = (String) snapshot.child(aquariumID).child("title").getValue();
                            String type = (String) snapshot.child(aquariumID).child("title").getValue();
                            int size = (int) snapshot.child(aquariumID).child("title").getValue();

                            return new Aquarium(title, size, "Liters", type);
                        }
                    }).build();

        mFirebaseAdapter = new FirebaseRecyclerAdapter<Aquarium, AquariumViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull AquariumViewHolder aquariumViewHolder, int i, @NonNull Aquarium aquarium) {
                aquariumViewHolder.setTitle(aquarium.getTitle());
                aquariumViewHolder.setType(aquarium.getType());
                aquariumViewHolder.setVolume(aquarium.getVolume());
            }

            @NonNull
            @Override
            public AquariumViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                return new AquariumViewHolder(inflater.inflate(R.layout.list_item_aquarium,
                        parent, false));
            }
        };

        mFirebaseAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                int aquariumCount = mFirebaseAdapter.getItemCount();
            }
        });

        mRecyclerView.setAdapter(mFirebaseAdapter);
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
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mFirebaseAdapter.startListening();
    }

    @Override
    public void onStart() {
        super.onStart();
        mFirebaseAdapter.startListening();
    }


}

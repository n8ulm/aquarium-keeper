package com.n8ulm.aquariumkeeper.ui.aquarium;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.n8ulm.aquariumkeeper.R;

import java.util.HashMap;
import java.util.Map;

public class AddAquariumFragment extends Fragment
        implements AdapterView.OnItemSelectedListener{

    private AddAquariumViewModel mViewModel;
    private EditText mAquariumTitle;
    private EditText mAquariumVolume;
    private Button mAddAquariumButton;
    private Button mCancelAquariumButton;
    private String mAquariumId;

    private DatabaseReference mDatabase;
    private DatabaseReference mAquariumsRef;
    private FirebaseUser mUser;
    private static final String TAG = AddAquariumFragment.class.getSimpleName();


    public AddAquariumFragment() {
        // Required empty public constructor
    }

    public static AddAquariumFragment newInstance() {
        return new AddAquariumFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_aquarium, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mUser = FirebaseAuth.getInstance().getCurrentUser();

        mAquariumsRef = FirebaseDatabase.getInstance().getReference()
                .child("users").child(mUser.getUid()).child("aquariums");

        mAquariumsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long id = dataSnapshot.getChildrenCount() + 1;
                mAquariumId = "aquarium" + String.valueOf(id);
                Log.d(TAG, "onDataChange" + id);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "onCancelled", databaseError.toException());
            }
        });

        final Spinner typeSpinner = (Spinner) view.findViewById(R.id.type_spinner);
        if (typeSpinner != null) {
            typeSpinner.setOnItemSelectedListener(this);
        }

        ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.types_array, R.layout.support_simple_spinner_dropdown_item);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        if (typeSpinner != null) {
            typeSpinner.setAdapter(typeAdapter);
        }

        final Spinner unitSpinner = (Spinner) view.findViewById(R.id.unit_spinner);
        if (unitSpinner != null) {
            unitSpinner.setOnItemSelectedListener(this);
        }

        ArrayAdapter<CharSequence> unitAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.units_array, R.layout.support_simple_spinner_dropdown_item);
        unitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        if (unitSpinner != null) {
            unitSpinner.setAdapter(unitAdapter);
        }

        mAddAquariumButton = (Button) view.findViewById(R.id.save_aquarium_button);

        mAddAquariumButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writeAquarium(mAquariumVolume.getText().toString() + " " + unitSpinner.getSelectedItem().toString(),
                        mAquariumTitle.getText().toString(),
                        typeSpinner.getSelectedItem().toString());
            }
        });

        mCancelAquariumButton = (Button) view.findViewById(R.id.cancel_aquarium_button);

        mCancelAquariumButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(getActivity(), R.id.my_nav_host_fragment)
                        .navigate(R.id.action_addAquariumFragment_to_dashboardFragment);
            }
        });

        mAquariumTitle = view.findViewById(R.id.aquarium_title_input);
        mAquariumVolume = view.findViewById(R.id.aquarium_volume_input);

    }

    private void writeAquarium(String volume, String title, String type) {
        Map<String, Object> childdUpdates = new HashMap<>();
        childdUpdates.put("size", volume);
        childdUpdates.put("title", title);
        childdUpdates.put("type", type);

        mDatabase.child("users")
                .child(mUser.getUid())
                .child("aquariums")
                .child(mAquariumId)
                .updateChildren(childdUpdates).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getActivity(),
                        "Aquarium Added", Toast.LENGTH_LONG).show();
                Navigation.findNavController(getActivity(), R.id.my_nav_host_fragment)
                        .navigate(R.id.action_addAquariumFragment_to_dashboardFragment);
            }
        });

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(AddAquariumViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onStart() {
        super.onStart();
    }
}

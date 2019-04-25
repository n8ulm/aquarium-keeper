package com.n8ulm.aquariumkeeper.ui.log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.n8ulm.aquariumkeeper.utilities.FirebaseQueryLiveData;

public class LogFragmentViewModel extends ViewModel {
    private static final DatabaseReference AQUARIUM_KEEPER_REF =
            FirebaseDatabase.getInstance().getReference("/aquariumkeeper");

    private final FirebaseQueryLiveData liveData = new FirebaseQueryLiveData(AQUARIUM_KEEPER_REF);

    public LiveData<DataSnapshot> getDataSnapshotLiveData() {
        return liveData;
    }
}

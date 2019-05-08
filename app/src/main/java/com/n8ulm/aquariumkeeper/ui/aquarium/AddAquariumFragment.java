package com.n8ulm.aquariumkeeper.ui.aquarium;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.n8ulm.aquariumkeeper.R;

public class AddAquariumFragment extends Fragment {

    private AddAquariumViewModel mViewModel;

    public static AddAquariumFragment newInstance() {
        return new AddAquariumFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_aquarium, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(AddAquariumViewModel.class);
        // TODO: Use the ViewModel
    }

}

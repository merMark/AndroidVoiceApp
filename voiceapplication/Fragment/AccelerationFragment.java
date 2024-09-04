package com.example.voiceapplication.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.voiceapplication.R;
import com.example.voiceapplication.SensorService.AccelerationService;
import com.example.voiceapplication.SensorService.DisplayStatusService;
import com.example.voiceapplication.SensorService.LightService;
import com.example.voiceapplication.databinding.FragmentThirdBinding;

public class AccelerationFragment extends Fragment {
    private FragmentThirdBinding binding;
    private View root;


    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentThirdBinding.inflate(inflater, container, false);
        root = binding.getRoot();

        AccelerationService acceleration = new AccelerationService(getContext());
        acceleration.getAccelerationValue();

        DisplayStatusService displayStatusService = new DisplayStatusService(getContext());
        displayStatusService.getDisplayStatus();

        LightService lightService = new LightService(getContext());
        lightService.getLightValue();

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(AccelerationFragment.this)
                        .navigate(R.id.action_ThirdFragment_to_SecondFragment);
            }
        });
    }

}

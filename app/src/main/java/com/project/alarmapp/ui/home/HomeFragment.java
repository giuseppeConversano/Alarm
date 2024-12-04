package com.project.alarmapp.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.slider.Slider;
import com.project.alarmapp.R;

public class HomeFragment extends Fragment {

    private TextView tvSoundLabel, tvLightLabel, tvVibrationLabel, tvTimeAdjustments;
    private Slider sliderSound, sliderLight, sliderVibration;
    private Button btnSetAlarm, btnOpenSpotify;
    private TextView tvAlarmVolume, tvVolumePercentage, tvBattery, tvBatteryPercentage;
    private Switch switchOnOff;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize Views
        tvSoundLabel = root.findViewById(R.id.tvSoundLabel);
        tvLightLabel = root.findViewById(R.id.tvLightLabel);
        tvVibrationLabel = root.findViewById(R.id.tvVibrationLabel);
        tvTimeAdjustments = root.findViewById(R.id.tvTimeAdjustments);

        sliderSound = root.findViewById(R.id.sliderSound);
        sliderLight = root.findViewById(R.id.sliderLight);
        sliderVibration = root.findViewById(R.id.sliderVibration);

        btnSetAlarm = root.findViewById(R.id.btnSetAlarm);
        btnOpenSpotify = root.findViewById(R.id.btnOpenSpotify);

        tvAlarmVolume = root.findViewById(R.id.tvAlarmVolume);
        tvVolumePercentage = root.findViewById(R.id.tvVolumePercentage);
        tvBattery = root.findViewById(R.id.tvBattery);
        tvBatteryPercentage = root.findViewById(R.id.tvBatteryPercentage);

        switchOnOff = root.findViewById(R.id.switchOnOff);

        // Set up listeners for sliders
        setupSliderListeners();

        // Set up listeners for buttons
        setupButtonListeners();

        return root;
    }

    private void setupSliderListeners() {
        // Slider for Sound
        sliderSound.addOnChangeListener((slider, value, fromUser) -> {
            Log.d("SliderSound", "Current value: " + value);
            // Update UI or save value if needed
        });

        // Slider for Light
        sliderLight.addOnChangeListener((slider, value, fromUser) -> {
            Log.d("SliderLight", "Current value: " + value);
            // Update UI or save value if needed
        });

        // Slider for Vibration
        sliderVibration.addOnChangeListener((slider, value, fromUser) -> {
            Log.d("SliderVibration", "Current value: " + value);
            // Update UI or save value if needed
        });
    }

    private void setupButtonListeners() {
        // Set Alarm Button
        btnSetAlarm.setOnClickListener(v -> {
            Log.d("SetAlarm", "Set Alarm Button Clicked");
            // Implement logic to open a time picker or set the alarm
        });

        // Open Spotify Button
        btnOpenSpotify.setOnClickListener(v -> {
            Log.d("OpenSpotify", "Open Spotify Button Clicked");
            // Open Spotify or handle playlist functionality
        });

        // Switch On/Off
        switchOnOff.setOnCheckedChangeListener((buttonView, isChecked) -> {
            Log.d("Switch", "Alarm is " + (isChecked ? "ON" : "OFF"));
            // Enable or disable the alarm
        });
    }
}

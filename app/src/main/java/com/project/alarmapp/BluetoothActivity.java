package com.project.alarmapp;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.UUID;

public class BluetoothActivity extends AppCompatActivity {


    public BluetoothAdapter bluetoothAdapter;
    public static Boolean bluetoothActive = false;
    private static final int REQUEST_ENABLE_BT = 1;
    private static final int REQUEST_BLUETOOTH_PERMISSION = 2;
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private ArrayList<String> discoveredDevicesList = new ArrayList<>();
    private ArrayList<BluetoothDevice> discoveredDevices = new ArrayList<>();
    private ArrayAdapter<String> deviceListAdapter;
    private AlertDialog discoveryDialog;
    private BluetoothSocket btSocket;
    private InputStream inputStream;
    private OutputStream outputStream;

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);

        // Bouton Next
        findViewById(R.id.next_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // change to wifi screen
                Intent intent = new Intent(BluetoothActivity.this, WifiActivity.class);
                startActivity(intent);
                finish();
            }
        });

        //Bluetooth part
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        //permission check
        if (bluetoothAdapter == null) {
            Toast.makeText(getApplicationContext(), "Bluetooth NOT supported on this device", Toast.LENGTH_SHORT).show();
        } else {
            checkBluetoothPermission();
        }
    }
    
    
    private void checkBluetoothPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.BLUETOOTH_CONNECT}, REQUEST_BLUETOOTH_PERMISSION);
            } else {
                enableBluetooth();
            }
        } else {
            enableBluetooth();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_BLUETOOTH_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                enableBluetooth();
            } else {
                Toast.makeText(this, "Bluetooth permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @SuppressLint("MissingPermission")
    private void enableBluetooth() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
        else{
            bluetoothActive = true;
            startDiscovery();
        }
    }

    @SuppressLint("MissingPermission")
    private void startDiscovery() {
        deviceListAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, discoveredDevicesList);
        discoveredDevices.clear(); // Clear previous devices to avoid duplicates
        discoveredDevicesList.clear();
        bluetoothAdapter.startDiscovery();

        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(() -> {
            if (discoveredDevicesList.isEmpty()) {
                Toast.makeText(getApplicationContext(), "No devices found", Toast.LENGTH_SHORT).show();
            }
        }, 10000);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select a Bluetooth Device");
        ListView deviceListView = new ListView(this);
        deviceListView.setAdapter(deviceListAdapter);
        builder.setView(deviceListView);
        builder.setPositiveButton("Cancel", (dialog, which) -> bluetoothAdapter.cancelDiscovery());
        discoveryDialog = builder.show();

        deviceListView.setOnItemClickListener((parent, view, position, id) -> {
            BluetoothDevice selectedDevice = discoveredDevices.get(position);
            Log.d("MainActivity", "Device selected: " + selectedDevice.getName() + " - " + selectedDevice.getAddress());
            Toast.makeText(getApplicationContext(), "Connecting to " + selectedDevice.getName(), Toast.LENGTH_SHORT).show();
            bluetoothAdapter.cancelDiscovery();
            connectToDevice(selectedDevice);  // Connect to the selected device
            discoveryDialog.dismiss();
        });
    }

    @SuppressLint("MissingPermission")
    private void connectToDevice(BluetoothDevice device) {
        new Thread(() -> {
            try {
                Log.d("MainActivity", "Attempting to connect to device: " + device.getName() + " - " + device.getAddress());
                btSocket = device.createRfcommSocketToServiceRecord(MY_UUID);
                bluetoothAdapter.cancelDiscovery();
                btSocket.connect();
                inputStream = btSocket.getInputStream();
                outputStream = btSocket.getOutputStream();
                runOnUiThread(() -> Toast.makeText(getApplicationContext(), "Connected to " + device.getName(), Toast.LENGTH_SHORT).show());
            } catch (IOException e) {
                Log.e("Bluetooth", "Connection failed", e);
                runOnUiThread(() -> Toast.makeText(getApplicationContext(), "Connection to " + device.getName() + " failed", Toast.LENGTH_SHORT).show());
                try {
                    if (btSocket != null) btSocket.close();
                } catch (IOException ex) {
                    Log.e("Bluetooth", "Socket close failed", ex);
                }
            }
        }).start();
    }

}

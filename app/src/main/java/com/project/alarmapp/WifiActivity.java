package com.project.alarmapp;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class WifiActivity extends AppCompatActivity {

    private static final int REQUEST_LOC_PERMISSION = 3;

    private WifiManager wifiManager;
    private ArrayAdapter<String> wifiListAdapter;
    private ListView wifiListView;

    private BluetoothSocket bluetoothSocket; //from previous layout
    private OutputStream outputStream;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi);

        findViewById(R.id.skip_wifi_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WifiActivity.this, MainScreenActivity.class);
                startActivity(intent);
                finish();
            }
        });
        //verify the bluetooth connection
//        if (!isBluetoothConnected()) {
//            Toast.makeText(this, "Bluetooth connection lost. Returning to Bluetooth setup.", Toast.LENGTH_SHORT).show();
//            redirectToBluetoothActivity();
//            return;
//        }

        //wifi part
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        if (wifiManager == null) {
            Toast.makeText(this, "Wi-Fi not supported on this device", Toast.LENGTH_SHORT).show();
        } else {
            checkWifiPermission();
        }
    }


    private boolean isBluetoothConnected() {
        return bluetoothSocket != null && bluetoothSocket.isConnected();
    }

    private void redirectToBluetoothActivity() {
        Intent intent = new Intent(WifiActivity.this, BluetoothActivity.class);
        startActivity(intent);
        finish();
    }

    private void checkWifiPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOC_PERMISSION);
        } else if (!wifiManager.isWifiEnabled()) {
            Intent wifiSettingsIntent = new Intent(WifiManager.ACTION_PICK_WIFI_NETWORK);
            startActivity(wifiSettingsIntent);
        } else {
            scanWifiNetworks();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //verify the state of wifi when user come back on app from settings
        checkWifiPermission();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOC_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                checkWifiPermission();
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

}



package com.project.alarmapp;

import android.bluetooth.BluetoothSocket;

public class BluetoothConnectionManager {
    private static BluetoothSocket bluetoothSocket;

    public static BluetoothSocket getBluetoothSocket() {
        return bluetoothSocket;
    }

    public static void setBluetoothSocket(BluetoothSocket socket) {
        bluetoothSocket = socket;
    }
}

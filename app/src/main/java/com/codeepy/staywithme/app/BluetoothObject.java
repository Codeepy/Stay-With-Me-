package com.codeepy.staywithme.app;

/**
 * Created by dumbastic on 01/11/2014.
 */
public class BluetoothObject  {
    private String address;
    private String name;
    private int rssi;

    public BluetoothObject(String address, String name, int rssi) {
        this.address = address;
        this.name = name;
        this.rssi = rssi;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getRssi() {
        return rssi;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }
}

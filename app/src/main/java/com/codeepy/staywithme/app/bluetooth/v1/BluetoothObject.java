package com.codeepy.staywithme.app.bluetooth.v1;

/**
 * Created by dumbastic on 01/11/2014.
 */
public class BluetoothObject  {
    private String address;
    private String name;
    private String rssi;

    public BluetoothObject(String address, String name, String rssi) {
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

    public String getRssi() {
        return rssi;
    }

    public void setRssi(String rssi) {
        this.rssi = rssi;
    }
}

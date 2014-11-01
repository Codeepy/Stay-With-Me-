package com.codeepy.staywithme.app;

import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.*;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;


public class ScanBluetoothActivity extends ListActivity {

    BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
    private BluetoothDeviceListAdapter bdla;

    private final BroadcastReceiver receiver = new BroadcastReceiver(){
        @Override
        public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if(BluetoothDevice.ACTION_FOUND.equals(action)) {
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            String name = intent.getStringExtra(BluetoothDevice.EXTRA_NAME);
            int rssi = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, Short.MIN_VALUE) * -1;
            bdla.addDevice(new BluetoothObject(device.getAddress(), device.getName() == null ? name : device.getName(), rssi));
            bdla.notifyDataSetChanged();
        }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        bdla = new BluetoothDeviceListAdapter();
        setListAdapter(bdla);

        registerReceiver(receiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));
        adapter.startDiscovery();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.bluetooth, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            bdla.clear();
            bdla.notifyDataSetChanged();
            adapter.startDiscovery();
        }
        return super.onOptionsItemSelected(item);
    }

    private class BluetoothDeviceListAdapter extends BaseAdapter {
        private ArrayList<BluetoothObject> bluetoothObjects;
        private LayoutInflater mInflator;

        public BluetoothDeviceListAdapter() {
            super();
            this.bluetoothObjects = new ArrayList<BluetoothObject>();
            mInflator = ScanBluetoothActivity.this.getLayoutInflater();
        }

        public void addDevice(BluetoothObject device) {
            if(!bluetoothObjects.contains(device)) {
                bluetoothObjects.add(device);
            }
        }

        public BluetoothObject getDevice(int position) {
            return bluetoothObjects.get(position);
        }

        public void clear() {
            bluetoothObjects.clear();
        }

        @Override
        public int getCount() {
            return bluetoothObjects.size();
        }

        @Override
        public Object getItem(int i) {
            return bluetoothObjects.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder;
            // General ListView optimization code.
            if (view == null) {
                view = mInflator.inflate(R.layout.listitem_device, null);
                viewHolder = new ViewHolder();
                viewHolder.deviceAddress = (TextView) view.findViewById(R.id.device_address);
                viewHolder.deviceName = (TextView) view.findViewById(R.id.device_name);
                viewHolder.deviceRSSI = (TextView) view.findViewById(R.id.device_rssi);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }

            BluetoothObject device = bluetoothObjects.get(i);
            final String deviceName = device.getName();
            if (deviceName != null && deviceName.length() > 0)
                viewHolder.deviceName.setText(deviceName);
            else
                viewHolder.deviceName.setText(R.string.unknown_device);
            viewHolder.deviceAddress.setText(device.getAddress());
            viewHolder.deviceRSSI.setText(device.getRssi() + " dB");

            return view;
        }

        class ViewHolder {
            TextView deviceName;
            TextView deviceAddress;
            TextView deviceRSSI;
        }
    }
}

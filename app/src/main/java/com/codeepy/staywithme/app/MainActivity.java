package com.codeepy.staywithme.app;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.nfc.*;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import com.codeepy.staywithme.app.bluetooth.v1.ScanBluetoothActivity;
import com.codeepy.staywithme.app.bluetooth.v2.DeviceScanActivity;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NfcManager manager = (NfcManager) getApplicationContext().getSystemService(Context.NFC_SERVICE);
        NfcAdapter adapter = manager.getDefaultAdapter();
        if (adapter == null) {
            Toast.makeText(this, "This device doesn't support NFC.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        if (adapter.isEnabled()) {
//            ((CheckBox)findViewById(R.id.checkbox_nfc)).setChecked(true);
//            ((CheckBox)findViewById(R.id.checkbox_nfc)).setText("NFS is enabled");
        }
        else {
//            ((CheckBox)findViewById(R.id.checkbox_nfc)).setChecked(false);
//            ((CheckBox)findViewById(R.id.checkbox_nfc)).setText("NFS is disabled");
        }

        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter.isEnabled()) {
//            ((CheckBox)findViewById(R.id.checkbox_bluetooth)).setChecked(true);
//            ((CheckBox)findViewById(R.id.checkbox_bluetooth)).setText("Bluetooth is enabled");
        }
        else {
//            ((CheckBox)findViewById(R.id.checkbox_bluetooth)).setChecked(false);
//            ((CheckBox)findViewById(R.id.checkbox_bluetooth)).setText("Bluetooth is disabled");
        }

//        final Button button = (Button) findViewById(R.id.register);
//        button.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                // Perform action on click
//                role = MINION;
//            }
//        });
//
//        button.setOnLongClickListener(new View.OnLongClickListener() {
//            public boolean onLongClick(View v) {
//                // Perform action on click
//                role = BOSS;
//                return true;
//            }
//        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_bluetooth) {
            Intent intent = new Intent(this, DeviceScanActivity.class);
//            Intent intent = new Intent(this, ScanBluetoothActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

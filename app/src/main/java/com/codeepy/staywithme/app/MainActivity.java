package com.codeepy.staywithme.app;

import android.app.Activity;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.NfcManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.codeepy.staywithme.app.bluetooth.v2.DeviceScanActivity;

public class MainActivity extends Activity implements View.OnClickListener {

    public static final String MIME_TEXT_PLAIN = "text/plain";
    public static final String TAG = "NfcDemo";

    private TextView mTextView;
    private NfcAdapter mNfcAdapter;

    private final int BOSS = 0;
    private final int MINION = 1;
    private int role;
    private boolean isNfcExist = true;
    private boolean isNfcEnabled;
    private boolean isBluetoothEnabled;

    private ImageButton btnNfc;
    private ImageButton btnBluetooth;
    private ImageButton btnSwm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnNfc = (ImageButton) findViewById(R.id.btn_nfc);
        btnBluetooth = (ImageButton) findViewById(R.id.btn_bluetooth);
        btnSwm = (ImageButton) findViewById(R.id.btn_swm);

        btnNfc.setOnClickListener(this);
    }

    /**
     * Called after {@link #onCreate} &mdash; or after {@link #onRestart} when
     * the activity had been stopped, but is now again being displayed to the
     * user.  It will be followed by {@link #onResume}.
     * <p/>
     * <p><em>Derived classes must call through to the super class's
     * implementation of this method.  If they do not, an exception will be
     * thrown.</em></p>
     *
     * @see #onCreate
     * @see #onStop
     * @see #onResume
     */
    @Override
    protected void onStart() {
        super.onStart();
        NfcManager manager = (NfcManager) getApplicationContext().getSystemService(Context.NFC_SERVICE);
        NfcAdapter adapter = manager.getDefaultAdapter();
        if (adapter == null) {
            isNfcExist = false;
            finish();
            return;
        }

        if (adapter.isEnabled()) {
            isNfcEnabled = true;
            btnNfc.setBackground(getResources().getDrawable(R.drawable.button_on));
        } else {
            isNfcEnabled = false;
        }

        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter.isEnabled()) {
            isBluetoothEnabled = true;
            btnBluetooth.setBackground(getResources().getDrawable(R.drawable.button_on));
        } else {
            isBluetoothEnabled = false;
        }
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
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_nfc:
                if (!isNfcExist) {
                    Toast.makeText(this, "This device doesn't support NFC.", Toast.LENGTH_LONG).show();
                } else {
                    if (!isNfcEnabled){
                        startActivity(new Intent(android.provider.Settings.ACTION_NFC_SETTINGS));
                    }
                }
        }
    }
}

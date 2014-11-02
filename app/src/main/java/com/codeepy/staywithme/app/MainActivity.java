package com.codeepy.staywithme.app;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.nfc.*;
import android.nfc.tech.IsoDep;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.codeepy.staywithme.app.bluetooth.v2.DeviceScanActivity;
import com.codeepy.staywithme.app.enums.Codeepy;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class MainActivity extends Activity implements View.OnClickListener, View.OnLongClickListener, IsoDepTransceiver.OnMessageReceived, NfcAdapter.ReaderCallback {

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
    private boolean isNfcBroadcast = false;

    private ImageButton btnNfc;
    private ImageButton btnBluetooth;
    private ImageButton btnSwm;

    private NfcAdapter nfcAdapter;
    private ListView listView;
    private IsoDepAdapter isoDepAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.list);
        isoDepAdapter = new IsoDepAdapter(getLayoutInflater());
        listView.setAdapter(isoDepAdapter);
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        btnNfc = (ImageButton) findViewById(R.id.btn_nfc);
        btnBluetooth = (ImageButton) findViewById(R.id.btn_bluetooth);
        btnSwm = (ImageButton) findViewById(R.id.btn_swm);

        btnNfc.setOnClickListener(this);
        btnBluetooth.setOnClickListener(this);
        btnSwm.setOnClickListener(this);
        btnSwm.setOnLongClickListener(this);
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
            btnNfc.setBackground(getResources().getDrawable(R.drawable.button_off));
        }

        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter.isEnabled()) {
            isBluetoothEnabled = true;
            btnBluetooth.setBackground(getResources().getDrawable(R.drawable.button_on));
        } else {
            isBluetoothEnabled = false;
            btnBluetooth.setBackground(getResources().getDrawable(R.drawable.button_off));
        }
    }



    @Override
    public void onResume() {
        super.onResume();
        if (isNfcBroadcast)
            nfcAdapter.enableReaderMode(this, this, NfcAdapter.FLAG_READER_NFC_A | NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK,
                    null);
    }

    @Override
    public void onPause() {
        super.onPause();
        nfcAdapter.disableReaderMode(this);
    }

    @Override
    public void onTagDiscovered(Tag tag) {
        IsoDep isoDep = IsoDep.get(tag);
        IsoDepTransceiver transceiver = new IsoDepTransceiver(isoDep, this);
        Thread thread = new Thread(transceiver);
        thread.start();

        Log.v(Codeepy.TAG.toString(), byteArrayToHex(tag.getId()) + " " + tag.toString() + " ");
        for (String tech : tag.getTechList()){
            Log.v(Codeepy.TAG.toString(), tech);
        }
    }

    public static String byteArrayToHex(byte[] a) {
        StringBuilder sb = new StringBuilder(a.length * 2);
        for (byte b : a) sb.append(String.format("%02x", b & 0xff));
        return sb.toString();
    }

    @Override
    public void onMessage(final byte[] message) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                isoDepAdapter.addMessage(new String(message));
            }
        });
    }

    @Override
    public void onError(Exception exception) {
        onMessage(exception.getMessage().getBytes());
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
                    startActivity(new Intent(android.provider.Settings.ACTION_NFC_SETTINGS));
                }
                break;
            case R.id.btn_bluetooth:
                startActivity(new Intent(android.provider.Settings.ACTION_BLUETOOTH_SETTINGS));
                break;
            case R.id.btn_swm:
                Log.v(Codeepy.TAG.toString(), "IM SLAVE");
                btnSwm.setBackground(getResources().getDrawable(R.drawable.button_on));
                break;
        }
    }


    /**
     * Called when a view has been clicked and held.
     *
     * @param v The view that was clicked and held.
     * @return true if the callback consumed the long click, false otherwise.
     */
    @Override
    public boolean onLongClick(View v) {
        Log.v(Codeepy.TAG.toString(), "IM MASTER");
        btnSwm.setBackground(getResources().getDrawable(R.drawable.button_master));
        if (!isNfcBroadcast) {
            isNfcBroadcast = true;
            nfcAdapter.enableReaderMode(this, this, NfcAdapter.FLAG_READER_NFC_A | NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK,
                    null);
        } else {
            isNfcBroadcast = false;
            nfcAdapter.disableReaderMode(this);
        }
        return true;
    }

    private NdefRecord createRecord(String text) throws UnsupportedEncodingException {

        //create the message in according with the standard
        String lang = "en";
        byte[] textBytes = text.getBytes();
        byte[] langBytes = lang.getBytes("US-ASCII");
        int langLength = langBytes.length;
        int textLength = textBytes.length;

        byte[] payload = new byte[1 + langLength + textLength];
        payload[0] = (byte) langLength;

        // copy langbytes and textbytes into payload
        System.arraycopy(langBytes, 0, payload, 1, langLength);
        System.arraycopy(textBytes, 0, payload, 1 + langLength, textLength);

        NdefRecord recordNFC = new NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT, new byte[0], payload);
        return recordNFC;
    }

    private void write(String text, Tag tag) throws IOException, FormatException {

        NdefRecord[] records = { createRecord(text) };
        NdefMessage message = new NdefMessage(records);
        Ndef ndef = Ndef.get(tag);
        ndef.connect();
        ndef.writeNdefMessage(message);
        ndef.close();
    }
}

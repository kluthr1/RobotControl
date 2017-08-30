package com.luthria.anonymousdodo.bluetoothrobo;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    private static final String TAG ="TAG:" ;
    private Joystick joy;
    private BluetoothAdapter btAdapter = null;
    private BluetoothSocket btSocket = null;
    private TextView text;
    private TextView t2;

    private OutputStream writer;
    // SPP UUID service
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    // MAC-address of Bluetooth module (you must edit this line)
    private static String address = "00:15:FF:F2:19:5F";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        btAdapter = BluetoothAdapter.getDefaultAdapter();       // get Bluetooth adapter

        setContentView(R.layout.activity_main);
        joy = (Joystick) findViewById(R.id.joystick);
        TextView tx = (TextView) findViewById(R.id.textView);
        tx.setText("The Offical Robot Controller for AHS MESA BattleBall Team");
        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "font.ttf");
        tx.setTypeface(custom_font);
        TextView t = (TextView) findViewById(R.id.t);
        t.setTypeface(custom_font);
        t.setText("Joystick Message");
        joy.t(t);
        t2 =  (TextView) findViewById(R.id.t2);
        t2.setText("Auto Message");
        t2.setTypeface(custom_font);
        btAdapter = BluetoothAdapter.getDefaultAdapter();       // get Bluetooth adapter
        checkBTState();
    }

    public void onClick(View v){
        try {
            writer.write(new String("XAutoStart").getBytes());
            t2.setText("Auto Sent");
            Toast toast = Toast.makeText(getApplicationContext(), "Auto Sent", Toast.LENGTH_SHORT);
            toast.show();

        } catch (IOException e) {
            t2.setText("Error Sending");
            e.printStackTrace();
        }

    }
    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {
        if(Build.VERSION.SDK_INT >= 10){
            try {
                final Method m = device.getClass().getMethod("createInsecureRfcommSocketToServiceRecord", new Class[] { UUID.class });
                return (BluetoothSocket) m.invoke(device, MY_UUID);
            } catch (Exception e) {
                Log.e(TAG, "Could not create Insecure RFComm Connection",e);
            }
        }
        return  device.createRfcommSocketToServiceRecord(MY_UUID);
    }

    public void onResume() {
        super.onResume();

        Log.d(TAG, "...onResume - try connect...");
        BluetoothDevice device = null;
        // Set up a pointer to the remote node using it's address.
        Set<BluetoothDevice> pairedDevices =    btAdapter.getBondedDevices();

        if (pairedDevices.size() > 0) {
            // There are paired devices. Get the name and address of each paired device.
            for (BluetoothDevice d : pairedDevices) {
                device = d;
            }
        }
        // Two things are needed to make a connection:
        //   A MAC address, which we got above.
        //   A Service ID or UUID.  In this case we are using the
        //     UUID for SPP.

        try {
            btSocket = createBluetoothSocket(device);
        } catch (IOException e) {
            finish();
        }

        // Discovery is resource intensive.  Make sure it isn't going on
        // when you attempt to connect and pass your message.
        btAdapter.cancelDiscovery();

        // Establish the connection.  This will block until it connects.
        Log.d(TAG, "...Connecting...");
        try {
            btSocket.connect();
            Log.d(TAG, "....Connection ok...");
        } catch (IOException e) {
            try {
                btSocket.close();
            } catch (IOException e2) {
                finish();
            }
        }

        // Create a data stream so we can talk to server.
        Log.d(TAG, "...Create Socket...");
        try {
            writer = btSocket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        joy.setWriter(writer);

    }

    @Override
    public void onPause() {
        super.onPause();

        Log.d(TAG, "...In onPause()...");

        try     {
            btSocket.close();
        } catch (IOException e2) {
            finish();
        }
    }

    private void checkBTState() {
        // Check for Bluetooth support and then check to make sure it is turned on
        // Emulator doesn't support Bluetooth and will return null
        if(btAdapter==null) {
            finish();
        } else {
            if (btAdapter.isEnabled()) {
                Log.d(TAG, "...Bluetooth ON...");
            } else {
                //Prompt user to turn on Bluetooth
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, 1);
            }
        }
    }
}

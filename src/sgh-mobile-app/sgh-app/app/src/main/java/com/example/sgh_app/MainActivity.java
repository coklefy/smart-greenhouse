package com.example.sgh_app;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.RadioButton;
import android.widget.RadioGroup.OnCheckedChangeListener;

import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private BluetoothChannel btChannel;
    private BluetoothAdapter btAdapter;
    private  RadioGroup radioGroup;
    //private RadioButton radioButton;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btAdapter = BluetoothAdapter.getDefaultAdapter();

        if (btAdapter != null && !btAdapter.isEnabled()) {
            startActivityForResult(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE), Utils.bluetooth.ENABLE_BT_REQUEST);
        }

        initUI();
    }

    private void initUI() {
        findViewById(R.id.btn_open).setEnabled(false);
        findViewById(R.id.btn_closed).setEnabled(false);

        findViewById(R.id.radioGroup).setEnabled(false);
        findViewById(R.id.btn_connect).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btAdapter != null && !btAdapter.isEnabled()) {
                    startActivityForResult(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE), Utils.bluetooth.ENABLE_BT_REQUEST);
                }
                try {
                    connectToBTServer();

                } catch (BluetoothDeviceNotFound bluetoothDeviceNotFound) {
                    bluetoothDeviceNotFound.printStackTrace();
                }
            }
        });

        findViewById(R.id.btn_closed).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (btChannel != null) {
                    btChannel.sendMessage("2");
                }

            }
        });

        findViewById(R.id.btn_open).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (btChannel != null) {
                    btChannel.sendMessage("1");
                }
            }
        });

        radioGroup = findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener( new RadioGroup.OnCheckedChangeListener(){
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (btChannel != null) {
                    int quantity;
                    String message;

                    switch (checkedId) {
                        case R.id.radioMin:
                            // do operations
                            quantity = 1;
                            message = String.valueOf(quantity);
                            btChannel.sendMessage(message);
                            break;
                        case R.id.radioMed:
                            // do operations
                             quantity = 2;
                             message = String.valueOf(quantity);
                            btChannel.sendMessage(message);

                            break;
                        case R.id.radioMax:
                            // do operations
                            quantity = 3;
                            message = String.valueOf(quantity);
                            btChannel.sendMessage(message);
                            break;
                    }
                }
            }
        });


    }

    @Override
    protected void onStop() {
        super.onStop();
        if (btChannel != null) {
            btChannel.close();
        }
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, @Nullable final Intent data) {
        if (requestCode == Utils.bluetooth.ENABLE_BT_REQUEST && resultCode == RESULT_OK) {
            Log.d(Utils.APP_LOG_TAG, "Bluetooth enabled!");
        }

        if (requestCode == Utils.bluetooth.ENABLE_BT_REQUEST && resultCode == RESULT_CANCELED) {
            Log.d(Utils.APP_LOG_TAG, "Bluetooth not enabled!");
        }
    }


    private void connectToBTServer() throws BluetoothDeviceNotFound {
        final BluetoothDevice serverDevice = BluetoothUtils.getPairedDeviceByName(Utils.bluetooth.BT_DEVICE_ACTING_AS_SERVER_NAME);
        final UUID uuid = BluetoothUtils.generateUuidFromString(Utils.bluetooth.BT_SERVER_UUID);

        //CORE
        AsyncTask<Void, Void, Integer> execute = new ConnectToBluetoothServerTask(serverDevice, uuid, new ConnectionTask.EventListener() {
            @Override
            public void onConnectionActive(final BluetoothChannel channel) {

                ((TextView) findViewById(R.id.statusLabel)).setText("- STATO: Modalità automatica, avvicinati alla serra per utilizzare i comandi");

                findViewById(R.id.btn_connect).setEnabled(false);
                btChannel = channel;
                btChannel.registerListener(new RealBluetoothChannel.Listener() {
                    @Override
                    public void onMessageReceived(String receivedMessage) {

                        if(receivedMessage.equals("ManIn")){
                            ((TextView) findViewById(R.id.statusLabel)).setText("- STATO: Modalità manuale");
                            findViewById(R.id.btn_open).setEnabled(true);
                            findViewById(R.id.radioGroup).setEnabled(true);
                        } else if(receivedMessage.equals("ManOut")){
                            ((TextView) findViewById(R.id.statusLabel)).setText("- STATO: Modalità automatica, avvicinati alla serra per utilizzare i comandi");
                            findViewById(R.id.btn_open).setEnabled(false);
                            findViewById(R.id.btn_closed).setEnabled(false);
                            findViewById(R.id.radioGroup).setEnabled(false);

                        } else if(receivedMessage.equals("Start")){
                            ((TextView) findViewById(R.id.statusLabel)).setText("- STATO: Sto Irrigando");
                            findViewById(R.id.btn_open).setEnabled(false);
                            findViewById(R.id.btn_closed).setEnabled(true);
                            findViewById(R.id.radioGroup).setEnabled(false);
                        } else if(receivedMessage.equals("Stop")){
                            ((TextView) findViewById(R.id.statusLabel)).setText("- STATO: Irrigazione Terminata");
                            findViewById(R.id.btn_open).setEnabled(true);
                            findViewById(R.id.btn_closed).setEnabled(false);
                            findViewById(R.id.radioGroup).setEnabled(true);
                        } else{
                            ((TextView) findViewById(R.id.chatLabel)).setText((String.format("- UMIDITA: percepita in real-time %s",
                                    receivedMessage)));
                            ((TextView) findViewById(R.id.chatLabel)).append("%");
                        }

                    }

                    @Override
                    public void onMessageSent(String sentMessage) {
                        ////
                    }
                });
            }

            @Override
            public void onConnectionCanceled() {
                ((TextView) findViewById(R.id.statusLabel)).setText(String.format("Status : unable to connect, device %s not found!",
                        Utils.bluetooth.BT_DEVICE_ACTING_AS_SERVER_NAME));
            }
        }).execute();


        new AsyncTask<Void, Void, Void>(){
            protected Void doInBackground(Void... voids) {
                while (!isCancelled()) {
                    if(btChannel != null){
                        btChannel.sendMessage("6");
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                }
                return null;
            }
            @Override
            protected void onCancelled() {
                super.onCancelled();
            }
        }.execute();


    }
}
package com.pktworld.emittoz.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.RemoteException;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.pktworld.emittoz.R;
import com.pktworld.emittoz.util.ApplicationDatabase;
import com.pktworld.emittoz.util.Applicationconstants;
import com.pktworld.emittoz.util.DatabaseModel;
import com.pktworld.emittoz.util.Utils;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.text.DecimalFormat;
import java.util.Collection;

/**
 * Created by ubuntu1 on 22/5/16.
 */
public class ScannBleActivity extends AppCompatActivity implements View.OnClickListener,BeaconConsumer, RangeNotifier{
    private static final String TAG = ScannBleActivity.class.getSimpleName();
    private Button btnScann,btnNext;
    private EditText editDeviceName;
    private ApplicationDatabase database;
    private BeaconManager mBeaconManager;
    private TextView txtBluetoothStatus,txtData;
    private ProgressBar mProgressBar;
    private String nameSpaceId, mInstanceId,distance = null;
    private String deviceId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scann);
        database = new ApplicationDatabase(this);
        isDevices();
        LocalBroadcastManager.getInstance(ScannBleActivity.this).registerReceiver(mMessageReceiver,
                new IntentFilter(Applicationconstants.BLUETOOTH_ENABLE_BROADCAST));
        LocalBroadcastManager.getInstance(ScannBleActivity.this).registerReceiver(mMessageReceiver1,
                new IntentFilter(Applicationconstants.BLUETOOTH_DATA_RECEIVED));



        btnScann = (Button)findViewById(R.id.btnScann);
        btnNext = (Button)findViewById(R.id.btnProceed);
        editDeviceName = (EditText)findViewById(R.id.editDeviceName);
        txtBluetoothStatus = (TextView)findViewById(R.id.txtBluetoothStatus);
        txtData = (TextView)findViewById(R.id.txtData);
        mProgressBar = (ProgressBar)findViewById(R.id.progressBar);

        btnScann.setOnClickListener(this);
        btnNext.setOnClickListener(this);
        txtBluetoothStatus.setOnClickListener(this);
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action.equals(Applicationconstants.BLUETOOTH_ENABLE_BROADCAST)){
                int data = intent.getExtras().getInt(Applicationconstants.FLAG1);
                Log.e("TAG",""+data);
                //int data = Integer.parseInt(message);
                switch (data){
                    case 1:
                        Handler threadHandler = new Handler(Looper.getMainLooper());
                        threadHandler.postDelayed(new Runnable() {

                            public void run() {
                                // TODO Auto-generated method stub
                                onResume();
                            }
                        }, 1500L);
                        break;
                }

            }

        }
    };
    private BroadcastReceiver mMessageReceiver1 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action.equals(Applicationconstants.BLUETOOTH_DATA_RECEIVED)){
                String  data1 = intent.getExtras().getString(Applicationconstants.FLAG1);
                String  data2 = intent.getExtras().getString(Applicationconstants.FLAG2);
                String  data3 = intent.getExtras().getString(Applicationconstants.FLAG3);
                Log.e("TAG", "" + data1 + " " + data2 + " " + data3);
                deviceId = data1+data2;
                txtData.setText("Device Found\n\nNameSpaceId: " + data1.toString()
                        + "\nInstanceId: " + data2.toString() + "\nDistance: " + data3 + " Meter Away");

                mBeaconManager.unbind(ScannBleActivity.this);
               // btnScann.setVisibility(View.GONE);
                mProgressBar.setVisibility(View.GONE);
                btnNext.setVisibility(View.VISIBLE);


            }

        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        if (Utils.isBluetoothEnable()){
            txtBluetoothStatus.setVisibility(View.GONE);
        }else {
            txtBluetoothStatus.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == btnScann){
            if (isDevicaNameValid()){
                if (Utils.isBluetoothEnable()){
                    scannForDevice();
                }else{
                    Utils.showToastMessage(ScannBleActivity.this,"Please TURN ON, Bluetooth first.");
                }
            }
        }else if (v == txtBluetoothStatus){
            Utils.setBluetooth(true);
        }else if (v == btnNext){
            database.addDevices(new DatabaseModel(editDeviceName.getText().toString().trim(),deviceId));
            Intent i = new Intent(ScannBleActivity.this,DeviceListActivity.class);
            startActivity(i);
        }
    }


    private boolean isDevicaNameValid(){
        if (editDeviceName.getText().toString().trim().length() == 0 | editDeviceName.getText().toString().isEmpty()){
            Utils.showToastMessage(ScannBleActivity.this, "Please enter device name.");
            return false;
        }else {
            return true;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        try{
            mBeaconManager.unbind(this);
        }catch (Exception e){

        }

    }

    @Override
    public void onPause() {
        super.onPause();
        try{
            mBeaconManager.unbind(this);
        }catch (Exception e){

        }
    }

    @Override
    public void onBeaconServiceConnect() {
        Region region = new Region("all-beacons-region", null, null, null);
        try {
            mBeaconManager.startRangingBeaconsInRegion(region);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        mBeaconManager.setRangeNotifier(this);
    }

    @Override
    public void didRangeBeaconsInRegion(Collection<Beacon> collection, Region region) {
        for (Beacon beacon : collection) {
            if (beacon.getServiceUuid() == 0xfeaa && beacon.getBeaconTypeCode() == 0x00) {
                // This is a Eddystone-UID frame
                Identifier namespaceId = beacon.getId1();
                Identifier instanceId = beacon.getId2();
                Log.d(TAG, "I see a beacon transmitting namespace id: " + namespaceId +
                        " and instance id: " + instanceId +
                        " approximately " + beacon.getDistance() + " meters away.");

                String data = namespaceId.toString()+instanceId.toString();
                nameSpaceId = namespaceId.toString();
                mInstanceId = instanceId.toString();
                distance =   ""+beacon.getDistance();
                DecimalFormat df = new DecimalFormat("#.##");
                distance = df.format(beacon.getDistance());
                sendBroadcast(nameSpaceId,mInstanceId,distance);
            }

        }
    }

    private void sendBroadcast(String nameSpaceId,String InstanceId,String distance){
        Intent intent1 = new Intent();
        intent1.setAction(Applicationconstants.BLUETOOTH_DATA_RECEIVED);
        intent1.putExtra(Applicationconstants.FLAG1, nameSpaceId);
        intent1.putExtra(Applicationconstants.FLAG2, InstanceId);
        intent1.putExtra(Applicationconstants.FLAG3, distance);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent1);
    }



    private void scannForDevice(){
        mBeaconManager = BeaconManager.getInstanceForApplication(this.getApplicationContext());
        // Detect the URL frame:
        /*mBeaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout("s:0-1=feaa,m:2-2=10,p:3-3:-41,i:4-20v"));*/
         /*Detect the main identifier (UID) frame*/
        mBeaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout("s:0-1=feaa,m:2-2=00,p:3-3:-41,i:4-13,i:14-19"));
        mBeaconManager.bind(this);

        btnScann.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.VISIBLE);
        //stopScanning();


    }


   /* private void stopScanning(){
        Handler threadHandler = new Handler(Looper.getMainLooper());
        threadHandler.postDelayed(new Runnable() {

            public void run() {
                // TODO Auto-generated method stub

            }
        }, 20000L);
    }*/

    private boolean isDevices(){
        int count = database.getDeviceCount();
        if (count == 0){
            return false;
        }else {
            Intent i = new Intent(ScannBleActivity.this,DeviceListActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            finish();
            return true;
        }
    }

}

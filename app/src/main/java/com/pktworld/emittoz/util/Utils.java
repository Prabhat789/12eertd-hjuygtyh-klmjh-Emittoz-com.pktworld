package com.pktworld.emittoz.util;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.pktworld.emittoz.R;

/**
 * Created by ubuntu1 on 22/5/16.
 */
public class Utils {

    private static Context mContext;

    public Utils(Context mContext) {
        this.mContext = mContext;
    }

    public static void showToastMessage(Context mContext, String msg) {
        LayoutInflater li = LayoutInflater.from(mContext);
        View layout = li.inflate(R.layout.custom_toast, null);
        TextView txtMsg = (TextView) layout.findViewById(R.id.txtToast);
        txtMsg.setText(msg);
        Toast toast = new Toast(mContext);
        toast.setDuration(Toast.LENGTH_SHORT);
        // toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.BOTTOM, 0, 0);
        toast.setView(layout);
        toast.show();
    }


    /*Check Bluetooth Status*/
    public static boolean isBluetoothEnable(){
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        boolean isEnabled = bluetoothAdapter.isEnabled();
        if (isEnabled){
            return true;
        }else {
            return false;
        }
    }

    /*Change Bluetooth Status*/
    public static boolean setBluetooth(boolean enable) {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        boolean isEnabled = bluetoothAdapter.isEnabled();
        if (enable && !isEnabled) {
            sendBroadcast();
            return bluetoothAdapter.enable();
        }
        else if(!enable && isEnabled) {
            return bluetoothAdapter.disable();
        }
        // No need to change bluetooth state
        return true;
    }

    private static void sendBroadcast(){
        Intent intent1 = new Intent();
        intent1.setAction(Applicationconstants.BLUETOOTH_ENABLE_BROADCAST);
        intent1.putExtra(Applicationconstants.FLAG1, Applicationconstants.BLUETOOTH_ENABLE);
        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent1);
    }
}

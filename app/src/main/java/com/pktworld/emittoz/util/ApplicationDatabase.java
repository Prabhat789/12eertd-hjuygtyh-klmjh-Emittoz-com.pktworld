package com.pktworld.emittoz.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ubuntu1 on 22/5/16.
 */
public class ApplicationDatabase extends SQLiteOpenHelper {
    private static final String TAG = ApplicationDatabase.class.getSimpleName();
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = Applicationconstants.APPLICATION_NAME;

    private static final String ID = "id";
    private static final String TABLE_DEVICES = "devices";
    private static final String DEVICE_NAME = "deviceName";
    private static final String DEVICE_ID = "deviceId";

    public ApplicationDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /*Creating Table*/
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_AUDIO_TABLE = "CREATE TABLE " + TABLE_DEVICES + "("
                + ID + " INTEGER PRIMARY KEY," + DEVICE_NAME + " TEXT,"
                + DEVICE_ID + " TEXT "  + ")";
        db.execSQL(CREATE_AUDIO_TABLE);


    }

     /*Upgrading database*/
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DEVICES);
        onCreate(db);
    }

    /*Add Devices*/
    public void addDevices(DatabaseModel imgModel) {
        // TODO Auto-generated method stub
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DEVICE_NAME, imgModel.getDeviceName());
        values.put(DEVICE_ID, imgModel.getDeviceId());
        db.insert(TABLE_DEVICES, null, values);
        db.close();

    }

    /*Get One Device by DeviceId*/
    public List<DatabaseModel> getSelectedDevice(String id) {
        // TODO Auto-generated method stub
        List<DatabaseModel> singleContact = new ArrayList<DatabaseModel>();
        String selectQuery = "SELECT * FROM " + TABLE_DEVICES+" WHERE "+DEVICE_ID+ " LIKE "+id+" ORDER BY Id DESC LIMIT 1";
        SQLiteDatabase adb = this.getWritableDatabase();
        Cursor cursor = adb.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                DatabaseModel contact = new DatabaseModel();
                contact.setId(Integer.parseInt(cursor.getString(0)));
                contact.setDeviceName(cursor.getString(1));
                contact.setDeviceId(cursor.getString(2));
                // Adding contact to list
                singleContact.add(contact);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return singleContact;

    }

    /*Get All Devices*/
    public List<DatabaseModel> getDeviceList() {
        // TODO Auto-generated method stub
        List<DatabaseModel> singleContact = new ArrayList<DatabaseModel>();
        String selectQuery = "SELECT * FROM " + TABLE_DEVICES ;
        SQLiteDatabase adb = this.getWritableDatabase();
        Cursor cursor = adb.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                DatabaseModel contact = new DatabaseModel();
                contact.setId(Integer.parseInt(cursor.getString(0)));
                contact.setDeviceName(cursor.getString(1));
                contact.setDeviceId(cursor.getString(2));
                // Adding contact to list
                singleContact.add(contact);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return singleContact;

    }

    public int getDeviceCount() {
        int count = 0;
        String countQuery = "SELECT  * FROM " + TABLE_DEVICES;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        if (cursor != null && !cursor.isClosed()) {
            count = cursor.getCount();
            cursor.close();
        }
        cursor.close();
        // return count
        return count;
    }

}

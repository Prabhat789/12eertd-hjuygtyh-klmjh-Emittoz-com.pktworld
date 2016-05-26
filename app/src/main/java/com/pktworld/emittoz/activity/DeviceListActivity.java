package com.pktworld.emittoz.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.pktworld.emittoz.R;
import com.pktworld.emittoz.adapter.DeviceListAdapter;
import com.pktworld.emittoz.util.ApplicationDatabase;
import com.pktworld.emittoz.util.DatabaseModel;
import com.pktworld.emittoz.util.SpacesItemDecoration;

import java.util.List;

/**
 * Created by ubuntu1 on 23/5/16.
 */
public class DeviceListActivity extends AppCompatActivity {
    private static final String TAG = DeviceListActivity.class.getSimpleName();
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<DatabaseModel> listDevices;
    private DeviceListAdapter mAdapter;
    private ApplicationDatabase mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.txtToolbar);
        mTitle.setText("Devices");
        getSupportActionBar().setTitle("");
        mRecyclerView = (RecyclerView) findViewById(R.id.listDevices);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(new SpacesItemDecoration(8));
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);


        mDatabase = new ApplicationDatabase(this);
        listDevices = mDatabase.getDeviceList();

        mAdapter = new DeviceListAdapter(DeviceListActivity.this,listDevices);
        mRecyclerView.setAdapter(mAdapter);


    }
}

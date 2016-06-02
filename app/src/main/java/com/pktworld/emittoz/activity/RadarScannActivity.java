package com.pktworld.emittoz.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.pktworld.emittoz.R;
import com.pktworld.emittoz.util.RadarView;

/**
 * Created by Prabhat on 02/06/16.
 */
public class RadarScannActivity extends AppCompatActivity {
    private static final String TAG = RadarScannActivity.class.getSimpleName();
    private RadarView radarView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radar_scann);

        radarView = (RadarView)findViewById(R.id.radar);
        radarView.startAnimation();
    }
}

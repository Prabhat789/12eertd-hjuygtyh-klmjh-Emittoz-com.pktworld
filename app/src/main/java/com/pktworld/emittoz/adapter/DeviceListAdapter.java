package com.pktworld.emittoz.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pktworld.emittoz.R;
import com.pktworld.emittoz.util.DatabaseModel;

import java.util.List;

/**
 * Created by ubuntu1 on 23/5/16.
 */
public class DeviceListAdapter extends RecyclerView.Adapter<DeviceListAdapter.DataObjectHolder> {

    private static final String TAG = DeviceListAdapter.class.getSimpleName();
    private List<DatabaseModel> mDataset;
    private Context mContext;

    public DeviceListAdapter(Context context, List<DatabaseModel> myDataset) {
        mDataset = myDataset;
        mContext = context;

    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_devices, parent, false);

        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, final int position) {

        holder.txtDeviceName.setText(mDataset.get(position).getDeviceName());
        holder.txtDeviceId.setText(mDataset.get(position).getDeviceId());

    }


    public void addItem(DatabaseModel dataObj, int index) {
        mDataset.add(index, dataObj);
        notifyItemInserted(index);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public class DataObjectHolder extends RecyclerView.ViewHolder {

        TextView txtDeviceName,txtDeviceId;
        CardView view;

        public DataObjectHolder(View itemView) {
            super(itemView);
            txtDeviceName = (TextView) itemView.findViewById(R.id.txtDeviceName);
            txtDeviceId = (TextView) itemView.findViewById(R.id.txtDeviceId);
            view = (CardView) itemView.findViewById(R.id.card_view);

        }

    }

}

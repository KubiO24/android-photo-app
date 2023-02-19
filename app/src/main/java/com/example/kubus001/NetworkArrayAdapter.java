package com.example.kubus001;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class NetworkArrayAdapter extends ArrayAdapter {
    private ArrayList<String> _list;
    private Context _context;
    private int _resource;

    public NetworkArrayAdapter(@NonNull Context context, int resource, @NonNull List objects) {
        super(context, resource, objects);

        this._list = (ArrayList<String>) objects;
        this._context = context;
        this._resource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(_resource, null);

        TextView text = (TextView) convertView.findViewById(R.id.network_row_text);
        ImageView image = (ImageView) convertView.findViewById(R.id.network_row_image);

        text.setText(_list.get(position));

        switch(position) {
            case 0:
                image.setImageResource(R.drawable.ic_baseline_cloud_upload_24);
                break;
            case 1:
                image.setImageResource(R.drawable.ic_baseline_share_24);
                break;
            case 2:
                image.setImageResource(R.drawable.ic_baseline_crop_24);
                break;
            case 3:
                image.setImageResource(R.drawable.ic_baseline_brush_24);
                break;
        }

        return convertView;
    }
}

package com.example.kubus001;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class RecAdapter extends RecyclerView.Adapter<RecAdapter.ViewHolder> {
    private List<Item> list;
    private Context _context;

    public RecAdapter(Context context, List<Item> list) {
        this._context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public RecAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecAdapter.ViewHolder holder, int position) {
        Item listItem = list.get(position);

        holder.timeTextView.setText(listItem.getTime());
        holder.sizeTextView.setText(listItem.getSize());

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(_context);
        String url = "http://" + preferences.getString("ip", null) + ":3000" + listItem.getUrl();
        Log.d("xxx", url);

        Picasso
            .get()
            .load(url)
            .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView timeTextView;
        private TextView sizeTextView;
        private ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            timeTextView = itemView.findViewById(R.id.cardview_time);
            sizeTextView = itemView.findViewById(R.id.cardview_size);
            imageView = itemView.findViewById(R.id.cardview_image);
        }
    }
}

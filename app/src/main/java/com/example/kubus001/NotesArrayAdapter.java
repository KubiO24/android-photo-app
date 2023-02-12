package com.example.kubus001;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class NotesArrayAdapter extends ArrayAdapter {
    private ArrayList<Note> _list;
    private Context _context;
    private int _resource;

    public NotesArrayAdapter(@NonNull Context context, int resource, @NonNull List objects) {
        super(context, resource, objects);

        this._list = (ArrayList<Note>) objects;
        this._context = context;
        this._resource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(_resource, null);
        Note note = this._list.get(position);

        TextView noteId = (TextView) convertView.findViewById(R.id.note_id);
        noteId.setText(note.get_id());

        TextView noteTitle = (TextView) convertView.findViewById(R.id.note_title);
        noteTitle.setText(note.getTitle());
        noteTitle.setTextColor(Color.parseColor("#" + note.getColor()));

        TextView noteText = (TextView) convertView.findViewById(R.id.note_text);
        noteText.setText(note.getText());

        TextView notePath = (TextView) convertView.findViewById(R.id.note_path);
        notePath.setText(note.getPath());

        ImageView noteImage = (ImageView) convertView.findViewById(R.id.note_image);
        noteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("XXX", "Klik");
            }
        });

        return convertView;
    }
}

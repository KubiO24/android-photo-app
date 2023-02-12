package com.example.kubus001;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

        return convertView;
    }
}

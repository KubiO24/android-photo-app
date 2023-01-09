package com.example.kubus001;


import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ImagesAdapter extends ArrayAdapter {
    public ImagesAdapter(@NonNull Context context, int resource, @NonNull List objects) {
        super(context, resource, objects);
        this._list= (ArrayList<File>) objects;
        this._context = context;
        this._resource = resource;
    }
    private ArrayList<File> _list;
    private Context _context;
    private int _resource;

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(_resource, null);

        ImageView imageView = (ImageView) convertView.findViewById(R.id.new_gallery_thumbnail);
        File tempFile = _list.get(position);
        Uri uri = Uri.fromFile(tempFile);
        imageView.setImageURI(uri);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(_context, PhotoActivity.class);
                intent.putExtra("imagepath", tempFile.getPath());
                _context.startActivity(intent);
            }
        });

        ImageView deletebt = convertView.findViewById(R.id.listView_deleteBtn);
        deletebt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert = new AlertDialog.Builder(_context);
                alert.setTitle("Uwaga!");
                alert.setMessage("Czy chcesz usunąc zdjecie?" + tempFile.getPath());

                alert.setPositiveButton("TAK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        tempFile.delete();
                        _list.get(position).delete();
                        _list.remove(position);
                        notifyDataSetChanged();
                    }

                });

                alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                alert.show();
            }
        });

        ImageView editbt = (ImageView) convertView.findViewById(R.id.listView_editBtn);
        editbt.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert = new AlertDialog.Builder(_context);
                alert.setTitle("Uwaga!");
                alert.setMessage("komunikat");

                LinearLayout ll = new LinearLayout(_context);
                ll.setOrientation(LinearLayout.VERTICAL);

                EditText title = new EditText(_context);
                title.setText("tytyl notatki");

                EditText noteContent = new EditText(_context);
                noteContent.setText("tresc notatki");

                ll.addView(title);
                ll.addView(noteContent);

                alert.setView(ll);

                final int[] selectedColor = {0xff000000};

                int[] arr = new int[]{0xff0099ff, 0xffff5100, 0xffeb2f58, 0xffa841d1, 0xfff765d0, 0xff7fe3bd, 0xff2fa134, 0xff074f0a};
                LinearLayout colors = new LinearLayout(_context);
                colors.setOrientation(LinearLayout.HORIZONTAL);
                for(int i :arr){
                    ImageView x = new ImageView(_context);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(100, 100);
                    x.setLayoutParams(layoutParams);
                    x.setBackgroundColor(i);
                    colors.addView(x);
                    x.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            selectedColor[0] = i;
                            title.setTextColor(selectedColor[0]);
                        }
                    });
                }
                ll.addView(colors);

                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

//                        DatabaseManager db = new DatabaseManager (
//                                _context, // activity z galerią zdjęć
//                                "NotatkiWojdylaArkadiusz.db", // nazwa bazy
//                                null,
//                                1 //wersja bazy, po zmianie schematu bazy należy ją zwiększyć
//                        );
//
//                        int color = selectedColor[0];
//                        db.insertNote( Integer.toHexString(color), title.getText().toString(), noteContent.getText().toString(), tempFile.getPath());

                    }

                });

//no
                alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //wyświetl which
                    }
                });
//
                alert.show();
            }
        });

        ImageView infobt = (ImageView) convertView.findViewById(R.id.listView_infoBtn);
        infobt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert = new AlertDialog.Builder(_context);
                alert.setTitle("Info  ");
                alert.setMessage(tempFile.getPath());
                alert.setCancelable(false);  //nie zamyka się po kliknięciu poza nim
                alert.setNeutralButton("OK", null).show();  // null to pusty click
            }
        });


        return convertView;
    }

    @Override
    public int getCount() {
        return _list.size();
    }
}

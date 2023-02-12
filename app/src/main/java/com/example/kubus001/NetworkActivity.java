package com.example.kubus001;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class NetworkActivity extends AppCompatActivity {
    private ArrayList<Item> list = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(NetworkActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(NetworkActivity.this);
        String url = "http://" + preferences.getString("ip", null) + ":3000/json";

        JsonArrayRequest jsonRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                response -> {
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject responseObj =  response.getJSONObject(i);

                            Item listItem = new Item(
                                    responseObj.getString("name"),
                                    responseObj.getString("url"),
                                    "czas zapisu: " + responseObj.getString("creationTime"),
                                    "wielkość zdjęcia: " + responseObj.getString("size") + " B"
                            );

                            list.add(listItem);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    adapter = new RecAdapter(NetworkActivity.this, list);
                    recyclerView.setAdapter(adapter);
                },
                error -> {
                    Log.d("XXX", "error" + error.getMessage());
                }
        );
        Volley.newRequestQueue(NetworkActivity.this).add(jsonRequest);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
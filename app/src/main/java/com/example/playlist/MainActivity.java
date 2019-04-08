package com.example.playlist;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import static java.util.Arrays.asList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView = findViewById(R.id.list_Canciones);

        // inicializar coleccion (bajarla de internet, etc.)
        final ArrayList<String> elementos = new ArrayList<String>(asList("Ben", "Billie Jean", "Dancing Machine", "Remember the Time",
                "Don't Stop till You Get Enough", "I Just Can´t Stop Loving You", "I Want You Back", "One More Chance", "Pretty Young Thing",
                "Rock With You", "Stranger in Moscow", "The Girl is Mine", "Wanna be Startin' Sometin'", "Who Is It"));

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, elementos);

        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getApplicationContext(), elementos.get(i), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

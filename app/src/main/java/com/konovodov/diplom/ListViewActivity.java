package com.konovodov.diplom;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class ListViewActivity extends AppCompatActivity {

    private NotesAdapter adapter;

    LocalDateTime ld;

    public ListViewActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);


        Toolbar toolbar = findViewById(R.id.toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        ListView listView = findViewById(R.id.listView);

        setSupportActionBar(toolbar);

        fillres();

//        ld = Calendar.getInstance().getTime();
        ld = LocalDateTime.now();
        long d = ld.toEpochSecond(ZoneOffset.ofHours(0));

//        long ldEp = DateTime.ge

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                generateRandomItemData();

            }
        });

        adapter = new NotesAdapter(this, null);
        listView.setAdapter(adapter);

        generateLoadedItemData();


        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(ListViewActivity.this, String.format(getString(R.string.toast_text), adapter.getItem(position).getTitle().length()), Toast.LENGTH_SHORT).show();
                return true;
            }
        });

    }

    private void fillres() {
    }

    private void generateRandomItemData() {
        adapter.addNote(new Note("1", "2", false));
    }

    private void generateLoadedItemData() {

/*
        List<String> list = externalFile.loadStringList();
        if (list == null) return;
        for (String s : list) {
            adapter.addItem(new ItemData(
                    images.get(random.nextInt(images.size())),
                    s,
                    getString(R.string.citates_from)));
        }
        Toast.makeText(ListViewActivity.this, getString(R.string.loaded, list.size()), Toast.LENGTH_SHORT).show();
*/
    }


}
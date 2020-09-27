package com.konovodov.diplom;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.*;
import java.util.List;
import java.util.Random;

public class NotesMainActivity extends AppCompatActivity {

    private NotesAdapter notesAdapter;
    private List<Note> notes;


    public NotesMainActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Toolbar toolbar = findViewById(R.id.toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        ListView notesListView = findViewById(R.id.notesListView);
        View notesListViewScreen = findViewById(R.id.notesListViewScreen);
        View getKeyScreen = findViewById(R.id.get_key_screen);
        Button button = findViewById(R.id.button);

        setSupportActionBar(toolbar);

        NoteEditor noteEditor = new NoteEditor(NotesMainActivity.this);
        notes = ThisApp.getNoteList();
        notesAdapter = new NotesAdapter(this, notes);

        button.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View view) {
//                getKeyScreen.setVisibility(View.INVISIBLE);
//                noteEditScreen.setVisibility(View.VISIBLE);

            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                noteEditor.editNote(notesAdapter, notes.size());
//                generateRandomItemData();

            }
        });

        notesListView.setAdapter(notesAdapter);

        generateLoadedItemData();


        notesListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(NotesMainActivity.this, getString(R.string.default_header), Toast.LENGTH_SHORT).show();
//                noteEditor.editNote(new Note(getString(R.string.default_header), getString(R.string.default_body), true, LocalDateTime.now().toEpochSecond(ZoneOffset.ofHours(0)), LocalDateTime.now().toEpochSecond(ZoneOffset.ofHours(0))));
                noteEditor.editNote(notesAdapter, position);
                return true;
            }
        });

    }

/*
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
*/



    private void generateRandomItemData() {
        Random r = new Random();
        Random r1 = new Random();
        notesAdapter.addNote(new Note(getString(R.string.default_header), getString(R.string.default_body), true, LocalDateTime.now().toEpochSecond(ZoneOffset.ofHours(0)) + 86400*(r.nextInt(3) - 1), LocalDateTime.now().toEpochSecond(ZoneOffset.ofHours(0))));
    }

    private void generateLoadedItemData() {

    }


}
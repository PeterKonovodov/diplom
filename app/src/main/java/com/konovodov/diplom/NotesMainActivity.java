package com.konovodov.diplom;

import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class NotesMainActivity extends AppCompatActivity {

    private NotesAdapter notesAdapter;
    private List<Note> noteList;
    private Toolbar toolbar;
    private PinManager pinManager;
    private View notesListViewScreen;
    private View getKeyScreen;


    public NotesMainActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initNotesListView();
        pinManager = new PinManager(this);


    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        if(ThisApp.isColdAppStart()) {
            ThisApp.setColdAppStart(false);

            if(pinManager.hasPin()) {
                //при наличии пин-кода скрываем список и открываем запрос пина
                ((TextView)findViewById(R.id.get_key_screen_header)).setText(getString(R.string.enter_pin));

                //скрываем из меню возможность установки нового пина при первичном запросе
                MenuItem menuSetPinItem = toolbar.getMenu().findItem(R.id.action_set_new_pin);
                menuSetPinItem.setVisible(false);

                notesListViewScreen.setVisibility(View.INVISIBLE);
                getKeyScreen.setVisibility(View.VISIBLE);


                //заряжаем коллбэк на сравнение по завершению ввода
                pinManager.SetWhatToDoWithPin(new PinManager.WhatToDoWithPin() {
                    @Override
                    public void doThis() {
                        if(pinManager.checkPin(pinManager.getEnteredPin())) {
                            Toast.makeText(NotesMainActivity.this, getString(R.string.pin_confirmed), Toast.LENGTH_SHORT).show();
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                public void run() {
                                    notesListViewScreen.setVisibility(View.VISIBLE);
                                    getKeyScreen.setVisibility(View.INVISIBLE);
                                    menuSetPinItem.setVisible(true);

                                }
                            }, 2000); //specify the number of milliseconds
                        }
                    }
                });
            }
            else {
                notesListViewScreen.setVisibility(View.VISIBLE);
                getKeyScreen.setVisibility(View.INVISIBLE);
            }
        } else {
            notesListViewScreen.setVisibility(View.VISIBLE);
            getKeyScreen.setVisibility(View.INVISIBLE);
        }




        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_set_new_pin) {
            ((TextView)findViewById(R.id.get_key_screen_header)).setText(getString(R.string.enter_new_pin));
            notesListViewScreen.setVisibility(View.INVISIBLE);
            getKeyScreen.setVisibility(View.VISIBLE);
            pinManager.clearEnteredPin();

            //
            item.setVisible(false);
            //заряжаем коллбэк на получение и сохранение нового пинкода

            pinManager.SetWhatToDoWithPin(new PinManager.WhatToDoWithPin() {
                @Override
                public void doThis() {
                    Toast.makeText(NotesMainActivity.this, getString(R.string.pin_set), Toast.LENGTH_SHORT).show();
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            notesListViewScreen.setVisibility(View.VISIBLE);
                            getKeyScreen.setVisibility(View.INVISIBLE);
                            pinManager.saveNew(pinManager.getEnteredPin());
                            item.setVisible(true);            //заряжаем коллбэк на получение и сохранение нового пинкода

                        }
                    }, 2000); //specify the number of milliseconds
                }
            });
            return true;
        }

        if (id == R.id.action_reset_pin) {
            if(!pinManager.hasPin()) {
                Toast.makeText(NotesMainActivity.this, getString(R.string.pin_not_exist), Toast.LENGTH_SHORT).show();
                return false;
            }
            //при наличии пин-кода скрываем список и открываем запрос пина
            ((TextView)findViewById(R.id.get_key_screen_header)).setText(getString(R.string.pin_confirm_to_delete));

            notesListViewScreen.setVisibility(View.INVISIBLE);
            getKeyScreen.setVisibility(View.VISIBLE);
            pinManager.clearEnteredPin();
            //заряжаем коллбэк на сравнение по завершению ввода
            pinManager.SetWhatToDoWithPin(new PinManager.WhatToDoWithPin() {
                @Override
                public void doThis() {
                    if(pinManager.checkPin(pinManager.getEnteredPin())) {
                        Toast.makeText(NotesMainActivity.this, getString(R.string.pin_deleted), Toast.LENGTH_SHORT).show();
                        pinManager.clearPin();
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {
                                notesListViewScreen.setVisibility(View.VISIBLE);
                                getKeyScreen.setVisibility(View.INVISIBLE);
                            }
                        }, 2000); //specify the number of milliseconds
                    }
                }
            });
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void initNotesListView() {

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setAlpha(0.5f);
        ListView notesListView = findViewById(R.id.notesListView);

        notesListViewScreen = findViewById(R.id.notesListViewScreen);
        getKeyScreen = findViewById(R.id.get_key_screen);

        setSupportActionBar(toolbar);

        NoteEditor noteEditor = new NoteEditor(NotesMainActivity.this);
        noteList = ThisApp.getNoteRepository().getNotes();
        notesAdapter = new NotesAdapter(this, noteList);





        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                noteEditor.editNote(notesAdapter, noteList.size());
            }
        });

        notesListView.setAdapter(notesAdapter);

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


}

/*
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
*/



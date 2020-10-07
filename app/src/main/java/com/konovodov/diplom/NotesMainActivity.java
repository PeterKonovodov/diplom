package com.konovodov.diplom;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import java.util.Locale;

public class NotesMainActivity extends AppCompatActivity {

    private NoteListFragment noteListFragment;
    private final String NOTESLIST_FRAGMENT_TAG = "NOTESLIST_FRAGMENT_TAG";
    private PinFragment pinFragment;
    private final String PIN_FRAGMENT_TAG = "PIN_FRAGMENT_TAG";

    private int activityState;
    private final int INIT_STATE = 0;
    private final int CHECK_PIN = 1;
    private final int SET_NEW_PIN = 2;
    private final int RESET_PIN = 3;
    private final int VIEW_NOTES = 4;


    private FragmentManager fragmentManager;


    private String appLocale;   //возможные значения - "en" и "ru"

    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fragmentManager = getSupportFragmentManager();
        activityState = INIT_STATE;


        if (savedInstanceState != null) {
            activityState = savedInstanceState.getInt("activityState");
            noteListFragment = (NoteListFragment)
                    getSupportFragmentManager().findFragmentByTag(NOTESLIST_FRAGMENT_TAG);
            pinFragment = (PinFragment)
                    getSupportFragmentManager().findFragmentByTag(PIN_FRAGMENT_TAG);
        }

        if (noteListFragment == null) {
            noteListFragment = new NoteListFragment();
        }
        if (pinFragment == null) {
            pinFragment = new PinFragment();
        }

        loadAppLocale();
        setLocale(false);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt("activityState", activityState);
        super.onSaveInstanceState(outState);
//        outState.putIntegerArrayList("deletedIndexes", deletedIndexes);
        //outState.put
//        Log.i(TAG, String.format("onSaveInstanceState, %d indexes saved", deletedIndexes.size()));
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        setLocaleIcon();

        switch (activityState) {
            case SET_NEW_PIN:
                setNewPIN();
                break;
            case RESET_PIN:
                resetPIN();
                break;
            case VIEW_NOTES:
                viewNotes();
                break;
            default:
                if (pinFragment.hasPin()) {
                    checkPIN();
                }
                else viewNotes();
                break;
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_switch_language) {
            if ("ru".equals(appLocale)) {
                appLocale = "en";
            } else {
                appLocale = "ru";
            }
            saveAppLocale();
            setLocale(true);
            return true;
        }

        if (id == R.id.action_set_new_pin) {

            setNewPIN();
            return true;
        }

        if (id == R.id.action_reset_pin) {
            resetPIN();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void loadAppLocale() {
        SharedPreferences sharedPreferences;
        sharedPreferences = getSharedPreferences("appLocale", Context.MODE_PRIVATE);
        if (sharedPreferences.contains("appLocale")) {
            appLocale = sharedPreferences.getString("appLocale", "");
        } else appLocale = "en";
    }

    private void saveAppLocale() {
        SharedPreferences sharedPreferences;
        sharedPreferences = getSharedPreferences("appLocale", Context.MODE_PRIVATE);
        sharedPreferences.edit().putString("appLocale", appLocale).apply();
    }

    private void setLocale(boolean needToRecreate) {
        Locale locale;
        locale = new Locale(appLocale);

        Configuration config = new Configuration();
        config.setLocale(locale);
        getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

        if (needToRecreate) recreate();
    }

    private void setLocaleIcon() {
        if ("ru".equals(appLocale)) {
            toolbar.getMenu().findItem(R.id.action_switch_language).setIcon(R.mipmap.ru);
        } else {
            toolbar.getMenu().findItem(R.id.action_switch_language).setIcon(R.mipmap.uk);
        }
    }

    private void viewNotes() {
        activityState = VIEW_NOTES;
        fragmentManager.beginTransaction().replace(R.id.fragmentContainer, noteListFragment, NOTESLIST_FRAGMENT_TAG).commit();
    }

    private void setNewPIN() {
        activityState = SET_NEW_PIN;

        pinFragment.setScreenHeader(getString(R.string.enter_new_pin));
        pinFragment.clearEnteredPin();
        fragmentManager.beginTransaction().replace(R.id.fragmentContainer, pinFragment, PIN_FRAGMENT_TAG).commit();
        //
//    item.setVisible(false);
        //заряжаем коллбэк на получение и сохранение нового пинкода
        pinFragment.SetOnPinEntered(new PinFragment.OnPinEntered() {
            @Override
            public void onPinEntered() {
                Toast.makeText(NotesMainActivity.this, getString(R.string.pin_set),
                        Toast.LENGTH_SHORT).show();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        pinFragment.saveNew(pinFragment.getEnteredPin());
                        viewNotes();
                        //                  item.setVisible(true);
                    }
                }, 2000); //specify the number of milliseconds
            }
        });
    }

    private void checkPIN() {
        activityState = CHECK_PIN;
        fragmentManager.beginTransaction().replace(R.id.fragmentContainer, pinFragment, PIN_FRAGMENT_TAG).commit();
        pinFragment.setScreenHeader(getString(R.string.enter_pin));

        //при наличии пин-кода скрываем список и открываем запрос пина

        //заряжаем коллбэк на сравнение по завершению ввода
        pinFragment.SetOnPinEntered(new PinFragment.OnPinEntered() {
            @Override
            public void onPinEntered() {
                if (pinFragment.checkPin(pinFragment.getEnteredPin())) {
                    Toast.makeText(NotesMainActivity.this, getString(R.string.pin_confirmed),
                            Toast.LENGTH_SHORT).show();
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            viewNotes();
                            //menuSetPinItem.setVisible(true);
                        }
                    }, 2000); //specify the number of milliseconds
                } else {
                    Toast.makeText(NotesMainActivity.this, getString(R.string.pin_wrong),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void resetPIN() {
        activityState = RESET_PIN;

        if (!pinFragment.hasPin()) {
            Toast.makeText(NotesMainActivity.this, getString(R.string.pin_not_exist),
                    Toast.LENGTH_SHORT).show();
        } else {
            pinFragment.setScreenHeader(getString(R.string.pin_confirm_to_delete));
            pinFragment.clearEnteredPin();

            //при наличии пин-кода скрываем список и открываем запрос пина
            fragmentManager.beginTransaction().replace(R.id.fragmentContainer, pinFragment, PIN_FRAGMENT_TAG).commit();


            //заряжаем коллбэк на сравнение по завершению ввода
            pinFragment.SetOnPinEntered(new PinFragment.OnPinEntered() {
                @Override
                public void onPinEntered() {
                    if (pinFragment.checkPin(pinFragment.getEnteredPin())) {
                        Toast.makeText(NotesMainActivity.this, getString(R.string.pin_deleted)
                                , Toast.LENGTH_SHORT).show();
                        pinFragment.clearPin();
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {
                                viewNotes();
                            }
                        }, 2000); //specify the number of milliseconds
                    } else {
                        Toast.makeText(NotesMainActivity.this, getString(R.string.pin_wrong),
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }

}
/*
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
*/



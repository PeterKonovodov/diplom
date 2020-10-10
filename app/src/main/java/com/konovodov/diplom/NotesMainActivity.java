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

    private FragmentManager fragmentManager;
    private NoteListFragment noteListFragment;
    private PinFragment pinFragment;
    //тэги, необходимые для поиска фрагмента после пересоздания активити (чтобы его не пересоздавать)
    private final String NOTESLIST_FRAGMENT_TAG = "NOTESLIST_FRAGMENT_TAG";
    private final String PIN_FRAGMENT_TAG = "PIN_FRAGMENT_TAG";

    //переменная, отражающая состояние активити, нужна для логики, управляющей пересозданием
    //активити с текущими фрагментами.
    private int activityState;
    public final int INIT_STATE = 0;
    public static final int CHECK_PIN = 1;
    public static final int SET_NEW_PIN = 2;
    public static final int RESET_PIN = 3;
    public static final int VIEW_NOTES = 4;
    private boolean coldAppStart = true;
    //переменные, необходимые для подсчета количества нажатий на кн. BACK для выхода из приложения
    private int backPressedCount;
    private long timeout;

    private String appLocale;   //текущая локаль в строковом выражении
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loadAppLocale();

        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fragmentManager = getSupportFragmentManager();
        activityState = INIT_STATE;
        backPressedCount = 0;
        timeout = System.currentTimeMillis();


        if (savedInstanceState != null) {
            activityState = savedInstanceState.getInt("activityState");
            noteListFragment = (NoteListFragment)
                    getSupportFragmentManager().findFragmentByTag(NOTESLIST_FRAGMENT_TAG);
            pinFragment = (PinFragment)
                    getSupportFragmentManager().findFragmentByTag(PIN_FRAGMENT_TAG);
            coldAppStart = false;
        } else coldAppStart = true;

        if (noteListFragment == null) {
            noteListFragment = new NoteListFragment();
        }

        //снабжаем фрагмент правильным колбэком
        if (pinFragment != null) {
            pinFragment.setOnPinEntered(getOnPinEntered(activityState));
        }

    }


    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt("activityState", activityState);
        super.onSaveInstanceState(outState);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        //скрываем из меню пункт выбора текущего языка и меняем иконку
        int iconId = R.mipmap.uk;   //иконка языка по умолчанию
        toolbar.getMenu().findItem(R.id.action_switch_language).setIcon(R.mipmap.uk);
        if ("en".equals(appLocale)) {
            toolbar.getMenu().findItem(R.id.action_set_english).setVisible(false);
        }
        if ("ru".equals(appLocale)) {
            toolbar.getMenu().findItem(R.id.action_set_russian).setVisible(false);
            iconId = R.mipmap.ru;
        }
        if ("af".equals(appLocale)) {
            toolbar.getMenu().findItem(R.id.action_set_afrikaans).setVisible(false);
            iconId = R.mipmap.za;
        }
        toolbar.getMenu().findItem(R.id.action_switch_language).setIcon(iconId);

        //обработка холодного старта. Начальная проверка пинкода запускается только здесь.
        if (coldAppStart) {
            coldAppStart = false;

            if (ThisApp.getPinStore().hasPin()) {
                checkPIN();
            } else viewNotes();
        }

        if (noteListFragment != null) {
            if (noteListFragment.isAdded()) {
                toolbar.getMenu().findItem(R.id.action_set_new_pin).setVisible(true);
                toolbar.getMenu().findItem(R.id.action_reset_pin).setVisible(true);
            }
        }
        //скрываем из меню настроек пункты с манипуляцией пин-кодом, если находимся в
        //экране работы с ним
        if (pinFragment != null) {
            if (pinFragment.isAdded()) {
                toolbar.getMenu().findItem(R.id.action_set_new_pin).setVisible(false);
                toolbar.getMenu().findItem(R.id.action_reset_pin).setVisible(false);
            }
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {
            case R.id.action_set_russian:
                appLocale = "ru";
                saveAppLocale();
                break;
            case R.id.action_set_english:
                appLocale = "en";
                saveAppLocale();
                break;
            case R.id.action_set_afrikaans:
                appLocale = "af";
                saveAppLocale();
                break;
            case R.id.action_set_new_pin:
                setNewPIN();
                break;
            case R.id.action_reset_pin:
                resetPIN();
                break;
        }
        return true;
    }


    private void loadAppLocale() {
        SharedPreferences sharedPreferences;
        sharedPreferences = getSharedPreferences("appLocale", Context.MODE_PRIVATE);
        if (sharedPreferences.contains("appLocale")) {
            appLocale = sharedPreferences.getString("appLocale", "");
        } else appLocale = "en";
        setLocale();

    }

    private void setLocale() {
        Locale locale = new Locale(appLocale);
        Locale.setDefault(locale);
        Configuration config = getResources().getConfiguration();
        config.setLocale(locale);
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());
    }

    private void saveAppLocale() {
        SharedPreferences sharedPreferences;
        sharedPreferences = getSharedPreferences("appLocale", Context.MODE_PRIVATE);
        sharedPreferences.edit().putString("appLocale", appLocale).apply();
        setLocale();
        recreate();
    }


    private void setNewPIN() {
        activityState = SET_NEW_PIN;
        pinFragment = PinFragment.getInstance(activityState, getOnPinEntered(activityState));
        fragmentManager.beginTransaction().replace(R.id.fragmentContainer, pinFragment, PIN_FRAGMENT_TAG).commit();
        toolbar.getMenu().findItem(R.id.action_set_new_pin).setVisible(false);
        toolbar.getMenu().findItem(R.id.action_reset_pin).setVisible(false);
    }

    private void checkPIN() {
        activityState = CHECK_PIN;
        pinFragment = PinFragment.getInstance(activityState, getOnPinEntered(activityState));
        fragmentManager.beginTransaction().replace(R.id.fragmentContainer, pinFragment, PIN_FRAGMENT_TAG).commit();
        toolbar.getMenu().findItem(R.id.action_set_new_pin).setVisible(false);
        toolbar.getMenu().findItem(R.id.action_reset_pin).setVisible(false);
    }

    private void resetPIN() {
        activityState = RESET_PIN;
        pinFragment = PinFragment.getInstance(activityState, getOnPinEntered(activityState));
        if (!pinFragment.hasPin()) {
            Toast.makeText(NotesMainActivity.this, getString(R.string.pin_not_exist),
                    Toast.LENGTH_SHORT).show();
        } else {
            toolbar.getMenu().findItem(R.id.action_set_new_pin).setVisible(false);
            toolbar.getMenu().findItem(R.id.action_reset_pin).setVisible(false);
            fragmentManager.beginTransaction().replace(R.id.fragmentContainer, pinFragment, PIN_FRAGMENT_TAG).commit();
        }
    }


    //Этот метод возвращает метод обратного вызова (колбэк), передаваемый фрагменту PinFragment
//в зависимости от состояния активити
//Используется как в ручном выборе режима работы (из меню), так и при пересоздании активити
//с фрагментом
    private PinFragment.OnPinEntered getOnPinEntered(int activityState) {
        switch (activityState) {
            case SET_NEW_PIN:
                return new PinFragment.OnPinEntered() {
                    @Override
                    public void onPinEntered() {
                        Toast.makeText(NotesMainActivity.this, getString(R.string.pin_set),
                                Toast.LENGTH_SHORT).show();

                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {
                                pinFragment.saveNew(pinFragment.getEnteredPin());
                                viewNotes();
                            }
                        }, 1300); //specify the number of milliseconds
                    }
                };
            case CHECK_PIN:
                return new PinFragment.OnPinEntered() {
                    @Override
                    public void onPinEntered() {
                        if (pinFragment.checkPin(pinFragment.getEnteredPin())) {
                            Toast.makeText(NotesMainActivity.this, getString(R.string.pin_confirmed),
                                    Toast.LENGTH_SHORT).show();

                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                public void run() {
                                    viewNotes();
                                }
                            }, 1300); //specify the number of milliseconds
                        } else {
                            Toast.makeText(NotesMainActivity.this, getString(R.string.pin_wrong),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                };
            case RESET_PIN:
                return new PinFragment.OnPinEntered() {
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
                            }, 1300); //specify the number of milliseconds
                        } else {
                            Toast.makeText(NotesMainActivity.this, getString(R.string.pin_wrong),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                };
            default:
                return null;
        }
    }

    private void viewNotes() {
        activityState = VIEW_NOTES;
        toolbar.getMenu().findItem(R.id.action_set_new_pin).setVisible(true);
        toolbar.getMenu().findItem(R.id.action_reset_pin).setVisible(true);
        fragmentManager.beginTransaction().replace(R.id.fragmentContainer, noteListFragment, NOTESLIST_FRAGMENT_TAG).commit();
    }

    @Override
    public void onBackPressed() {

        switch (activityState) {
            case SET_NEW_PIN:
            case RESET_PIN:
                viewNotes();
                break;
            //далее по кнопке BACK выход из приложения возможен только по двойному нажатию,
            // причем второе нажатие после первого должно быть не далее чем через 3 секунды
            default:
                timeout = System.currentTimeMillis() - timeout;
                if (timeout > 3000) backPressedCount = 0;
                backPressedCount++;
                if (backPressedCount == 1) {
                    Toast.makeText(NotesMainActivity.this, getString(R.string.backpress_more),
                            Toast.LENGTH_SHORT).show();
                    timeout = System.currentTimeMillis();  //засекаем отсчет времени после первого
                    // нажатия
                }
                if (backPressedCount == 2) super.onBackPressed();
                break;
        }
    }

}





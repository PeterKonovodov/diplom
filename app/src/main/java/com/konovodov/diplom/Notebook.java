package com.konovodov.diplom;

import android.app.Application;
import android.content.Context;
import android.util.Log;

public class Notebook extends Application {

    private static NoteRepository noteRepository;
    private static Keystore keystore;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("tag", "Notebook onCreate");

        noteRepository = new SQLiteNoteRepository(this);
        keystore = new SimpleKeystore(this);

        noteRepository.saveNote(new Note("dd", "aa", false));

        Context context = getApplicationContext();
        Context context1 = getApplicationContext();


    }

    @Override
    public void onTerminate() {
        Log.i("tag", "Notebook onTerminate"); //это не работает. видимо, ликвидируется раньше
        super.onTerminate();
    }


}

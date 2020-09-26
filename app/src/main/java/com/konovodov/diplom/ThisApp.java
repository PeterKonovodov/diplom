package com.konovodov.diplom;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ThisApp extends Application {

    private static ThisApp instance;
    private static ZoneOffset zoneOffset;

    private NoteRepository noteRepository;
    private Keystore keystore;
    private static List<Note> notes;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");


    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("tag", "ThisApp onCreate");
        noteRepository = new SQLiteNoteRepository(this);
        keystore = new SimpleKeystore(this);
        noteRepository.saveNote(new Note("dd", "aa", false, 0, 0));
        Context context = getApplicationContext();
        Context context1 = getApplicationContext();
        notes = new ArrayList<>();
        zoneOffset = ZoneOffset.systemDefault().getRules().getOffset(Instant.now());
    }

    public static List<Note> getNotes() {
        return notes;
    }


    @Override
    public void onTerminate() {
        Log.i("tag", "ThisApp onTerminate"); //это не работает. видимо, ликвидируется раньше
        super.onTerminate();
    }

    public static String getFormattedDate(LocalDateTime date) {
        return date.format(formatter);
    }
    public static String getFormattedDate(long date) {
        return getDateOfEpoch(date).format(formatter);
    }

    public static LocalDateTime getDateOfEpoch(long date) {
        return LocalDateTime.ofEpochSecond(date, 0, zoneOffset);
    }
    public static long getEpochDate(LocalDateTime date) {
        return date.toEpochSecond(zoneOffset);
    }


}

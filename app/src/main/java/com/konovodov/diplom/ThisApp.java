package com.konovodov.diplom;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.konovodov.diplom.comparators.DeadLineComparator;
import com.konovodov.diplom.comparators.HasDeadLineComparator;
import com.konovodov.diplom.comparators.IsDoneComparator;
import com.konovodov.diplom.comparators.ModifyDateComparator;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ThisApp extends Application {

    private static ThisApp instance;
    private static ZoneOffset zoneOffset;

    public static final int SECONDS_PER_DAY = 86400;




    private static NoteRepository noteRepository;
    private static PinStore pinStore;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");


    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("tag", "ThisApp onCreate");
        noteRepository = new SQLiteNoteRepository(this);
        pinStore = new SharedPrefPinStore(this);
        Context context = getApplicationContext();
        Context context1 = getApplicationContext();

        zoneOffset = ZoneOffset.systemDefault().getRules().getOffset(Instant.now());


/*
        int mt = 60;
        int dl = SECONDS_PER_DAY;
        noteRepository.getNotes().add(new Note(1, "1", "11", true, getEpochDateNowTruncDays() - 5*dl, getEpochDate(LocalDateTime.now()) - 1*mt, false));
        noteRepository.getNotes().add(new Note(2, "2", "22", true, getEpochDateNowTruncDays() - 4*dl, getEpochDate(LocalDateTime.now()) - 2*mt, false));
        noteRepository.getNotes().add(new Note(3, "3", "33", true, getEpochDateNowTruncDays() - 3*dl, getEpochDate(LocalDateTime.now()) - 3*mt, false));
        noteRepository.getNotes().add(new Note(4, "4", "44", true, getEpochDateNowTruncDays() - 2*dl, getEpochDate(LocalDateTime.now()) - 4*mt, false));
        noteRepository.getNotes().add(new Note(5, "5", "55", true, getEpochDateNowTruncDays() - 1*dl, getEpochDate(LocalDateTime.now()) - 5*mt, false));
        noteRepository.getNotes().add(new Note(6, "6", "66", true, getEpochDateNowTruncDays() - 0*dl, getEpochDate(LocalDateTime.now()) - 6*mt, false));
        noteRepository.getNotes().add(new Note(7, "7", "77", true, getEpochDateNowTruncDays() + 1*dl, getEpochDate(LocalDateTime.now()) - 7*mt, true));
        noteRepository.getNotes().add(new Note(8, "8", "88", true, getEpochDateNowTruncDays() + 2*dl, getEpochDate(LocalDateTime.now()) - 8*mt, false));
        noteRepository.getNotes().add(new Note(9, "9", "99", true, getEpochDateNowTruncDays() + 3*dl, getEpochDate(LocalDateTime.now()) - 9*mt, false));
        noteRepository.getNotes().add(new Note(10, "A", "AA", true, getEpochDateNowTruncDays() + 4*dl, getEpochDate(LocalDateTime.now()) - 10*mt, false));
*/



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


    public static long getEpochDateNowTruncDays() {
        return getEpochDate(LocalDateTime.now().truncatedTo(ChronoUnit.DAYS));
    }

    public static NoteRepository getNoteRepository() {
        return noteRepository;
    }

    public static PinStore getPinStore() {
        return pinStore;
    }
}

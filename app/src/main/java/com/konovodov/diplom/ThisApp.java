package com.konovodov.diplom;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.konovodov.diplom.comparators.DeadLineComparator;
import com.konovodov.diplom.comparators.HasDeadLineComparator;
import com.konovodov.diplom.comparators.ModifyDateComparator;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ThisApp extends Application {

    private static ThisApp instance;
    private static ZoneOffset zoneOffset;

    private NoteRepository noteRepository;
    private Keystore keystore;
    private static List<Note> noteList;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private static final HasDeadLineComparator hasDeadLineComparator = new HasDeadLineComparator();
    private static final DeadLineComparator deadLineComparator = new DeadLineComparator();
    private static final ModifyDateComparator modifyDateComparator = new ModifyDateComparator();


    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("tag", "ThisApp onCreate");
        noteRepository = new SQLiteNoteRepository(this);
        keystore = new SimpleKeystore(this);
        noteRepository.saveNote(new Note("dd", "aa", false, 0, 0));
        Context context = getApplicationContext();
        Context context1 = getApplicationContext();
        noteList = new ArrayList<>();

        zoneOffset = ZoneOffset.systemDefault().getRules().getOffset(Instant.now());
        int mt = 60;
        int dl = 86400;
        noteList.add(new Note("1", "11", true, getEpochDate(LocalDateTime.now()) - 5*dl, getEpochDate(LocalDateTime.now()) - 1*mt));
        noteList.add(new Note("2", "22", true, getEpochDate(LocalDateTime.now()) - 4*dl, getEpochDate(LocalDateTime.now()) - 2*mt));
        noteList.add(new Note("3", "33", true, getEpochDate(LocalDateTime.now()) - 3*dl, getEpochDate(LocalDateTime.now()) - 3*mt));
        noteList.add(new Note("4", "44", true, getEpochDate(LocalDateTime.now()) - 2*dl, getEpochDate(LocalDateTime.now()) - 4*mt));
        noteList.add(new Note("5", "55", true, getEpochDate(LocalDateTime.now()) - 1*dl, getEpochDate(LocalDateTime.now()) - 5*mt));
        noteList.add(new Note("6", "66", false, getEpochDate(LocalDateTime.now()) - 0*dl, getEpochDate(LocalDateTime.now()) - 6*mt));
        noteList.add(new Note("7", "77", true, getEpochDate(LocalDateTime.now()) + 1*dl, getEpochDate(LocalDateTime.now()) - 7*mt));
        noteList.add(new Note("8", "88", true, getEpochDate(LocalDateTime.now()) + 2*dl, getEpochDate(LocalDateTime.now()) - 8*mt));
        noteList.add(new Note("9", "99", true, getEpochDate(LocalDateTime.now()) + 3*dl, getEpochDate(LocalDateTime.now()) - 9*mt));
        noteList.add(new Note("A", "AA", true, getEpochDate(LocalDateTime.now()) + 4*dl, getEpochDate(LocalDateTime.now()) - 10*mt));
    }

    public static List<Note> getNoteList() {
        return noteList;
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

    public static void sortNotes() {
        Collections.sort(noteList, hasDeadLineComparator.thenComparing(deadLineComparator).thenComparing(modifyDateComparator));
//        Collections.sort(noteList, hasDeadLineComparator);
//        Collections.sort(noteList, deadLineComparator);
//        Collections.sort(noteList, modifyDateComparator);
    }


}

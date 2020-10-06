package com.konovodov.diplom;

import android.app.Application;
import android.content.Context;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class ThisApp extends Application {

    private static ZoneOffset zoneOffset;
    public static final int SECONDS_PER_DAY = 86400;
    private static boolean coldAppStart = false;
    private static NoteRepository noteRepository;
    private static PinStore pinStore;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");


    @Override
    public void onCreate() {
        super.onCreate();
        coldAppStart = true;
        noteRepository = new SQLiteNoteRepository(this);
        pinStore = new SharedPrefPinStore(this);
        zoneOffset = ZoneOffset.systemDefault().getRules().getOffset(Instant.now());

    }


    public static String getFormattedDate(LocalDateTime date) {
        return date.format(formatter);
    }

/*
    public static String getFormattedDate(long date) {
        return getDateOfEpoch(date).format(formatter);
    }
*/

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

    public static boolean isColdAppStart() {
        return coldAppStart;
    }

    public static void setColdAppStart(boolean coldAppStart) {
        ThisApp.coldAppStart = coldAppStart;
    }


}

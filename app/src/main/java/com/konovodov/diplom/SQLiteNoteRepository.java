package com.konovodov.diplom;

import android.app.Activity;
import android.app.Application;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


import java.util.List;

public class SQLiteNoteRepository implements NoteRepository {

    final String LOG_TAG = "SQLiteRep";
    Context context;
    DBHelper dbHelper;

    // создаем объект для данных
    ContentValues cv = new ContentValues();


    public SQLiteNoteRepository(Context context) {
        this.context = context;

        // создаем объект для создания и управления версиями БД
        dbHelper = new DBHelper(context);



    }

    class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context) {
            // конструктор суперкласса
            super(context, "notesDB", null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.d(LOG_TAG, "--- onCreate database ---");
            // создаем таблицу с полями
            db.execSQL("create table mytable ("
                    + "id integer primary key autoincrement,"
                    + "name text,"
                    + "email text" + ");");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }


    @Override
    public Note getNoteById(String id) {
        return null;
    }

    @Override
    public List<Note> getNotes() {
        return null;
    }

    @Override
    public void saveNote(Note note) {
        // получаем данные из полей ввода
        String name = "nname";
        String email = "eemail";

        // подключаемся к БД
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Log.d(LOG_TAG, "--- Insert in mytable: ---");
        // подготовим данные для вставки в виде пар: наименование столбца - значение

        cv.put("name", name);
        cv.put("email", email);
        // вставляем запись и получаем ее ID
        long rowID = db.insert("mytable", null, cv);
        Log.d(LOG_TAG, "row inserted, ID = " + rowID);

        // закрываем подключение к БД
        dbHelper.close();

    }

    @Override
    public void deleteById(String id) {

    }
}

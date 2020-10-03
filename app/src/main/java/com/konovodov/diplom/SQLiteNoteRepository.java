package com.konovodov.diplom;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import com.konovodov.diplom.comparators.DeadLineComparator;
import com.konovodov.diplom.comparators.HasDeadLineComparator;
import com.konovodov.diplom.comparators.IsCompletedComparator;
import com.konovodov.diplom.comparators.ModifyDateComparator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SQLiteNoteRepository implements NoteRepository {

    private final String LOG_TAG = "SQLiteRep";
    private final String TABLE_NAME = "Notes_Table";
    private final DBHelper dbHelper;
    private final List<Note> noteList;

    private static final HasDeadLineComparator hasDeadLineComparator = new HasDeadLineComparator();
    private static final DeadLineComparator deadLineComparator = new DeadLineComparator();
    private static final ModifyDateComparator modifyDateComparator = new ModifyDateComparator();
    private static final IsCompletedComparator IS_COMPLETED_COMPARATOR = new IsCompletedComparator();


    // создаем объект для данных
    final ContentValues cv = new ContentValues();


    public SQLiteNoteRepository(Context context) {
        // создаем объект для создания и управления версиями БД
        dbHelper = new DBHelper(context);
        noteList = new ArrayList<>();
        loadNotes();
    }

    class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context) {
            // конструктор суперкласса
            super(context, "notesDB", null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            // создаем таблицу с полями
            db.execSQL("create table " + TABLE_NAME + " ("
                    + "id integer primary key autoincrement,"
                    + "header text,"
                    + "body text,"
                    + "hasDeadLine integer,"
                    + "epochDeadLineDate integer,"
                    + "epochModifyDate integer,"
                    + "isCompleted integer" + ");");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }


    @Override
    public List<Note> getNotes() {
        return noteList;
    }

    private void loadNotes() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // делаем запрос всех данных из таблицы mytable, получаем Cursor
        Cursor c = db.query(TABLE_NAME, null, null, null, null, null, null);

        // ставим позицию курсора на первую строку выборки
        // если в выборке нет строк, вернется false
        if (c.moveToFirst()) {

            // определяем номера столбцов по имени в выборке
            int idColIndex = c.getColumnIndex("id");
            int headerColIndex = c.getColumnIndex("header");
            int bodyColIndex = c.getColumnIndex("body");
            int hasDeadLineIndex = c.getColumnIndex("hasDeadLine");
            int epochDeadLineDateIndex = c.getColumnIndex("epochDeadLineDate");
            int epochModifyDateIndex = c.getColumnIndex("epochModifyDate");
            int isCompletedIndex = c.getColumnIndex("isCompleted");

            do {
                // получаем значения по номерам столбцов и пишем все в лог
                Note note = new Note(c.getInt(idColIndex), c.getString(headerColIndex),
                        c.getString(bodyColIndex), intToBoolean(c.getInt(hasDeadLineIndex)),
                        c.getInt(epochDeadLineDateIndex), c.getInt(epochModifyDateIndex),
                        intToBoolean(c.getInt(isCompletedIndex)));

                noteList.add(note);
                // переход на следующую строку
                // а если следующей нет (текущая - последняя), то false - выходим из цикла
            } while (c.moveToNext());
        }
        c.close();
        dbHelper.close();
    }


    @Override
    public void saveNote(Note note) {
        // подключаемся к БД
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // подготовим данные для вставки в виде пар: наименование столбца - значение
        cv.put("header", note.getHeaderText());
        cv.put("body", note.getBodyText());
        cv.put("hasDeadLine", booleanToInt(note.hasDeadLine()));
        cv.put("epochDeadLineDate", note.getEpochDeadLineDate());
        cv.put("epochModifyDate", note.getEpochModifyDate());
        cv.put("isCompleted", booleanToInt(note.isCompleted()));

        if (note.getId() == 0) {     //если id=0, то это новая запись
            // вставляем запись и получаем ее ID
            note.setId(db.insert(TABLE_NAME, null, cv));
        } else {
            // если id != 0, то обновляем по id
            int updCount = db.update(TABLE_NAME, cv, "id = ?",
                    new String[]{"" + note.getId()});
        }
        dbHelper.close();
    }


    @Override
    public void deleteById(long id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int delCount = db.delete(TABLE_NAME, "id = " + id, null);
        dbHelper.close();
    }


    public void sortNotes() {
        Collections.sort(getNotes(), IS_COMPLETED_COMPARATOR.thenComparing(hasDeadLineComparator)
                .thenComparing(deadLineComparator).thenComparing(modifyDateComparator));
    }


    private int booleanToInt(boolean in) {
        if (in) return 1;
        return 0;
    }

    private boolean intToBoolean(int in) {
        return in != 0;
    }
}

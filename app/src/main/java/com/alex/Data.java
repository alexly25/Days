package com.alex;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by alex on 13.02.14.
 */
public class Data implements DataInterface {

    private static final String LOG = "myLogData";

    SQLiteDatabase db = null;
    Cursor c = null;

    private SQLite sqLite;

    public Data(SQLite sqLite) {
        Log.d(LOG, "Data()");
        this.sqLite = sqLite;
    }


    public boolean addNote(Note note) {
        Log.d(LOG, "addNote()");

        boolean result = true;

        try {

            // подключаемся к БД
            db = sqLite.getWritableDatabase();

            int idTag;
            if ((idTag = getIdTag(note.getTag())) == -1) {
                return false;
            }

            addNote(note.getNote());

            int idNote = getIdNote(note.getNote());

            if (idNote == -1) {
                return false;
            }

            if (isExist(idTag, idNote)) {
                return false;
            }

            // создаем объект для данных
            ContentValues cv = new ContentValues();

            cv.put(SqlVariables.COLUMN_TAG_ID, idTag);
            cv.put(SqlVariables.COLUMN_NOTE_ID, idNote);

            if (db.insert(SqlVariables.TABLE_TAG_NOTE, null, cv) == -1) {
                result = false;
                Log.d(LOG, "addNote(): Do not added '" + note.toString() + "' into table '" + SqlVariables.TABLE_TAG_NOTE + "'");
            }

        } catch (Exception e) {

            Log.d(LOG, "!!!!!addNote() catch error: " + e.toString());
            return false;

        } finally {
            db.close();
        }

        return result;
    }

    private boolean isExist(int idTag, int idNote) {
        Log.d(LOG, "isExist()");

        c = db.rawQuery("Select * from " + SqlVariables.TABLE_TAG_NOTE +
                " where " + SqlVariables.COLUMN_TAG_ID + " = " + idTag +
                " and " + SqlVariables.COLUMN_NOTE_ID + " = " + idNote, null);

        int count = c.getCount();

        c.close();

        Log.d(LOG, "count: " + count);

        if (count > 0) {
            return true;
        }

        return false;
    }

    private int getIdNote(String note) {
        Log.d(LOG, "getIdNote():");

        db = sqLite.getWritableDatabase(); // подключаемся к БД

        c = db.query(SqlVariables.TABLE_NOTES, new String[]{SqlVariables.COLUMN_NOTE_ID}, SqlVariables.COLUMN_NOTE + " = ?", new String[]{note}, null,null,null);// ("SELECT * FROM " + SqlVariables.TABLE_NOTES + " WHERE " + SqlVariables.COLUMN_NOTE + " = '" + note + "'", null);

        if (c.moveToFirst()) {

            return c.getInt(0);

        } else {
            Log.d(LOG, "getIdNote() 0 rows");
        }

        c.close();
        db.close();

        return -1;
    }

    private int getIdTag(String tag) {
        Log.d(LOG, "getIdTag()");

        c = db.query(SqlVariables.TABLE_TAGS, new String[]{SqlVariables.COLUMN_TAG_ID}, SqlVariables.COLUMN_TAG + " = ?", new String[]{tag}, null, null, null);

        if (c.moveToFirst()) {

            return c.getInt(c.getColumnIndex(SqlVariables.COLUMN_TAG_ID));

        } else {
            Log.d(LOG, "getIdTag() 0 rows");
        }

        c.close();

        return -1;
    }

    private void addNote(String note) {
        Log.d(LOG, "addNote():");

        // создаем объект для данных
        ContentValues cv = new ContentValues();

        // подключаемся к БД
        db = sqLite.getWritableDatabase();

        cv.put(SqlVariables.COLUMN_NOTE, note);

        if (db.insert(SqlVariables.TABLE_NOTES, null, cv) == -1) {

            Log.d(LOG, "addNote(): Do not added '" + note + "' into table '" + SqlVariables.TABLE_NOTES + "'");
        } else {

            Log.d(LOG, "addNote(): added '" + note + "' into table '" + SqlVariables.TABLE_NOTES + "'");
        }

        db.close();
    }

    public ArrayList<String> getColumn(String tableName, String columnName) {
        Log.d(LOG, "getColumn()");

        ArrayList<String> arrayList = new ArrayList<String>();

        try {

            db = sqLite.getWritableDatabase(); // подключаемся к БД

            c = db.query(tableName, new String[]{columnName}, null, null, null, null, null); // делаем запрос всех данных из таблицы tableName

            if (c.moveToFirst()) {

                do {

                    arrayList.add(c.getString(0));

                    // переход на следующую строку
                    // а если следующей нет (текущая - последняя), то false - выходим из цикла
                } while (c.moveToNext());

            } else {
                Log.d(LOG, "getColumn() 0 rows");
            }

        } catch (Exception e) {

            Log.d(LOG, "!!!!!getColumn() catch error: " + e.toString());
            return null;

        } finally {
            c.close();
            db.close();
        }
        return arrayList;
    }

    /**
     * Метод возвращает все записи равные tag.
     *
     * @param tag
     * @return
     */
    public ArrayList<String> getNotes(String tag) {
        Log.d(LOG, "getNotes()");

        ArrayList<String> alNote = new ArrayList<String>();

        try {

            db = sqLite.getWritableDatabase(); // подключаемся к БД

            int idTag = getIdTag(tag);

            c = db.rawQuery("select * from " + SqlVariables.TABLE_NOTES + ", " + SqlVariables.TABLE_TAG_NOTE +
                    " where " + SqlVariables.TABLE_TAG_NOTE + "." + SqlVariables.COLUMN_TAG_ID + " = " + idTag +
                    " and " + SqlVariables.TABLE_TAG_NOTE + "." + SqlVariables.COLUMN_NOTE_ID + " = " + SqlVariables.TABLE_NOTES + "." + SqlVariables.COLUMN_NOTE_ID
                    , null);

            if (c.moveToFirst()) {

                do {

                    alNote.add(c.getString(c.getColumnIndex(SqlVariables.COLUMN_NOTE)));

                    // переход на следующую строку
                    // а если следующей нет (текущая - последняя), то false - выходим из цикла
                } while (c.moveToNext());

            } else {
                Log.d(LOG, "getNotes() 0 rows");
            }

        } catch (Exception e) {

            Log.d(LOG, "!!!!!getNotes() catch error: " + e.toString());
            return null;

        } finally {
            c.close();
            db.close();
        }
        return alNote;
    }

    /**
     * Метод добовляет тег в базу данных.
     *
     * @param tag - имя тега.
     * @return true - если добаленно успешно, false - если тег не получилось добавить.
     */
    public boolean addTag(String tag) {
        Log.d(LOG, "addTag(): " + tag);

        boolean result = true;

        try {

            // создаем объект для данных
            ContentValues cv = new ContentValues();

            // подключаемся к БД
            db = sqLite.getWritableDatabase();

            cv.put(SqlVariables.COLUMN_TAG, tag);
            if (db.insert(SqlVariables.TABLE_TAGS, null, cv) == -1) {
                result = false;
                Log.d(LOG, "addTag(): Do not added '" + tag + "' into table '" + SqlVariables.TABLE_TAGS + "'");
            }

        } catch (Exception e) {

            Log.d(LOG, "!!!!!addTag() catch error: " + e.toString());
            return false;

        } finally {
            db.close();
        }

        return result;

    }

    public int getCount(String nameTable) {
        Log.d(LOG, "getCount()");

        int count = -1;

        try {

            db = sqLite.getWritableDatabase(); // подключаемся к БД

            c = db.query(nameTable, null, null, null, null, null, null); // делаем запрос всех данных из таблицы tableName
            count = c.getCount();

        } catch (Exception e) {

            Log.d(LOG, "!!!!!getCount() catch error: " + e.toString());
            return -1;

        } finally {
            c.close();
            db.close();
        }
        return count;
    }

    /**
     * Метод удаляет запись note из базы данных.
     *
     * @param note Запись.
     * @return true - если удаление прошло успешно, false - если запись не получилось удалить.
     */
    public boolean deleteNote(String note) {
        Log.d(LOG, "deleteNote()");

        try {

            // подключаемся к БД
            db = sqLite.getWritableDatabase();

            int idNote = getIdNote(note);
            db.delete(SqlVariables.TABLE_NOTES, SqlVariables.COLUMN_NOTE + " = ?", new String[]{note});
            db.delete(SqlVariables.TABLE_TAG_NOTE, SqlVariables.COLUMN_NOTE_ID + " = ?", new String[]{String.valueOf(idNote)});

        } catch (Exception e) {

            Log.d(LOG, "!!!!!deleteNote() catch error: " + e.toString());
            return false;

        } finally {
            db.close();
        }

        return true;
    }

    public boolean delete(String tableName, String columnName, String value) {
        Log.d(LOG, "deleteNote()");

        boolean result = false;
        try {

            // подключаемся к БД
            db = sqLite.getWritableDatabase();

            result = (db.delete(tableName, columnName + " = ?", new String[]{value}) > 0);


        } catch (Exception e) {

            Log.d(LOG, "!!!!!deleteNote() catch error: " + e.toString());

        } finally {
            db.close();
        }

        return result;
    }

    /**
     * Метод удаляет тег из базы данных.
     *
     * @param tag имя тега.
     * @return true - если удаление прошло успешно, false - если запись не получилось удалить.
     */
    public boolean deleteTag(String tag) {
        Log.d(LOG, "deleteTag()");

        boolean result = false;
        try {

            // подключаемся к БД
            db = sqLite.getWritableDatabase();

            result = (db.delete(SqlVariables.TABLE_TAGS, SqlVariables.COLUMN_TAG + " = ?", new String[]{tag}) > 0);

        } catch (Exception e) {

            Log.d(LOG, "!!!!!deleteNote() catch error: " + e.toString());
            return false;

        } finally {
            db.close();
        }

        return result;
    }

    public boolean deleteNotes(String tag) {
        Log.d(LOG, "deleteNotes");

        ArrayList<String> alNotes = getNotes(tag);

        if (alNotes != null) {
            for (String note : alNotes) {

                if (!deleteNote(note)) {
                    //Toast.makeText(db., "Do not deleted note '" + note + "'", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
        }
        return true;
    }

    public void cleanTagNote(){


    }

    public void outTables() {

        Log.d(LOG, "outTables() ------------" + SqlVariables.TABLE_TAGS + "---------------");
        ArrayList<String> arrayList = getColumn(SqlVariables.TABLE_TAGS, SqlVariables.COLUMN_TAG_ID);
        ArrayList<String> arrayList1 = getColumn(SqlVariables.TABLE_TAGS, SqlVariables.COLUMN_TAG);
        for (int i = 0; i < arrayList.size(); i++) {
            Log.d(LOG, "outTables() " + arrayList.get(i) + "|" + arrayList1.get(i));
        }

        Log.d(LOG, "outTables() ------------" + SqlVariables.TABLE_NOTES + "---------------");
        arrayList = getColumn(SqlVariables.TABLE_NOTES, SqlVariables.COLUMN_NOTE_ID);
        arrayList1 = getColumn(SqlVariables.TABLE_NOTES, SqlVariables.COLUMN_NOTE);
        for (int i = 0; i < arrayList.size(); i++) {
            Log.d(LOG, "outTables() " + arrayList.get(i) + "|" + arrayList1.get(i));
        }

        Log.d(LOG, "outTables() ------------" + SqlVariables.TABLE_TAG_NOTE + "---------------");
        ArrayList<String> arrayList2 = getColumn(SqlVariables.TABLE_TAG_NOTE, "id");
        arrayList = getColumn(SqlVariables.TABLE_TAG_NOTE, SqlVariables.COLUMN_TAG_ID);
        arrayList1 = getColumn(SqlVariables.TABLE_TAG_NOTE, SqlVariables.COLUMN_NOTE_ID);
        for (int i = 0; i < arrayList.size(); i++) {
            Log.d(LOG, "outTables() " + arrayList2.get(i) + "|" + arrayList.get(i) + "|" + arrayList1.get(i));
        }
    }

}

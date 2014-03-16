package com.alex;

import java.util.ArrayList;

/**
 * Created by alex on 16.03.14.
 */
public interface DataInterface {
    public boolean addTag(String tag);
    public boolean addNote(Note note);
    public boolean deleteTag(String tag);
    public boolean deleteNote(String note);
    public ArrayList<String> getColumn(String tableName, String columnName);
    public ArrayList<String> getNotes(String tag);
    public int getCount(String nameTable);
    public boolean delete(String tableName, String columnName, String value);
    public boolean deleteNotes(String tag);
}

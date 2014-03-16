package com.alex;

/**
 * Created by alex on 13.02.14.
 */
public class Note {
    private String tag;
    private String note;

    public Note(String tag, String note) {
        this.tag = tag;
        this.note = note;
    }

    public String getTag() {
        return tag;
    }

    public String getNote() {
        return note;
    }

    public String toString() {
        return tag + ": " + note;
    }
}

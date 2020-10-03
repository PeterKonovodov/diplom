package com.konovodov.diplom;

import java.util.List;

public interface NoteRepository {
    List<Note> getNotes();
    void saveNote(Note note);
    void deleteById(long id);
    void sortNotes();
}
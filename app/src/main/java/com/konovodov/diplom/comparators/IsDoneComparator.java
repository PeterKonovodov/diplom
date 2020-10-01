package com.konovodov.diplom.comparators;

import com.konovodov.diplom.Note;

import java.util.Comparator;

public class IsDoneComparator implements Comparator<Note> {
    @Override
    public int compare(Note n1, Note n2) {
        if(n1.isCompleted() && !n2.isCompleted()) return 1;
        if(!n1.isCompleted() && n2.isCompleted()) return -1;
        return 0;
    }

}






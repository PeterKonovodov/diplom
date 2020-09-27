package com.konovodov.diplom.comparators;

import com.konovodov.diplom.Note;

import java.util.Comparator;

public class HasDeadLineComparator implements Comparator<Note> {
    @Override
    public int compare(Note n1, Note n2) {
        if(n2.hasDeadLine() && !n1.hasDeadLine()) return 1;
        if(!n2.hasDeadLine() && n1.hasDeadLine()) return -1;
        return 0;
    }
}

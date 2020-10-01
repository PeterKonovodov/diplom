package com.konovodov.diplom.comparators;

import com.konovodov.diplom.Note;

import java.util.Comparator;

public class HasDeadLineComparator implements Comparator<Note> {
    @Override
    public int compare(Note n1, Note n2) {
        if(n1.isCompleted() && n2.isCompleted()) return 0; //это для того, чтобы выполненные задачи
        //сортировались только по времени модификации
        if(n2.hasDeadLine() && !n1.hasDeadLine()) return 1;
        if(!n2.hasDeadLine() && n1.hasDeadLine()) return -1;
        return 0;
    }
}

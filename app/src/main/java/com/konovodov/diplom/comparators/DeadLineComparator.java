package com.konovodov.diplom.comparators;

import com.konovodov.diplom.Note;

import java.util.Comparator;

public class DeadLineComparator implements Comparator<Note> {
    @Override
    public int compare(Note n1, Note n2) {
        if(!n1.hasDeadLine() && !n2.hasDeadLine()) return 0; //это для того, чтобы задачи
        // без дедлайна не сортировались по скрытому полю дедлайна

        if(n1.isCompleted() && n2.isCompleted()) return 0; //это для того, чтобы выполненные задачи
        //сортировались только по времени модификации

        return Long.compare((n2.getEpochDeadLineDate()), (n1.getEpochDeadLineDate()));
    }
}

package com.konovodov.diplom.comparators;

import com.konovodov.diplom.Note;

import java.util.Comparator;

public class DeadLineComparator implements Comparator<Note> {
    @Override
    public int compare(Note n1, Note n2) {
        return Long.compare((n2.getEpochDeadLineDate()), (n1.getEpochDeadLineDate()));
    }
}

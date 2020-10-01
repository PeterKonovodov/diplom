package com.konovodov.diplom;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.List;

public class NoteEditor implements NoteRepository {

    private Context context;
    private boolean thisIsNewNote;
    private List<Note> noteList;
    private Note note;
    private Long EpochDeadLineDate;
    private LocalDateTime deadLineDate;

    public NoteEditor(Context context) {
        this.context = context;
    }

    public void editNote(NotesAdapter notesAdapter, int position) {
        thisIsNewNote = false;
//        noteList = notesAdapter.getNoteList();
        noteList = getNotes();
        if (position >= noteList.size()) {
            thisIsNewNote = true;
            note = new Note(0, "", "", false, ThisApp.getEpochDateNowTruncDays(), 0, false);
        } else note = noteList.get(position);


        LayoutInflater li = LayoutInflater.from(context);
        View noteEditDialogView = li.inflate(R.layout.note_edit, null);
        AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(context);
        mDialogBuilder.setView(noteEditDialogView);
        EditText headerEdit = noteEditDialogView.findViewById(R.id.headerEdit);
        EditText bodyEdit = noteEditDialogView.findViewById(R.id.bodyEdit);
        CheckBox hasDeadLineEdit = noteEditDialogView.findViewById(R.id.hasdeadlineEdit);
        CheckBox isDoneEdit = noteEditDialogView.findViewById(R.id.isDoneEdit);
        DatePicker deadlinePicker = noteEditDialogView.findViewById(R.id.deadLinePicker);

        headerEdit.setText(note.getHeaderText());
        bodyEdit.setText(note.getBodyText());
        EpochDeadLineDate = note.getEpochDeadLineDate();
        deadLineDate = ThisApp.getDateOfEpoch(EpochDeadLineDate);
        isDoneEdit.setChecked(note.isCompleted());


        deadlinePicker.init(deadLineDate.getYear(), deadLineDate.getMonthValue() - 1, deadLineDate.getDayOfMonth(), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                try {
                    deadLineDate = deadLineDate.withYear(year).withMonth(monthOfYear + 1).withDayOfMonth(dayOfMonth);
                    EpochDeadLineDate = ThisApp.getEpochDate(deadLineDate);
                    note.setCompleted(false);    //при манипуляциях с датой флаг выполненности сбрасывается
                    isDoneEdit.setChecked(false);
                } catch (DateTimeException e) {
                }
            }
        });

        if (note.hasDeadLine()) {
            hasDeadLineEdit.setChecked(true);
            deadlinePicker.setVisibility(View.VISIBLE);
        } else {
            hasDeadLineEdit.setChecked(false);
            deadlinePicker.setVisibility(View.GONE);
        }


        isDoneEdit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                note.setCompleted(isChecked);
            }
        });


        hasDeadLineEdit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    deadlinePicker.setVisibility(View.VISIBLE);
                } else
                    deadlinePicker.setVisibility(View.GONE);
            }
        });

        mDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                if (headerEdit.getText().length() == 0 && bodyEdit.getText().length() == 0) {
                                    if (!thisIsNewNote) noteList.remove(position);
                                } else {
                                    if (thisIsNewNote) noteList.add(note);
                                    note.setHeaderText(headerEdit.getText().toString());
                                    note.setBodyText(bodyEdit.getText().toString());
                                    note.setHasDeadLine(hasDeadLineEdit.isChecked());
                                    note.setEpochDeadLineDate(EpochDeadLineDate);
                                    note.setEpochModifyDate(ThisApp.getEpochDate(LocalDateTime.now())); //не обрезано, т.е. с точностью до секунд
                                    saveNote(note);
                                }
                                notesAdapter.notifyDataSetChanged();
                            }
                        })
                .setNegativeButton("Отмена",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                notesAdapter.notifyDataSetChanged();
                                dialog.cancel();
                            }
                        });
        if (!thisIsNewNote) mDialogBuilder.setNeutralButton("Удалить запись",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        deleteById(note.getId());
                        noteList.remove(position);
                        notesAdapter.notifyDataSetChanged();
                        dialog.cancel();

                    }
                });


        AlertDialog alertDialog = mDialogBuilder.create();
        alertDialog.show();

    }


    @Override
    public Note getNoteById(long id) {
        return ThisApp.getNoteRepository().getNoteById(id);
    }

    @Override
    public List<Note> getNotes() {
        return ThisApp.getNoteRepository().getNotes();
    }

    @Override
    public void saveNote(Note note) {
        ThisApp.getNoteRepository().saveNote(note);
    }

    @Override
    public void deleteById(long id) {
        ThisApp.getNoteRepository().deleteById(id);
    }

    @Override
    public void sortNotes() {
        ThisApp.getNoteRepository().sortNotes();
    }
}

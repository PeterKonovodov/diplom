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

    private final Context context;
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
        noteList = getNotes();
        if (position >= noteList.size()) {
            thisIsNewNote = true;
            //id = 0 - признак новой записи при ее сохранении
            note = new Note(0, "", "", false,
                    ThisApp.getEpochDateNowTruncDays(), 0, false);
        } else note = noteList.get(position);


        LayoutInflater li = LayoutInflater.from(context);
        View noteEditDialogView = li.inflate(R.layout.note_edit, null);
        AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(context);
        mDialogBuilder.setView(noteEditDialogView);
        EditText headerEdit = noteEditDialogView.findViewById(R.id.headerEdit);
        EditText bodyEdit = noteEditDialogView.findViewById(R.id.bodyEdit);
        CheckBox hasDeadLineEdit = noteEditDialogView.findViewById(R.id.hasdeadlineEdit);
        CheckBox isCompletedEdit = noteEditDialogView.findViewById(R.id.isCompletedEdit);
        DatePicker deadlinePicker = noteEditDialogView.findViewById(R.id.deadLinePicker);

        headerEdit.setText(note.getHeaderText());
        bodyEdit.setText(note.getBodyText());
        EpochDeadLineDate = note.getEpochDeadLineDate();
        deadLineDate = ThisApp.getDateOfEpoch(EpochDeadLineDate);
        isCompletedEdit.setChecked(note.isCompleted());
        if (note.getId() == 0)
            isCompletedEdit.setVisibility(View.INVISIBLE);
        else isCompletedEdit.setVisibility(View.VISIBLE);


        deadlinePicker.init(deadLineDate.getYear(), deadLineDate.getMonthValue() - 1,
                deadLineDate.getDayOfMonth(), new DatePicker.OnDateChangedListener() {
                    @Override
                    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        try {
                            deadLineDate = deadLineDate.withYear(year).withMonth(monthOfYear + 1)
                                    .withDayOfMonth(dayOfMonth);
                            EpochDeadLineDate = ThisApp.getEpochDate(deadLineDate);
                            note.setCompleted(false);    //при манипуляциях с датой флаг
                            // выполненности сбрасывается
                            isCompletedEdit.setChecked(false);
                        } catch (DateTimeException e) {
                            e.printStackTrace();
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


        isCompletedEdit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

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
                .setPositiveButton(context.getString(R.string.ok_string),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                if (headerEdit.getText().length() == 0 && bodyEdit.getText()
                                        .length() == 0) {
                                    if (!thisIsNewNote) noteList.remove(position);
                                } else {
                                    if (thisIsNewNote) noteList.add(note);
                                    note.setHeaderText(headerEdit.getText().toString());
                                    note.setBodyText(bodyEdit.getText().toString());
                                    note.setHasDeadLine(hasDeadLineEdit.isChecked());
                                    note.setEpochDeadLineDate(EpochDeadLineDate);
                                    note.setEpochModifyDate(ThisApp.getEpochDate(LocalDateTime
                                            .now())); //не обрезано, т.е. с точностью до секунд
                                    saveNote(note);
                                }
                                notesAdapter.notifyDataSetChanged();
                            }
                        })
                .setNegativeButton(context.getString(R.string.cancel_string),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                notesAdapter.notifyDataSetChanged();
                                dialog.cancel();
                            }
                        });
        if (!thisIsNewNote) mDialogBuilder.setNeutralButton(context.getString(R.string.delete_note),
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

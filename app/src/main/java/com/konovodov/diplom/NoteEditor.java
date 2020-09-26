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

import java.time.LocalDateTime;
import java.util.List;

public class NoteEditor {

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
        noteList = notesAdapter.getNoteList();
        if(position >= noteList.size()) {
            thisIsNewNote = true;
            note = new Note("", "", false, ThisApp.getEpochDate(LocalDateTime.now()), 0);
        }
        else note = noteList.get(position);


        LayoutInflater li = LayoutInflater.from(context);
        View noteEditDialogView = li.inflate(R.layout.note_edit, null);
        AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(context);
        mDialogBuilder.setView(noteEditDialogView);
        EditText headerEdit = noteEditDialogView.findViewById(R.id.headerEdit);
        EditText bodyEdit = noteEditDialogView.findViewById(R.id.bodyEdit);
        CheckBox hasDeadLineEdit = noteEditDialogView.findViewById(R.id.hasdeadlineEdit);
        DatePicker deadlinePicker = noteEditDialogView.findViewById(R.id.deadLinePicker);

        headerEdit.setText(note.getHeaderText());
        bodyEdit.setText(note.getBodyText());
        EpochDeadLineDate = note.getEpochDeadLineDate();
        deadLineDate = ThisApp.getDateOfEpoch(EpochDeadLineDate);

        deadlinePicker.init(deadLineDate.getYear(), deadLineDate.getMonthValue(), deadLineDate.getDayOfMonth(), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                deadLineDate = deadLineDate.withYear(year).withMonth(monthOfYear).withDayOfMonth(dayOfMonth);
                EpochDeadLineDate = ThisApp.getEpochDate(deadLineDate);
            }
        });

        if(note.isHasDeadLine()) {
            hasDeadLineEdit.setChecked(true);
            deadlinePicker.setVisibility(View.VISIBLE);
        }
        else {
            hasDeadLineEdit.setChecked(false);
            deadlinePicker.setVisibility(View.GONE);
        }


        hasDeadLineEdit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    deadlinePicker.setVisibility(View.VISIBLE);
                }
                else
                    deadlinePicker.setVisibility(View.GONE);
            }
        });

        mDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                if(headerEdit.getText().length() == 0 && bodyEdit.getText().length() == 0)
                                {
                                    if(!thisIsNewNote) noteList.remove(position);
                                }
                                else {
                                    if(thisIsNewNote) noteList.add(note);
                                    note.setHeaderText(headerEdit.getText().toString());
                                    note.setBodyText(bodyEdit.getText().toString());
                                    note.setHasDeadLine(hasDeadLineEdit.isChecked());
                                    note.setEpochDeadLineDate(EpochDeadLineDate);
                                    note.setEpochModifyDate(ThisApp.getEpochDate(LocalDateTime.now()));
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
        if(!thisIsNewNote) mDialogBuilder.setNeutralButton("Удалить запись",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                noteList.remove(position);
                                notesAdapter.notifyDataSetChanged();
                                dialog.cancel();
                            }
                        });


        AlertDialog alertDialog = mDialogBuilder.create();
        alertDialog.show();

    }


}

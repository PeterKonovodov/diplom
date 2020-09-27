package com.konovodov.diplom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import java.time.*;
import java.util.List;

public class NotesAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private List<Note> noteList;


    public NotesAdapter(Context context, List<Note> noteList) {
        this.context = context;
        this.noteList = noteList;
/*
        if (this.notes == null) {
            this.notes = new ArrayList<>();
        }
*/
        ThisApp.sortNotes();
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void addNote(Note note) {
        this.noteList.add(note);
        notifyDataSetChanged();
    }

    public void removeNote(int position) {
        noteList.remove(position);
        notifyDataSetChanged();
    }

    public List<Note> getNoteList() {
        return noteList;
    }

    @Override
    public int getCount() {
        return noteList.size();
    }

    @Override
    public Note getItem(int position) {
        if (position < noteList.size()) {
            return noteList.get(position);
        } else {
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = inflater.inflate(R.layout.note_view, parent, false);
        }

        Note note = noteList.get(position);

        TextView headerText = view.findViewById(R.id.header);
        TextView bodyText = view.findViewById(R.id.body);
        TextView deadlineText = view.findViewById(R.id.deadline);
        TextView modifyDateText = view.findViewById(R.id.modifyDate);
        CardView card = view.findViewById(R.id.card);

        headerText.setText(note.getHeaderText());
        bodyText.setText(note.getBodyText());

        int cardColor = R.color.colorCard;
        if (note.hasDeadLine()) {
            deadlineText.setVisibility(View.VISIBLE);
            LocalDateTime date = ThisApp.getDateOfEpoch(note.getEpochDeadLineDate());
            deadlineText.setText(context.getString(R.string.deadline_string, ThisApp.getFormattedDate(date)));

            long deltaEpochDate_inDays = note.getEpochDeadLineDate()/86400 - ThisApp.getEpochDate(LocalDateTime.now())/86400;

            cardColor = R.color.colorCard;      //цвет по умолчанию
            if (deltaEpochDate_inDays < 0) {
                cardColor = R.color.colorExpiredCard;
            }
            switch ((int) deltaEpochDate_inDays) {
                case -1:
                    deadlineText.setText(context.getString(R.string.deadline_string, "вчера"));
                    break;
                case 0:
                    cardColor = R.color.colorTodaysCard;
                    deadlineText.setText(context.getString(R.string.deadline_string, "сегодня"));
                    break;
                case 1:
                    deadlineText.setText(context.getString(R.string.deadline_string, "завтра"));
                    break;
                default:
                    break;
            }
        } else {
            deadlineText.setVisibility(View.GONE);
        }

        if (headerText.getText().length() == 0) headerText.setVisibility(View.GONE);
        else headerText.setVisibility(View.VISIBLE);
        if (bodyText.getText().length() == 0) bodyText.setVisibility(View.GONE);
        else bodyText.setVisibility(View.VISIBLE);

//        card.setCardBackgroundColor(ColorStateList.valueOf(context.getResources().getColor(cardColor)));
        card.setCardBackgroundColor(ContextCompat.getColor(context, cardColor));

        LocalDateTime time = ThisApp.getDateOfEpoch(note.getEpochModifyDate());
//        modifyDateText.setText(context.getString(R.string.modify_string, time.format(formatter)));
        modifyDateText.setVisibility(View.GONE);

        return view;
    }

    @Override
    public void notifyDataSetChanged() {
        ThisApp.sortNotes();
        super.notifyDataSetChanged();
    }
}

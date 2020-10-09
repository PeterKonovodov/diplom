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

import static com.konovodov.diplom.ThisApp.SECONDS_PER_DAY;

public class NotesAdapter extends BaseAdapter {

    private final Context context;
    private final LayoutInflater inflater;
    private final List<Note> noteList;


    public NotesAdapter(Context context, List<Note> noteList) {
        this.context = context;
        this.noteList = noteList;
        ThisApp.getNoteRepository().sortNotes();
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
            deadlineText.setText(context.getString(R.string.deadline_string_formatted,
                    ThisApp.getFormattedDate(date)));

            long currentEpochDate = ThisApp.getEpochDateNowTruncDays();
            long epochDeadLineDate = note.getEpochDeadLineDate();

            long deltaEpochDate_inDays = (epochDeadLineDate - currentEpochDate) / SECONDS_PER_DAY;

            cardColor = R.color.colorCard;      //цвет по умолчанию
            if (deltaEpochDate_inDays < 0) {
                cardColor = R.color.colorExpiredCard;
            }

            switch ((int) deltaEpochDate_inDays) {
                case -1:
                    deadlineText.setText(context.getString(R.string.deadline_string_formatted, context.getString(R.string.yesterday_string)));
                    break;
                case 0:
                    cardColor = R.color.colorTodaysCard;
                    deadlineText.setText(context.getString(R.string.deadline_string_formatted, context.getString(R.string.today_string)));
                    break;
                case 1:
                    deadlineText.setText(context.getString(R.string.deadline_string_formatted, context.getString(R.string.tomorrow_string)));
                    break;
                default:
                    break;
            }

        } else deadlineText.setVisibility(View.GONE);

        if(note.isCompleted()) {
            deadlineText.setText(context.getString(R.string.completed_string));
            deadlineText.setVisibility(View.VISIBLE);
            cardColor = R.color.colorDoneCard;
        }
        if (headerText.getText().length() == 0) headerText.setVisibility(View.GONE);
        else headerText.setVisibility(View.VISIBLE);
        if (bodyText.getText().length() == 0) bodyText.setVisibility(View.GONE);
        else bodyText.setVisibility(View.VISIBLE);

        card.setCardBackgroundColor(ContextCompat.getColor(context, cardColor));
        modifyDateText.setVisibility(View.GONE);

        return view;
    }

    @Override
    public void notifyDataSetChanged() {
        ThisApp.getNoteRepository().sortNotes();
        super.notifyDataSetChanged();
    }
}

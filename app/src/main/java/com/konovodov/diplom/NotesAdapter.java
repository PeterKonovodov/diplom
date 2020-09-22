package com.konovodov.diplom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class NotesAdapter extends BaseAdapter {

    Context context;
    private List<Note> notes;

    private LayoutInflater inflater;


    public NotesAdapter(Context context, List<Note> notes) {
        this.context = context;

        if (this.notes == null) {
            this.notes = new ArrayList<>();
        } else {
            this.notes = this.notes;
        }
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void addNote(Note note) {
        this.notes.add(note);
        notifyDataSetChanged();
    }

    public void removeNote(int position) {
        notes.remove(position);
        notifyDataSetChanged();
    }

/*
    public List<String> getAdapterStrings() {
        List<String> list = new ArrayList<>();
        for (Note item : notes) {
            list.add(item.getTitle());
        }
        return list;
    }
*/

    @Override
    public int getCount() {
        return notes.size();
    }

    @Override
    public Note getItem(int position) {
        if (position < notes.size()) {
            return notes.get(position);
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
            view = inflater.inflate(R.layout.note_list_view, parent, false);
        }

        Note itemData = notes.get(position);

//        ImageView image = view.findViewById(R.id.icon);
        TextView headerText = view.findViewById(R.id.header);
        TextView bodyText = view.findViewById(R.id.body);
//        Button delBtn = view.findViewById(R.id.delbtn);

        headerText.setText(itemData.getHeaderText());
        bodyText.setText(itemData.getBodyText());
//        delBtn.setTag(position);
//        delBtn.setOnClickListener(delBtnListener);

        return view;
    }
}

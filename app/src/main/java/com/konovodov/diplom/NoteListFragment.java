package com.konovodov.diplom;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class NoteListFragment extends Fragment {

    private View fragmentView;
    private NotesAdapter notesAdapter;
    private List<Note> noteList;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentView = inflater.inflate(R.layout.notes_list_view, container, false);
        initViews();
        return fragmentView;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void initViews() {

        FloatingActionButton fab = fragmentView.findViewById(R.id.fab);
        fab.setAlpha(0.5f);
        ListView notesListView = fragmentView.findViewById(R.id.notesListView);


        NoteEditor noteEditor = new NoteEditor(getActivity());
        noteList = ThisApp.getNoteRepository().getNotes();
        notesAdapter = new NotesAdapter(getActivity(), noteList);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                noteEditor.editNote(notesAdapter, noteList.size());
            }
        });

        notesListView.setAdapter(notesAdapter);

        notesListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long id) {
                noteEditor.editNote(notesAdapter, position);
                return true;
            }
        });
    }


}

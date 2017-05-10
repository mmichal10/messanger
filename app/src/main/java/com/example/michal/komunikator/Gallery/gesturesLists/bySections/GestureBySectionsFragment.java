package com.example.michal.komunikator.Gallery.gesturesLists.bySections;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.Filter;

import com.example.michal.komunikator.R;
import com.example.michal.komunikator.Gesture;

import java.util.ArrayList;

/**
 * Created by michal on 14.04.2017.
 */

public class GestureBySectionsFragment extends Fragment {

    private ArrayList<Gesture> gesturesList = null;
    private ArrayList<String> sectionsList = null;
    private GestureExpandablelistAdapter adapter = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_separator, container, false);

        Bundle arguments = getArguments();
        this.gesturesList = arguments.getParcelableArrayList("gestures");
        this.sectionsList = arguments.getStringArrayList("sections");

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        ExpandableListView elv = (ExpandableListView)this.getView().findViewById(R.id.expandableSections);
        adapter = new GestureExpandablelistAdapter(this.getContext(), sectionsList, gesturesList);

        elv.setAdapter(adapter);
    }

    public Filter getFilter(){
        return adapter.getFilter();
    }
}

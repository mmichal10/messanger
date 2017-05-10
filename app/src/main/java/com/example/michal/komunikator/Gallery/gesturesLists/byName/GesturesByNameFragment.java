package com.example.michal.komunikator.Gallery.gesturesLists.byName;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ListView;

import com.example.michal.komunikator.Gesture;
import com.example.michal.komunikator.R;

import java.util.ArrayList;

public class GesturesByNameFragment extends Fragment {

    private ArrayList<Gesture> gesturesList = null;
    private GestureListAdapter adapter = null;

    public GesturesByNameFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gestures_by_name, container, false);

        Bundle arguments = getArguments();
        this.gesturesList = arguments.getParcelableArrayList("gestures");

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        ListView listView = (ListView)this.getView().findViewById(R.id.gesturesListByName);

        adapter = new GestureListAdapter(getContext(), R.layout.list_element_layout, gesturesList);

        listView.setAdapter(adapter);
    }

    public Filter getFilter(){
        return adapter.getFilter();
    }

}

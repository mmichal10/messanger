package com.example.michal.komunikator.Gallery.gesturesLists.bySections;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.michal.komunikator.Gallery.gesturesLists.GalleryActivity;
import com.example.michal.komunikator.R;
import com.example.michal.komunikator.Gesture;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by michal on 14.04.2017.
 */

public class GestureExpandablelistAdapter  extends BaseExpandableListAdapter implements Filterable {

    private Context context = null;
    private HashMap<String, List<Gesture>> gesturesList = null;
    private List<Gesture> filteredGesturesList = null;
    private List<String> sectionsList = null;
    private Filter gesturesFilter = new GestureExpandablelistAdapter.GesturesFilter();


    public GestureExpandablelistAdapter(Context context, ArrayList<String> sectionsList, ArrayList<Gesture> gesturesList){
        this.context = context;
        this.sectionsList = sectionsList;
        this.gesturesList = new HashMap<>();
        for (Gesture g : gesturesList){
            if(!this.gesturesList.keySet().contains(g.getCategory()))
                this.gesturesList.put(g.getCategory(), new ArrayList<Gesture>());
            this.gesturesList.get(g.getCategory()).add(g);
        }
    }


    @Override
    public int getGroupCount() {
        return sectionsList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return gesturesList.get(sectionsList.get(groupPosition)).size();
    }

    @Override
    public String getGroup(int groupPosition) {
        return sectionsList.get(groupPosition);
    }

    @Override
    public Gesture getChild(int groupPosition, int childPosition) {
        return gesturesList.get(sectionsList.get(groupPosition)).get(childPosition);

    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String sectionName = getGroup(groupPosition);
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.separator, null);
        }

        TextView sectionTitle = (TextView)convertView.findViewById(R.id.listHeaderTitle);
        sectionTitle.setTypeface(null, Typeface.BOLD);
        sectionTitle.setText(sectionName);
        return convertView;
    }


    //tu sie odbywa cała magia (y)
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        View v = convertView;

        Gesture gesture = getChild(groupPosition, childPosition);

        if (v == null) {
            LayoutInflater vi =  LayoutInflater.from(context);
            v = vi.inflate(R.layout.list_element_layout, null);
        }

        TextView name = (TextView) v.findViewById(R.id.gestureName);
        TextView category = (TextView) v.findViewById(R.id.gestureCategory);
        ImageView symbol = (ImageView) v.findViewById(R.id.gestureSymbol);

        if (name != null)
            name.setText(gesture.getName());

        if (category != null)
            category.setText(gesture.getCategory());

        if (symbol != null)
            symbol.setImageDrawable(gesture.getSymbol());


        v.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){

                String name = ((TextView)v.findViewById(R.id.gestureName)).getText().toString();
                String category = ((TextView)v.findViewById(R.id.gestureCategory)).getText().toString();

                Gesture clickedGesture = null;
                for(Gesture g : gesturesList.get(category)){
                    if(g.getName().contains(name)){
                        clickedGesture = g;
                        break;
                    }
                }
                ((GalleryActivity)context).addGestureToMessege(clickedGesture);
                Log.i("GestureExpandListAdapt", "Message extended by " + clickedGesture.getName() + " gesture");
            }
        } );

        return  v;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public Filter getFilter(){
        return gesturesFilter;
    }

    private class GesturesFilter extends Filter{

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            List<Gesture> filteredList = new ArrayList<Gesture>();

            constraint = constraint.toString().toLowerCase();

//
//            for(Gesture g : gesturesList)
//                if(g.getName().toLowerCase().contains(constraint))
//                    filteredList.add(g);
//
//            results.count = filteredList.size();
//            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredGesturesList = (ArrayList<Gesture>) results.values;
            notifyDataSetChanged();
        }
    }


}

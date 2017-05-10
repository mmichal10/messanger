package com.example.michal.komunikator.Gallery.gesturesLists.byName;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.michal.komunikator.Gallery.gesturesLists.GalleryActivity;
import com.example.michal.komunikator.Gesture;
import com.example.michal.komunikator.R;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Created by michal on 10.04.2017.
 */

public class GestureListAdapter extends ArrayAdapter<Gesture> implements Filterable {

    private int layoutResource;
    private Context context = null;
    private List<Gesture> gesturesList = null;
    private List<Gesture> filteredGesturesList = null;
    private List<String> categoriesList = null;
    private Filter gesturesFilter = new GesturesFilter();


    public GestureListAdapter(Context context, int resource, List<Gesture> objects) {
        super(context, resource, objects);
        this.layoutResource = resource;
        gesturesList = objects;
        filteredGesturesList = objects;
        categoriesList = new ArrayList<>();
        this.context = context;
    }


    @Override
    public int getCount(){
        return filteredGesturesList.size();
    }

    @Override
    public Gesture getItem(int position){
        return filteredGesturesList.get(position);
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public Filter getFilter(){
        return gesturesFilter;
    }

    @Override
    public void sort(Comparator<? super Gesture> comparator){
        super.sort(comparator);
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent){
        View v = convertView;

        Gesture gesture = getItem(position);

        if (v == null) {
            LayoutInflater vi =  LayoutInflater.from(getContext());
            v = vi.inflate(layoutResource, null);
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

                Gesture clickedGesture = null;
                for(Gesture g : gesturesList){
                    if(g.getName().contains(name)){
                        clickedGesture = g;
                        break;
                    }
                }
                ((GalleryActivity)context).addGestureToMessege(clickedGesture);
                Log.i("GestureListAdapter", "Message extended by " + clickedGesture.getName() + " gesture");
            }
        } );

        return  v;
    }

    private class GesturesFilter extends Filter{

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            List<Gesture> filteredList = new ArrayList<Gesture>();

            constraint = constraint.toString().toLowerCase();

            for(Gesture g : gesturesList)
                if(g.getName().toLowerCase().contains(constraint))
                    filteredList.add(g);

            results.count = filteredList.size();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredGesturesList = (ArrayList<Gesture>) results.values;
            notifyDataSetChanged();
        }
    }

}
